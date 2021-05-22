package nl.avans.infrastructure.repositories.interfaces;

import nl.avans.domain.models.Product;

import java.util.ArrayList;
import java.util.UUID;

public interface ProductRepository {
    ArrayList<Product> getAll();
    Product getById(UUID id);
    void create(Product product);
    void update(Product product);
    void delete(UUID productId);
    void updateStock(UUID productId, int amount);
}
