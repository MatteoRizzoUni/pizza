package awesome.pizza.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import awesome.pizza.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUsername(String username);
    // public List<User> findByCognome(String lastName);

}
