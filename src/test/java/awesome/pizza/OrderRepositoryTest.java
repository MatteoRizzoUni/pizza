package awesome.pizza;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import awesome.pizza.model.Customer;
import awesome.pizza.model.Order;
import awesome.pizza.model.OrderPizza;
import awesome.pizza.model.Pizza;
import awesome.pizza.model.StatusOrder;
import awesome.pizza.repository.CustomerRepository;
import awesome.pizza.repository.OrderPizzaRepository;
import awesome.pizza.repository.OrderRepository;
import awesome.pizza.repository.PizzaRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PizzaRepository pizzaRepository;
    @Autowired
    private OrderPizzaRepository orderPizzaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateOrder(){
// Customer
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPhoneNumber("1234567890"); 
        Customer savedCustomer = customerRepository.save(customer); 
        Assertions.assertThat(savedCustomer).isNotNull();
// Pizza1
        Pizza pizza1 = new Pizza();
        pizza1.setName("Margherita");
        pizza1.setPrice(12.5);
        pizza1.setDescription("Tomato, mozzarella, basil");
        pizza1.setAvailable(true);
        
// Pizza2
        Pizza pizza2 = new Pizza();
        pizza2.setName("Pepperoni");
        pizza2.setPrice(14.5);
        pizza2.setDescription("Pepperoni, tomato, mozzarella");
        pizza2.setAvailable(true);
// Order
        Order order = new Order();
        order.setOrderDateTime();
        order.setDeliveryOrderDateTime(LocalDateTime.now());
        order.setStatus(StatusOrder.DELIVERED);
        order.setCustomer(savedCustomer);
// OrderPizza
        Set<OrderPizza> setPizzas = new HashSet<>();
        OrderPizza orderPizza1 = new OrderPizza();
        orderPizza1.setOrder(order);
        orderPizza1.setPizza(pizza1);
        orderPizza1.setQuantity(2);
        orderPizza1.setSubTotal(pizza1.getPrice() * orderPizza1.getQuantity());
        setPizzas.add(orderPizza1);
        OrderPizza orderPizza2 = new OrderPizza();
        orderPizza2.setOrder(order);
        orderPizza2.setPizza(pizza2);
        orderPizza2.setQuantity(1);
        orderPizza2.setSubTotal(pizza2.getPrice() * orderPizza2.getQuantity());
        setPizzas.add(orderPizza2);

//Order pizzas
        order.setOrderPizzas(setPizzas);
        pizza1.setOrderPizzas(setPizzas);
        pizza2.setOrderPizzas(setPizzas);



        Pizza savedPizza1 = pizzaRepository.save(pizza1);
        Pizza savedPizza2 = pizzaRepository.save(pizza2);
        Order savedOrder = orderRepository.save(order);
        OrderPizza savedOrderPizza1 = orderPizzaRepository.save(orderPizza1);
        OrderPizza savedOrderPizza2 = orderPizzaRepository.save(orderPizza2);


        Order existOrder = entityManager.find(Order.class, savedOrder.getId());
        Pizza existPizza1 = entityManager.find(Pizza.class, savedPizza1.getId());
        Pizza existPizza2 = entityManager.find(Pizza.class, savedPizza2.getId());
        OrderPizza existOrderPizza = entityManager.find(OrderPizza.class, savedOrder.getId());

        Assertions.assertThat(savedOrder).isNotNull();
        Assertions.assertThat(savedPizza1).isNotNull();
        Assertions.assertThat(savedPizza2).isNotNull();
        Assertions.assertThat(savedOrderPizza1).isNotNull();
        Assertions.assertThat(savedOrderPizza2).isNotNull();

        Assertions.assertThat(orderPizzaRepository.findByOrderId(savedOrder.getId()).size()).isEqualTo(2);
        Assertions.assertThat(savedOrder.getOrderPizzas().size()).isEqualTo(2);
        Assertions.assertThat(savedOrder.getOrderPizzas().contains(savedOrderPizza1));
        Assertions.assertThat(savedOrder.getOrderPizzas().contains(savedOrderPizza2));
        Assertions.assertThat(savedOrder.getCustomer()).isEqualTo(savedCustomer);
        Assertions.assertThat(savedPizza1.getOrderPizzas().contains(savedOrderPizza1));
        Assertions.assertThat(savedPizza2.getOrderPizzas().contains(savedOrderPizza2));




        Assertions.assertThat(orderRepository.findById(savedOrder.getId()).isPresent());
        Assertions.assertThat(!orderRepository.findById(5L).isPresent());
        Assertions.assertThat(existOrder.getCustomer()).isEqualTo(savedCustomer);
    }
}
