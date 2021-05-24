package nl.avans.infrastructure.repository;

import nl.avans.domain.models.Basket;

import java.util.UUID;

public interface BasketRepository {
    Basket get(UUID customerId);
    void create(Basket basket);
    void update(Basket basket);
}
