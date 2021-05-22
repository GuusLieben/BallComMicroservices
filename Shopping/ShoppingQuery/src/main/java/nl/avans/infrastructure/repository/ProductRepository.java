package nl.avans.infrastructure.repository;

import nl.avans.domain.models.Product;

import java.util.ArrayList;
import java.util.UUID;

public interface ProductRepository {
    ArrayList<Product> get();
    Product getById(UUID productId);
    void create(Product product);
    void update(Product product);
}
