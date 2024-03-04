package awesome.pizza.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PizzaResponse {
    @JsonProperty("message")
    private String message;
    private Long id;
    private String name;
    private String description;
    private Double price;
    private boolean available;
    

    public PizzaResponse(String message, Long id, String name, String description, Double price, boolean available) {
        this.message = message;
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;

    }

    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


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


    

    

}
