package awesome.pizza.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import awesome.pizza.model.Token;



public interface TokenRepository extends JpaRepository<Token, Long>{

    @Query( """
        select t from Token t inner join User e 
        on t.user.id = e.id
        where t.user.id = :userId AND t.loggedOut = false
            
            """)
    List<Token> findAllTokensByUser(Long userId);

    Optional<Token> findByToken(String token);

}
//               select t from Token t inner join User u on t.user.id = u.id
//               where t.user.id = :userId and t.loggedOut = false
