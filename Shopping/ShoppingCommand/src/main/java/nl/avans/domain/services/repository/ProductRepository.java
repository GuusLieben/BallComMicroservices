package nl.avans.domain.services.repository;

import nl.avans.domain.models.events.product.ProductEventModel;

import java.util.ArrayList;
import java.util.UUID;

public interface ProductRepository {
    ArrayList<ProductEventModel> getById(UUID id);
    void create(ProductEventModel productEventModel);
}
