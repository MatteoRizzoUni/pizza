package awesome.pizza.service;

import org.springframework.stereotype.Service;

import awesome.pizza.model.Customer;
import awesome.pizza.model.Order;
import awesome.pizza.model.StatusOrder;
import awesome.pizza.repository.CustomerRepository;
import awesome.pizza.repository.OrderRepository;
import awesome.pizza.response.CustomerOrderResponse;
import awesome.pizza.response.CustomerResponse;
import awesome.pizza.response.OrderResponse;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
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
                                        new OrderResponse(order.getId(), order.getStatus(), 
                                                        order.getOrderDateTime(), order.getDeliveryOrderDateTime()));


    }

    private CustomerResponse CustomerResponse() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'CustomerResponse'");
    }

    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

        return new OrderResponse(order.getId(), order.getStatus(), 
                                order.getOrderDateTime(), order.getDeliveryOrderDateTime());
    }

}
