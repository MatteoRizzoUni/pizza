package awesome.pizza.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import awesome.pizza.model.Pizza;
import awesome.pizza.response.PizzaResponse;
import awesome.pizza.service.PizzaService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class PizzaController {

    private final PizzaService pizzaService;

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @PostMapping("/add-pizza")
    public ResponseEntity<PizzaResponse> addPizza(@RequestBody Pizza pizzaRequest) {
        try {
            
            return ResponseEntity.ok(pizzaService.addPizza(pizzaRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/pizza/{id}")
    public ResponseEntity<PizzaResponse> getPizza(@PathVariable("id")  Long id) {
        try {
            return ResponseEntity.ok(pizzaService.getPizza(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pizzas")
    public ResponseEntity<Iterable<PizzaResponse>> getAllPizzas() {
        try {
            return ResponseEntity.ok(pizzaService.getAllPizzas());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pizzas-available")
    public ResponseEntity<Iterable<PizzaResponse>> getPizzasAvailable() {
        try {
            return ResponseEntity.ok(pizzaService.getPizzasAvailable());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update-pizza/{id}")
    public ResponseEntity<PizzaResponse> updatePizza(@PathVariable("id") Long id, 
                                                    @RequestBody Pizza pizzaRequest) {
        try {
            return ResponseEntity.ok(pizzaService.updatePizza(id, pizzaRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    
}
