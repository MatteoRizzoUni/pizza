package awesome.pizza.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import awesome.pizza.model.StatusOrder;

public class OrderResponse {
    @JsonProperty("message")
    private String message;
    private Long orderId;
    private LocalDateTime orderDateTime;
    private LocalDateTime deliveryDateTime;
    private StatusOrder status;
    private List<PizzaForOrderResponse> pizzas;
    private double subTotal;



    public OrderResponse(String message, Long orderId, LocalDateTime orderDateTime, LocalDateTime deliveryDateTime,
            StatusOrder status, List<PizzaForOrderResponse> pizzas, double subTotal) {
        this.message = message;
        this.orderId = orderId;
        this.orderDateTime = orderDateTime;
        this.deliveryDateTime = deliveryDateTime;
        this.status = status;
        this.pizzas = pizzas;
        this.subTotal = subTotal;
    }

    //TO DO: REMOVE
    public OrderResponse(String message, Long orderId, StatusOrder status, LocalDateTime orderDateTime, 
    LocalDateTime deliveryDateTime ) {
        this.message = message;
        this.orderId = orderId;
        this.orderDateTime = orderDateTime;
        this.deliveryDateTime = deliveryDateTime;
        this.status = status;
    }


    public double getSubTotal() {
        return subTotal;
    }

    public String getMessage() {
        return message;
    }

    public Long getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public LocalDateTime getDeliveryDateTime() {
        return deliveryDateTime;
    }

    public StatusOrder getStatus() {
        return status;
    }

    public List<PizzaForOrderResponse> getPizzas() {
        return pizzas;
    }

    
    

}
