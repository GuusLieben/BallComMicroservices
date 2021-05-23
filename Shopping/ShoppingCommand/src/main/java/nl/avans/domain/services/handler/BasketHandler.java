package nl.avans.domain.services.handler;

import nl.avans.domain.models.message.ReturnObject;
import nl.avans.domain.models.models.Basket;
import nl.avans.domain.models.models.BasketItem;
import nl.avans.domain.models.models.Order;

import java.util.UUID;

public interface BasketHandler {
    ReturnObject<Basket> addBasketItem(UUID customerId, UUID productId, int amount);
    ReturnObject<BasketItem> removeBasketItem(UUID customerId, UUID productId);
    ReturnObject<Order> checkout(Order order);
}
