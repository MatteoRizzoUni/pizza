package awesome.pizza.response;

public class CustomerResponse {
    private String customerFirstName;
    private String customerLastName;


    public CustomerResponse( String customerFirstName, String customerLastName) {
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }
    

}
