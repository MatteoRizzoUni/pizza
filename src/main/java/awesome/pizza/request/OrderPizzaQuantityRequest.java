package awesome.pizza.request;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


public class OrderPizzaQuantityRequest {
    @JsonProperty("deliveryOrderDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliveryOrderDateTime;

    @JsonProperty("pizza")
    private List<PizzaQuantityRequest> pizza;

    public LocalDateTime getDeliveryOrderDateTime() {
        return deliveryOrderDateTime;
    }

    public void setDeliveryOrderDateTime(LocalDateTime deliveryOrderDateTime) {
        this.deliveryOrderDateTime = deliveryOrderDateTime;
    }

    public List<PizzaQuantityRequest> getPizza() {
        return pizza;
    }

    public void setPizza(List<PizzaQuantityRequest> pizza) {
        this.pizza = pizza;
    }

    
    

    

    

}
