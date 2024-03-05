package awesome.pizza.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import awesome.pizza.model.Customer;
import awesome.pizza.model.Order;
import awesome.pizza.request.CustomerOrderRequestWrapped;
import awesome.pizza.request.OrderPizzaQuantityRequest;
import awesome.pizza.request.PizzaQuantityRequest;
import awesome.pizza.response.CustomerOrderResponse;
import awesome.pizza.response.OrderResponse;
import awesome.pizza.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class OrderController {

    
    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/add-order")
    public ResponseEntity<CustomerOrderResponse> addOrder(
                            @RequestBody CustomerOrderRequestWrapped customerOrderRequestWrapped) {

        try {
            Customer customerRequest = customerOrderRequestWrapped.getCustomer();
            OrderPizzaQuantityRequest orderPizzaRequest = customerOrderRequestWrapped.getOrder();
            LocalDateTime orderDateTimeRequest = orderPizzaRequest.getDeliveryOrderDateTime();

          
            List<PizzaQuantityRequest> pizzaQuantityRequesList = orderPizzaRequest.getPizza();

            return ResponseEntity.ok(orderService.addOrder(customerRequest, 
                                                            orderDateTimeRequest, 
                                                            pizzaQuantityRequesList));

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
            
            return ResponseEntity.ok(orderService.putOrder(id, order, token));

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().build();
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