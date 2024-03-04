package awesome.pizza.service;

import org.springframework.stereotype.Service;

import awesome.pizza.model.Pizza;
import awesome.pizza.repository.PizzaRepository;
import awesome.pizza.response.PizzaResponse;

@Service
public class PizzaService {
    
    private final PizzaRepository pizzaRepository;

    public PizzaService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    public PizzaResponse addPizza(Pizza requestPizza) {
        Pizza pizza = new Pizza();

        if (pizzaRepository.findByName(requestPizza.getName()).isPresent()){
            pizza = pizzaRepository.findByName(requestPizza.getName()).get();

            return new PizzaResponse("ERROR: Pizza already exists!", pizza.getId(), pizza.getName(), 
                                    pizza.getDescription(), pizza.getPrice(),
                                    pizza.isAvailable());
        }
        
        pizza.setName(requestPizza.getName());
        pizza.setDescription(requestPizza.getDescription());
        pizza.setPrice(requestPizza.getPrice());
        pizza.setAvailable(requestPizza.isAvailable());

        pizza = pizzaRepository.save(pizza);

        return new PizzaResponse("Pizza added successfully!", pizza.getId(), pizza.getName(), 
                                pizza.getDescription(), pizza.getPrice(),
                                pizza.isAvailable());
    }
    

    public PizzaResponse getPizza(Long id) {
        Pizza pizza = pizzaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Pizza not found"));

        return new PizzaResponse(null, pizza.getId(), pizza.getName(), 
                                pizza.getDescription(), pizza.getPrice(),
                                pizza.isAvailable());
    }

    public Iterable<PizzaResponse> getAllPizzas() {
        return pizzaRepository.findAll().stream()
                                .map(pizza -> new PizzaResponse(null, 
                                        pizza.getId(), pizza.getName(), 
                                        pizza.getDescription(), pizza.getPrice(),
                                        pizza.isAvailable())).toList();

    }

    public Iterable<PizzaResponse> getPizzasAvailable() {
        return pizzaRepository.findByAvailable(true).stream()
                                .map(pizza -> new PizzaResponse(null, 
                                        pizza.getId(), pizza.getName(), 
                                        pizza.getDescription(), pizza.getPrice(),
                                        pizza.isAvailable())).toList();
    }

    public PizzaResponse updatePizza(Long id, Pizza requestPizza) {
        Pizza pizza = pizzaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pizza not found"));
        
        pizza.setDescription(requestPizza.getDescription());
        pizza.setPrice(requestPizza.getPrice());
        pizza.setAvailable(requestPizza.isAvailable());

        pizza = pizzaRepository.save(pizza);

        return new PizzaResponse(null, pizza.getId(), pizza.getName(), 
                                pizza.getDescription(), pizza.getPrice(),
                                pizza.isAvailable());

    }
}
