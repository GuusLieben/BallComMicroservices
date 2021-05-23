package nl.avans.infrastructure.api.controllers;

import lombok.RequiredArgsConstructor;
import nl.avans.domain.services.handler.ProductHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
public class ProductController {
    private final ProductHandler productHandler;

    @PostMapping("/product/{productId}/detailsViewed")
    public UUID detailsViewed(@PathVariable("productId") UUID productId) {
        productHandler.upDetailsViewed(productId);
        return productId;
    }
}
