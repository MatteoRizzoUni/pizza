package awesome.pizza.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demoAll")
    public ResponseEntity<String> demoAll() {
        return ResponseEntity.ok("Hello World to Awesome Pizza!");
    }

    @GetMapping("/demoAdmin")
    public ResponseEntity<String> demoAdmin() {
        return ResponseEntity.ok("Hello Admin to Awesome Pizza!");
    }
}
