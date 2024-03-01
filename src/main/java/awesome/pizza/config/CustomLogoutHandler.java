package awesome.pizza.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import awesome.pizza.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutHandler implements LogoutHandler{

    private final TokenRepository tokenRepository;

    public CustomLogoutHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, 
                        HttpServletResponse response, 
                        Authentication authentication) {

        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        //7: "Bearer " is 7 characters long with the space
        String token = authHeader.substring(7);

        //get stored token from the database
        tokenRepository.findByToken(token).ifPresent(t -> {
            t.setLoggedOut(true);
            tokenRepository.save(t);
        });
    }

}
