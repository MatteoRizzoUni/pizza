package awesome.pizza.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import awesome.pizza.model.Order;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long>{
    
    Optional<Order> findById(Long id);
    List<Order> findAllByOrderByDeliveryOrderDateTimeAsc();
    List<Order> findAllByOrderByDeliveryOrderDateTimeDesc();

}
