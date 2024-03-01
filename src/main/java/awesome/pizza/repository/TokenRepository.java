package awesome.pizza.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import awesome.pizza.model.Token;



public interface TokenRepository extends JpaRepository<Token, Long>{

    @Query( """
        select t from Token t inner join Employee e 
        on t.employee.id = e.id
        where t.employee.id = :employeeId AND t.loggedOut = false
            
            """)
    List<Token> findAllTokensByUser(Long employeeId);

    Optional<Token> findByToken(String token);

}
//               select t from Token t inner join User u on t.user.id = u.id
//               where t.user.id = :userId and t.loggedOut = false