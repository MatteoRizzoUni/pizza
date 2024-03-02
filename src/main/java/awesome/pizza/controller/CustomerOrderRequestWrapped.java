package awesome.pizza.controller;

import awesome.pizza.model.Customer;
import awesome.pizza.model.Order;

public class CustomerOrderRequestWrapped {
    private Customer customer;
    private Order order;
    
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }

}
