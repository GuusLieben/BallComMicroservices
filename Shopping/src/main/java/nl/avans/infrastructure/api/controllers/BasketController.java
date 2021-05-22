package nl.avans.infrastructure.api.controllers;

import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Basket;
import nl.avans.domain.models.Order;
import nl.avans.infrastructure.api.models.crud.BasketCRUDViewModel;
import nl.avans.infrastructure.api.models.viewmodels.AddOrDeleteProductToBasketViewModel;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
public class BasketController {

    private final BasketCRUDViewModel basketViewModel;

    @GetMapping("/basket/{id}")
    public Basket get(UUID customerId) {
        return basketViewModel.get(customerId);
    }

    @PostMapping("/basket")
    public AddOrDeleteProductToBasketViewModel post(AddOrDeleteProductToBasketViewModel basketItem) {
        basketViewModel.addProduct(basketItem);
        return basketItem;
    }

    @DeleteMapping("/basket")
    public Basket put(Basket basket) {
        return basket;
    }

    @PostMapping("/basket/checkout")
    public Order checkout(@RequestBody Order order) {
        return order;
    }
}
