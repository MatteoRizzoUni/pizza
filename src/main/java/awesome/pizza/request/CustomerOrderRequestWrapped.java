package awesome.pizza.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import awesome.pizza.model.Customer;


public class CustomerOrderRequestWrapped {
    private Customer customer;
    @JsonProperty("order")
    private OrderPizzaQuantityRequest order;
    
   
    public OrderPizzaQuantityRequest getOrder() {
        return order;
    }

    public void setOrder(OrderPizzaQuantityRequest order) {
        this.order = order;
    }

    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    

}
