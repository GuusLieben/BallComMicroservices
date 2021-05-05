package nl.avans.infrastructure.api.controllers;

import nl.avans.domain.models.Basket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class BasketController {
    @Value("${ball.rabbitmq.queue}")
    private String iii;

    @GetMapping("/basket")
    public String getAll(Basket basket) {
        return iii;
    }

    @PutMapping("/basket")
    public Basket put(Basket basket) {
        return basket;
    }

    @PostMapping("/basket/checkout")
    public Basket checkout(Basket basket) {
        return basket;
    }
}
