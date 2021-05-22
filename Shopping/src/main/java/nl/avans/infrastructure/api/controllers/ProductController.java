package nl.avans.infrastructure.api.controllers;

import nl.avans.domain.models.Product;
import nl.avans.infrastructure.repositories.interfaces.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping("/product")
    public ArrayList<Product> getAll() {
        return productRepository.getAll();
    }

    @GetMapping("/product/{id}")
    public Product getById(UUID id) {
        return productRepository.getById(id);
    }

    @PostMapping("/product/detailsViewed")
    public Product detailsViewed(UUID id) {
        return productRepository.getById(id);
    }
}
