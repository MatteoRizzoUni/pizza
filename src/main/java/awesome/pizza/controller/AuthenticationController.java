package awesome.pizza.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import awesome.pizza.response.AuthenticationResponse;
import awesome.pizza.model.Employee;
import awesome.pizza.model.User;
import awesome.pizza.service.AuthenticationService;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User registerRequest) {
        
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping("/register-employee")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody Employee registerRequest) {
        
        return ResponseEntity.ok(authenticationService.registerEmploy(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User loginRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequest));
    }
}
