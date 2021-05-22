package nl.avans.infrastructure.api;

import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Basket;
import nl.avans.infrastructure.repository.BasketRepository;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
public class BasketController {
    private final BasketRepository basketRepository;

    @GetMapping("/basket/{customerId}")
    public Basket get(@PathVariable("customerId") UUID customerId) {
        return basketRepository.get(customerId);
    }
}
