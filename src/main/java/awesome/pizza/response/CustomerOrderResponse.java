package awesome.pizza.response;


import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerOrderResponse {
    @JsonProperty("customer")
    private CustomerResponse customer;
    @JsonProperty("order")
    private OrderResponse order;

    public CustomerOrderResponse(CustomerResponse customer, OrderResponse order) {
        this.customer = customer;
        this.order = order;
    }

    public CustomerResponse getCustomer() {
        return customer;
    }

    public OrderResponse getOrder() {
        return order;
    }

    

}
