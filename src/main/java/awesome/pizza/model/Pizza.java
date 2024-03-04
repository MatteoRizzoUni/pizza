package awesome.pizza.model;

import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "pizza")
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 20)
    private String name;

    @Column(name = "description", nullable = false, length = 150)
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "available", columnDefinition = "boolean default false")
    private boolean available;
// OrderPizza Relationship
    @OneToMany(mappedBy = "pizza",
                     cascade = CascadeType.ALL,
                     fetch = FetchType.EAGER)
    private Set<OrderPizza> orderPizzas = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Set<OrderPizza> getOrderPizzas() {
        return orderPizzas;
    }

    public void setOrderPizzas(Set<OrderPizza> orderPizzas) {
        this.orderPizzas = orderPizzas;
    }

   
    
    

}
