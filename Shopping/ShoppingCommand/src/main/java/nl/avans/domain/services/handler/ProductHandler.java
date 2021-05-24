package nl.avans.domain.services.handler;

import nl.avans.domain.models.message.ReturnObject;
import nl.avans.domain.models.models.Product;

import java.util.UUID;

public interface ProductHandler {
    ReturnObject<Product> upDetailsViewed(UUID productId);
}
