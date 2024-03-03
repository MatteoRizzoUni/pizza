package awesome.pizza.service;

import org.springframework.stereotype.Service;

import awesome.pizza.model.Customer;
import awesome.pizza.model.Employee;
import awesome.pizza.model.Order;
import awesome.pizza.model.StatusOrder;
import awesome.pizza.model.Token;
import awesome.pizza.repository.CustomerRepository;
import awesome.pizza.repository.EmployeeRepository;
import awesome.pizza.repository.OrderRepository;
import awesome.pizza.repository.TokenRepository;
import awesome.pizza.response.CustomerOrderResponse;
import awesome.pizza.response.CustomerResponse;
import awesome.pizza.response.OrderResponse;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final TokenRepository tokenRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, EmployeeRepository employeeRepository, TokenRepository tokenRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.tokenRepository = tokenRepository;
    }

    public CustomerOrderResponse addOrder(Customer requestCustomer, Order requestOrder) {
        Customer customer = new Customer();
        customer.setFirstName(requestCustomer.getFirstName());
        customer.setLastName(requestCustomer.getLastName());
        customer.setPhoneNumber(requestCustomer.getPhoneNumber());

        customerRepository.findCustomerByPhoneNumber(requestCustomer.getPhoneNumber()).orElseGet(() -> {
            return customerRepository.save(customer);
        });

        Order order = new Order();
        order.setOrderDateTime();
        order.setDeliveryOrderDateTime(requestOrder.getDeliveryOrderDateTime());
        order.setStatus(StatusOrder.RECEIVED);
        order.setCustomer(customerRepository.findCustomerByPhoneNumber(requestCustomer.getPhoneNumber()).get());

        order = orderRepository.save(order);

        return new CustomerOrderResponse(new CustomerResponse(customer.getFirstName(),customer.getLastName()),
                                        new OrderResponse("Order added", order.getId(), order.getStatus(), 
                                                        order.getOrderDateTime(), order.getDeliveryOrderDateTime()));

    }

    public OrderResponse getOrder(Long id) {

        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

        return new OrderResponse(null, order.getId(), order.getStatus(), 
                                order.getOrderDateTime(), order.getDeliveryOrderDateTime());
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
