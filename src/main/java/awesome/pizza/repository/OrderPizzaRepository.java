package awesome.pizza.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import awesome.pizza.model.OrderPizza;

public interface OrderPizzaRepository extends JpaRepository<OrderPizza, Long>{

    List<OrderPizza> findByOrderId(Long orderId);

}

