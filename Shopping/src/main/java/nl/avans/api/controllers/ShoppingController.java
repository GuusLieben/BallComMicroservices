package nl.avans.api.controllers;

import nl.avans.data.layer.SetupConnection;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShoppingController {

    @RequestMapping("/")
    public String index() {
        SetupConnection s = new SetupConnection();
        s.connect();
        return "Greetings from Spring Boot!";
    }
}
