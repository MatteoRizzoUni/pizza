package awesome.pizza.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import awesome.pizza.response.AuthenticationResponse;
import awesome.pizza.model.Employee;
import awesome.pizza.model.Token;
import awesome.pizza.repository.EmployeeRepository;
import awesome.pizza.repository.TokenRepository;

@Service
public class AuthenticationService {
    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationService(EmployeeRepository repository, 
            PasswordEncoder passwordEncoder, JwtService jwtService, 
            AuthenticationManager authenticationManager,
            TokenRepository tokenRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    //register user
    public AuthenticationResponse regiser(Employee request) {

        if(repository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, "User already exist");
        }
        
        Employee employee = new Employee();
        employee.setUsername(request.getUsername());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setRole(request.getRole());

        employee = repository.save(employee);

        String jwtToken = jwtService.generateToken(employee);

        //save generated Token
        saveEmployeeToken(employee, jwtToken);


        return new AuthenticationResponse(jwtToken, employee.getRole() + ":" + employee.getUsername() + " registration was successful");

    }

    
    //login user
    public AuthenticationResponse authenticate(Employee request) {
       authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(), 
                request.getPassword())
            );

        Employee employee = repository.findByUsername(request.getUsername()).orElseThrow();
        String jwtToken = jwtService.generateToken(employee);

        //revoke all token by user
        revokeAllTokenByEmployee(employee);

        //save new token
        saveEmployeeToken(employee, jwtToken);
        
        return new AuthenticationResponse(jwtToken, employee.getRole()+ ":" + employee.getUsername() + " login was successful");
        
    }

    private void revokeAllTokenByEmployee(Employee employee) {
        List<Token> validTokenListByUser = tokenRepository.findAllTokensByUser(employee.getId());
        
        if(!validTokenListByUser.isEmpty()) {
            validTokenListByUser.forEach(token -> {
                token.setLoggedOut(true);
            });
        }   
        tokenRepository.saveAll(validTokenListByUser);    

    }

    private void saveEmployeeToken(Employee employee, String jwtToken) {
        Token token = new Token();
        token.setToken(jwtToken);
        token.setLoggedOut(false);
        token.setEmployee(employee);
        tokenRepository.save(token);
    }
}
