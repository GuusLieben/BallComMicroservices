package nl.avans.infrastructure.api.controllers;

import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.models.Order;
import nl.avans.infrastructure.api.models.AddBasketItemViewModel;
import nl.avans.infrastructure.api.models.RemoveBasketIdemViewModel;
import nl.avans.domain.services.handler.BasketHandler;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
public class BasketController {
    private final BasketHandler basketHandler;

    @PostMapping("/basket")
    public AddBasketItemViewModel post(@RequestBody AddBasketItemViewModel viewModel) {
        basketHandler.addBasketItem(viewModel.getCustomerId(), viewModel.getProductId(), viewModel.getAmount());
        return viewModel;
    }

    @DeleteMapping("/basket")
    public RemoveBasketIdemViewModel delete(RemoveBasketIdemViewModel viewModel) {
        basketHandler.removeBasketItem(viewModel.getCustomerId(), viewModel.getProductId());
        return viewModel;
    }

    @PostMapping("/basket/checkout")
    public Order checkout(@RequestBody Order order) {
        basketHandler.checkout(order);
        return order;
    }
}
