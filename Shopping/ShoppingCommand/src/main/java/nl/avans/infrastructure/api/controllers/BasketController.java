package nl.avans.infrastructure.api.controllers;

import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.message.ReturnObject;
import nl.avans.domain.models.models.Order;
import nl.avans.infrastructure.api.models.AddBasketItemViewModel;
import nl.avans.infrastructure.api.models.RemoveBasketIdemViewModel;
import nl.avans.domain.services.handler.BasketHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
public class BasketController {
    private final BasketHandler basketHandler;

    @PostMapping("api/baskets")
    public ResponseEntity<String> post(@RequestBody AddBasketItemViewModel viewModel) {
        ReturnObject returnObject = basketHandler.addBasketItem(viewModel.getCustomerId(), viewModel.getProductId(), viewModel.getAmount());
        if (returnObject.getError() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnObject.getError());
        }
        return ResponseEntity.ok("Success");
    }

    @DeleteMapping("api/baskets")
    public ResponseEntity<String> delete(@RequestBody RemoveBasketIdemViewModel viewModel) {
        ReturnObject returnObject = basketHandler.removeBasketItem(viewModel.getCustomerId(), viewModel.getProductId());
        if (returnObject.getError() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnObject.getError());
        }
        return ResponseEntity.ok("Success");
    }

    @PostMapping("api/baskets/checkout")
    public ResponseEntity<String> checkout(@RequestBody Order order) {
        ReturnObject returnObject = basketHandler.checkout(order);
        if (returnObject.getError() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnObject.getError());
        }
        return ResponseEntity.ok("Success");
    }
}
