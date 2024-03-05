package awesome.pizza.response;

public class PizzaForOrderResponse {
    private String namePizza;
    private int quantity;
    private double price;

    
    public PizzaForOrderResponse() {
    }

    public PizzaForOrderResponse(String namePizza, int quantity, double price) {
        this.namePizza = namePizza;
        this.quantity = quantity;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getNamePizza() {
        return namePizza;
    }
    public void setNamePizza(String namePizza) {
        this.namePizza = namePizza;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    

}
