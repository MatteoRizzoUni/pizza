package awesome.pizza.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import awesome.pizza.model.StatusOrder;

public class OrderResponse {
    @JsonProperty("message")
    private String message;
    private Long orderId;
    private LocalDateTime orderDateTime;
    private LocalDateTime deliveryDateTime;
    private StatusOrder status;

    public OrderResponse(String message, Long orderId, StatusOrder status, LocalDateTime orderDateTime, 
                        LocalDateTime deliveryDateTime ) {
        this.message = message;
        this.orderId = orderId;
        this.orderDateTime = orderDateTime;
        this.deliveryDateTime = deliveryDateTime;
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getMessage() {
        return message;
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
    

}
