package awesome.pizza.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import awesome.pizza.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

    Optional<Employee> findByUsername(String username);
    // public List<Employee> findByCognome(String lastName);

}
