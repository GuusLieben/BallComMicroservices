package nl.avans.domain.services.handler;

import nl.avans.domain.models.models.Order;

import java.util.UUID;

public interface BasketHandler {
    void addBasketItem(UUID customerId, UUID productId, int amount);
    void removeBasketItem(UUID customerId, UUID productId);
    void checkout(Order order);
}
