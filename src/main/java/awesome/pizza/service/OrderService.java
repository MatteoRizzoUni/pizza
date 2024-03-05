package awesome.pizza.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import awesome.pizza.model.Customer;
import awesome.pizza.model.Employee;
import awesome.pizza.model.Order;
import awesome.pizza.model.OrderPizza;
import awesome.pizza.model.Pizza;
import awesome.pizza.model.StatusOrder;
import awesome.pizza.model.Token;
import awesome.pizza.repository.CustomerRepository;
import awesome.pizza.repository.EmployeeRepository;
import awesome.pizza.repository.OrderPizzaRepository;
import awesome.pizza.repository.OrderRepository;
import awesome.pizza.repository.PizzaRepository;
import awesome.pizza.repository.TokenRepository;
import awesome.pizza.request.PizzaQuantityRequest;
import awesome.pizza.response.CustomerOrderResponse;
import awesome.pizza.response.CustomerResponse;
import awesome.pizza.response.OrderResponse;
import awesome.pizza.response.PizzaForOrderResponse;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final PizzaRepository pizzaRepository;
    private final OrderPizzaRepository orderPizzaRepository;
    private final TokenRepository tokenRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, 
                        EmployeeRepository employeeRepository, PizzaRepository pizzaRepository,
                        OrderPizzaRepository orderPizzaRepository, TokenRepository tokenRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.pizzaRepository = pizzaRepository;
        this.employeeRepository = employeeRepository;
        this.orderPizzaRepository = orderPizzaRepository;
        this.tokenRepository = tokenRepository;
    }


    public CustomerOrderResponse addOrder(Customer customerRequest, 
                                            LocalDateTime orderDateTimeRequest,
                                            List<PizzaQuantityRequest> pizzasRequestList){
        //CUSTOMER SET                                        
        Customer customer = new Customer();
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());

        customerRepository.findCustomerByPhoneNumber(customerRequest.getPhoneNumber()).orElseGet(() -> {
            return customerRepository.save(customer);
        });

        //PIZZA SET
        HashMap<Pizza, Integer> pizzas = new HashMap<>();
        for(PizzaQuantityRequest pizzaRequest:  pizzasRequestList){
             pizzas.put(pizzaRepository.findByName(pizzaRequest.getNamePizza())
                                        .orElseThrow(() -> new RuntimeException("Pizza not found")), 
                                        pizzaRequest.getQuantity());
         }

        Customer customerSaved = customerRepository.findCustomerByPhoneNumber(customer.getPhoneNumber()).get();

        //ORDER SET
        Order order = new Order();
        order.setOrderDateTime();
        order.setDeliveryOrderDateTime(orderDateTimeRequest);
        order.setStatus(StatusOrder.RECEIVED);
        order.setCustomer(customerSaved);

        Order orderSaved = orderRepository.save(order);

        List<PizzaForOrderResponse> pizzasResponse = new ArrayList<>();
        
        pizzas.forEach((pizza, quantity) -> {

            //ORDERPIZZA SET
            OrderPizza orderPizza = new OrderPizza();
            orderPizza.setOrder(orderSaved);
            orderPizza.setPizza(pizza);
            orderPizza.setQuantity(quantity);
            orderPizza.setSubTotal(pizza.getPrice() * orderPizza.getQuantity());
            orderPizzaRepository.save(orderPizza);
            //ORDERPIZZA RESPONSE SET
            PizzaForOrderResponse pizzaResponse = new PizzaForOrderResponse();
            pizzaResponse.setNamePizza(pizza.getName());
            pizzaResponse.setQuantity(quantity);
            pizzaResponse.setPrice(pizza.getPrice()* orderPizza.getQuantity());
            pizzasResponse.add(pizzaResponse);
            
        });

        double totalAmount = totalPriceOrder(pizzasResponse);

        return new CustomerOrderResponse(new CustomerResponse(customerSaved.getFirstName(),customerSaved.getLastName()),
                                        new OrderResponse("Order added", orderSaved.getId(), 
                                                        orderSaved.getOrderDateTime(), orderSaved.getDeliveryOrderDateTime(),
                                                        StatusOrder.RECEIVED, pizzasResponse, totalAmount));
    }


    private double totalPriceOrder(List<PizzaForOrderResponse> pizzasResponse){
        double totalAmount = 0;
        for(PizzaForOrderResponse pizza: pizzasResponse){
            totalAmount += pizza.getPrice();
        }
        return totalAmount;
    }


    public OrderResponse getOrder(Long id) {

        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        
        List<PizzaForOrderResponse> pizzasResponse = new ArrayList<>();

        orderPizzaRepository.findByOrderId(id).forEach(orderPizza -> {
            PizzaForOrderResponse pizzaResponse = new PizzaForOrderResponse();
            pizzaResponse.setNamePizza(orderPizza.getPizza().getName());
            pizzaResponse.setQuantity(orderPizza.getQuantity());
            pizzaResponse.setPrice(orderPizza.getSubTotal());
            pizzasResponse.add(pizzaResponse);
        });

        double totalAmount = totalPriceOrder(pizzasResponse);

        return new OrderResponse(null, order.getId(), 
                                order.getOrderDateTime(), order.getDeliveryOrderDateTime(),
                                order.getStatus(), pizzasResponse, totalAmount);

    }

    public OrderResponse putOrder(Long id, Order orderRequest, String tokenRequest) {
        // find order
        Order order = orderRepository.findById(id)
                                    .orElseThrow(() -> new RuntimeException("Order not found"));
        // find user by token
        Token token = tokenRepository.findByToken(tokenRequest)
                                    .orElseThrow(() -> new RuntimeException("Token not found"));
        // finde employee by username from user
        Employee employee = employeeRepository.findByUsername(token.getUser().getUsername())
                                            .orElseThrow(() -> new RuntimeException("Employee not found"));

        //EMPLOYEE ALREADY GET THIS ORDER
        if( order.getEmployee() != null && order.getEmployee().getId() != employee.getId() ){
            return new OrderResponse("Order NOT updated: EMPLOYEE ALREADY GET THIS ORDER", 
                                    order.getId(), order.getStatus(), 
                                    order.getOrderDateTime(), order.getDeliveryOrderDateTime());
        }
        
        if(!employee.isBusy()){
            if( orderRequest.getStatus() == StatusOrder.IN_PROGRESS && order.getStatus() == StatusOrder.RECEIVED){

                changeEmployeeAndOrder(employee, employee, true, order, orderRequest);

                return new OrderResponse("Order get IN PROGRESS successfully", 
                                    order.getId(), order.getStatus(), 
                                    order.getOrderDateTime(), order.getDeliveryOrderDateTime());

            } else if( orderRequest.getStatus() == StatusOrder.CANCELED && order.getStatus() == StatusOrder.RECEIVED){
                changeOrder(order, orderRequest, employee);

                return new OrderResponse("Order CANCELED successfully", 
                                    order.getId(), order.getStatus(), 
                                    order.getOrderDateTime(), order.getDeliveryOrderDateTime());

            }else if( orderRequest.getStatus() == StatusOrder.RECEIVED && order.getStatus() == StatusOrder.CANCELED){
                changeOrder(order, orderRequest, null);
                

                return new OrderResponse("Order RETRIEVED successfully", 
                                    order.getId(), order.getStatus(), 
                                    order.getOrderDateTime(), order.getDeliveryOrderDateTime());
            }else{
                return new OrderResponse("ORDER NOT UPDATED: METOD NOT ALLOWED", 
                                                    order.getId(), order.getStatus(), 
                                                    order.getOrderDateTime(), order.getDeliveryOrderDateTime());
            }
  
        }else{
            if(orderRequest.getStatus() == StatusOrder.RECEIVED && order.getStatus() == StatusOrder.IN_PROGRESS){
                    
                changeEmployeeAndOrder(employee,  null,false, order, orderRequest);

                return new OrderResponse("Order LEFT successfully", 
                        order.getId(), order.getStatus(), 
                        order.getOrderDateTime(), order.getDeliveryOrderDateTime());
            }
            else if((orderRequest.getStatus() == StatusOrder.RECEIVED || orderRequest.getStatus() == StatusOrder.CANCELED)
                    && (order.getStatus() == StatusOrder.RECEIVED || order.getStatus() == StatusOrder.CANCELED)){

                changeOrder(order, orderRequest, (orderRequest.getStatus() == StatusOrder.RECEIVED) ? null : employee);
                
                return new OrderResponse("Order updated successfully", 
                        order.getId(), order.getStatus(), 
                        order.getOrderDateTime(), order.getDeliveryOrderDateTime());

            }else if(orderRequest.getStatus() == StatusOrder.DELIVERED && order.getStatus() == StatusOrder.IN_PROGRESS){

                changeEmployeeAndOrder(employee, employee, false, order, orderRequest);

                return new OrderResponse("Order DELIVERED successfully", 
                        order.getId(), order.getStatus(), 
                        order.getOrderDateTime(), order.getDeliveryOrderDateTime());
            }
            
            return new OrderResponse("ORDER NOT UPDATED: METOD NOT ALLOWED", 
                                                    order.getId(), order.getStatus(), 
                                                    order.getOrderDateTime(), order.getDeliveryOrderDateTime());
        }
        
    }

    private void changeEmployeeAndOrder(Employee employee, Employee employeeToSet, boolean bool, Order order, Order orderRequest){
        employee.setBusy(bool);
        employeeRepository.save(employee);

        order.setStatus(orderRequest.getStatus());
        order.setEmployee(employeeToSet);
        orderRepository.save(order);
    }

    private void changeOrder(Order order, Order orderRequest, Employee employeeToSet){

        order.setStatus(orderRequest.getStatus());
        order.setEmployee(employeeToSet);
        order = orderRepository.save(order);

    }
    

    public Iterable<OrderResponse> getAllOrdersAsc() {

        return orderRepository.findAllByOrderByDeliveryOrderDateTimeAsc().stream()
                            .map(order -> new OrderResponse(null, order.getId(), order.getStatus(), 
                                order.getOrderDateTime(), order.getDeliveryOrderDateTime())).toList();

    }
    public Iterable<OrderResponse> getAllOrdersDesc() {

        return orderRepository.findAllByOrderByDeliveryOrderDateTimeDesc().stream()
                            .map(order -> new OrderResponse(null, order.getId(), order.getStatus(), 
                                order.getOrderDateTime(), order.getDeliveryOrderDateTime())).toList();

    }

    

}
