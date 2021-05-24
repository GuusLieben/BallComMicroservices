package nl.avans.infrastructure.api;

import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Product;
import nl.avans.infrastructure.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
public class ProductController {
    private final ProductRepository productRepository;

    @GetMapping("/products")
    public ArrayList<Product> get() {
        return productRepository.get();
    }

    @GetMapping("/products/{productId}")
    public Product get(@PathVariable("productId") UUID productId) {
        return productRepository.getById(productId);
    }
}
