package awesome.pizza;



import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import awesome.pizza.model.Customer;
import awesome.pizza.repository.CustomerRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CustomRepositoryTest {
    
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateCustomer(){

        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPhoneNumber("1234567890");

        Customer savedCustomer = customerRepository.save(customer);
        Customer existCustomer = entityManager.find(Customer.class, savedCustomer.getId());

        Assertions.assertThat(savedCustomer).isNotNull();
        Assertions.assertThat(savedCustomer.getId()).isGreaterThan(0);

        Assertions.assertThat(customerRepository.findCustomerByPhoneNumber(savedCustomer.getPhoneNumber()).isPresent());
        Assertions.assertThat(customerRepository.findCustomerByPhoneNumber(existCustomer.getPhoneNumber()).isPresent());

        Assertions.assertThat(customerRepository.findCustomerById(savedCustomer.getId()).isPresent());
        Assertions.assertThat(customerRepository.findCustomerById(existCustomer.getId()).isPresent());


        Assertions.assertThat(existCustomer.getPhoneNumber()).isEqualTo(savedCustomer.getPhoneNumber());
    }

}

