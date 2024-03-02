package awesome.pizza.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_pizza")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Rome")
    @Column(name = "orderDateTime")
    private LocalDateTime orderDateTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Rome")
    @Column(name = "deliveryOrderDateTime")
    private LocalDateTime deliveryOrderDateTime;

    @Enumerated(value = EnumType.STRING)
    private StatusOrder status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public LocalDateTime getDeliveryOrderDateTime() {
        return deliveryOrderDateTime;
    }

    public void setDeliveryOrderDateTime(LocalDateTime deliveryOrderDateTime) {
        this.deliveryOrderDateTime = deliveryOrderDateTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime() {
        this.orderDateTime = LocalDateTime.now();
    }

    

    public StatusOrder getStatus() {
        return status;
    }

    public void setStatus(StatusOrder status) {
        this.status = status;
    }

}

