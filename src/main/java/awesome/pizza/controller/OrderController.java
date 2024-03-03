package awesome.pizza.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import awesome.pizza.model.Customer;
import awesome.pizza.model.Order;
import awesome.pizza.response.CustomerOrderResponse;
import awesome.pizza.response.OrderResponse;
import awesome.pizza.service.JwtService;
import awesome.pizza.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class OrderController {

    
    private final OrderService orderService;
    private final JwtService jwtService;

    

    public OrderController(OrderService orderService, JwtService jwtService) {
        this.orderService = orderService;
        this.jwtService = jwtService;
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

    @PutMapping("/status-order/{id}")
    public ResponseEntity<OrderResponse> putOrderById(
                            @PathVariable("id") Long id, @RequestBody Order order,
                            HttpServletRequest request, HttpServletResponse response,
                            Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        
        if(authHeader == null || !authHeader.startsWith("Bearer ")){

            return ResponseEntity.badRequest().build();
        }

        String token = authHeader.substring(7);
        try {
            System.out.println("token: " + token);
            System.out.println("id: " + id);
            System.out.println("order: " + order.getStatus());
            return ResponseEntity.ok(orderService.putOrder(id, order, token));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all-orders")
    public ResponseEntity<Iterable<OrderResponse>> getAllOrders(@RequestParam(value = "sort", required = false) String sort) {

        if (sort != null && sort.equals("desc")) {
            try {
                return ResponseEntity.ok(orderService.getAllOrdersDesc());
            } catch (RuntimeException e) {
                return ResponseEntity.notFound().build();
            }
        } else  {
            try {
                return ResponseEntity.ok(orderService.getAllOrdersAsc());
            } catch (RuntimeException e) {
                return ResponseEntity.notFound().build();
            }
        }
    }
    
    
}