package nl.avans.infrastructure.api.controllers;

import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.message.ReturnObject;
import nl.avans.domain.services.handler.ProductHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("api/products/{productId}/detailsViewed")
    public ResponseEntity<String> detailsViewed(@PathVariable("productId") UUID productId) {
        ReturnObject returnObject = productHandler.upDetailsViewed(productId);
        if (returnObject.getError() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnObject.getError());
        }
        return ResponseEntity.ok("Success");
    }
}
