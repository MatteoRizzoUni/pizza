package awesome.pizza.response;

import java.time.LocalDateTime;

import awesome.pizza.model.StatusOrder;

public class OrderResponse {
    private Long orderId;
    private LocalDateTime orderDateTime;
    private LocalDateTime deliveryDateTime;
    private StatusOrder status;

    public OrderResponse(Long orderId, StatusOrder status, LocalDateTime orderDateTime, 
                        LocalDateTime deliveryDateTime ) {
        this.orderId = orderId;
        this.orderDateTime = orderDateTime;
        this.deliveryDateTime = deliveryDateTime;
        this.status = status;
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
    

}
