package awesome.pizza.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import awesome.pizza.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

    Optional<Customer> findCustomerById(Long id);
    Optional<Customer> findCustomerByPhoneNumber(String phoneNumber);

}
