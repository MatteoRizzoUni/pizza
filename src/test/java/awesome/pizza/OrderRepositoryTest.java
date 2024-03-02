package awesome.pizza;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import awesome.pizza.model.Customer;
import awesome.pizza.model.Order;
import awesome.pizza.model.StatusOrder;
import awesome.pizza.repository.CustomerRepository;
import awesome.pizza.repository.OrderRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateOrder(){
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPhoneNumber("1234567890"); 
        Customer savedCustomer = customerRepository.save(customer); 
        Assertions.assertThat(savedCustomer).isNotNull();

        Order order = new Order();
        order.setOrderDateTime();
        order.setDeliveryOrderDateTime(LocalDateTime.now());
        order.setStatus(StatusOrder.DELIVERED);
        order.setCustomer(savedCustomer);

        Order savedOrder = orderRepository.save(order);
        Order existOrder = entityManager.find(Order.class, savedOrder.getId());

        Assertions.assertThat(savedOrder).isNotNull();
        Assertions.assertThat(orderRepository.findById(savedOrder.getId()).isPresent());
        Assertions.assertThat(!orderRepository.findById(5L).isPresent());
        Assertions.assertThat(existOrder.getCustomer()).isEqualTo(savedCustomer);
    }
}
