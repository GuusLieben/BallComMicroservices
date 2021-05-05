package nl.avans.infrastructure.repositories.product;

import nl.avans.domain.models.Product;

import java.util.ArrayList;
import java.util.UUID;

public interface ProductRepository {
    ArrayList<Product> getAll();
    Product getById(UUID id);
    Product create(Product product);
    Product update(Product product);
    void delete(UUID productId);
}
