package awesome.pizza.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import awesome.pizza.model.Pizza;

public interface PizzaRepository extends JpaRepository<Pizza, Long>{

    Optional<Pizza> findByName(String name);

}
