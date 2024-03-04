package awesome.pizza.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
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
// Customer Relationship
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
// Employee Relationship
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = true)
    private Employee employee;
// Pizza Relationship
    @OneToMany(mappedBy = "order", 
                cascade = CascadeType.ALL,
                fetch = FetchType.EAGER)
    private Set<OrderPizza> orderPizzas = new HashSet<>();



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
    public LocalDateTime getDeliveryOrderDateTime() {
        return deliveryOrderDateTime;
    }
    public void setDeliveryOrderDateTime(LocalDateTime deliveryOrderDateTime) {
        this.deliveryOrderDateTime = deliveryOrderDateTime;
    }
    public StatusOrder getStatus() {
        return status;
    }
    public void setStatus(StatusOrder status) {
        this.status = status;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public Set<OrderPizza> getOrderPizzas() {
        return orderPizzas;
    }
    public void setOrderPizzas(Set<OrderPizza> orderPizzas) {
        this.orderPizzas = orderPizzas;
    }
    

// public BigDecimal getTotalAmount() {
//        BigDecimal amount = new BigDecimal("0.0");
//        for(OrderItem item : this.orderItems) {
//            amount = amount.add(item.getPrice());
//        }
//        return amount;
//     }

}

