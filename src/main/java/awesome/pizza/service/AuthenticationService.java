package awesome.pizza.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import awesome.pizza.response.AuthenticationResponse;
import awesome.pizza.model.User;
import awesome.pizza.model.Employee;
import awesome.pizza.model.Role;
import awesome.pizza.model.Token;
import awesome.pizza.repository.UserRepository;
import awesome.pizza.repository.TokenRepository;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationService(
                UserRepository userRepository, PasswordEncoder passwordEncoder, 
                JwtService jwtService, AuthenticationManager authenticationManager, 
                TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    //register user
    public AuthenticationResponse register(User request) {

        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, "User already exist");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole());

        user = userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        //save generated Token
        saveUserToken(user, jwtToken);


        return new AuthenticationResponse(jwtToken, user.getRole() + ":" + user.getUsername() + " registration was successful");

    }

    public AuthenticationResponse registerEmploy(Employee request) {

        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, "already exist");
        }
        
        Employee employee = new Employee();
        employee.setUsername(request.getUsername());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setRole(Role.EMPLOYEE);

        employee = userRepository.save(employee);

        String jwtToken = jwtService.generateToken(employee);

        //save generated Token
        saveUserToken(employee, jwtToken);


        return new AuthenticationResponse(jwtToken, employee.getRole() + ":" + employee.getUsername() + " registration was successful");

    }
    
    //login user
    public AuthenticationResponse authenticate(User request) {
       authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(), 
                request.getPassword())
            );

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);

        //revoke all token by user
        revokeAllTokenByUser(user);

        //save new token
        saveUserToken(user, jwtToken);
        
        return new AuthenticationResponse(jwtToken, user.getRole()+ ":" + user.getUsername() + " login was successful");
        
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokenListByUser = tokenRepository.findAllTokensByUser(user.getId());
        
        if(!validTokenListByUser.isEmpty()) {
            validTokenListByUser.forEach(token -> {
                token.setLoggedOut(true);
            });
        }   
        tokenRepository.saveAll(validTokenListByUser);    

    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = new Token();
        token.setToken(jwtToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
}
