package awesome.pizza.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import awesome.pizza.model.Customer;
import awesome.pizza.model.Order;
import awesome.pizza.response.CustomerOrderResponse;
import awesome.pizza.response.OrderResponse;
import awesome.pizza.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class OrderController {

    
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/add-order")
    public ResponseEntity<CustomerOrderResponse> addOrder(@RequestBody CustomerOrderRequestWrapped customerOrderRequestWrapped) {

        try {
            Customer customerRequest = customerOrderRequestWrapped.getCustomer();
            Order orderRequest = customerOrderRequestWrapped.getOrder();
            return ResponseEntity.ok(orderService.addOrder(customerRequest, orderRequest));

        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }


    }

    @GetMapping("/order/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(orderService.getOrder(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    
}