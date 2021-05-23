package nl.avans.domain.services.repository;

import nl.avans.domain.models.events.basket.BasketEventModel;
import java.util.ArrayList;
import java.util.UUID;

public interface BasketRepository {
    ArrayList<BasketEventModel> getById(UUID customerId);
    void create(BasketEventModel basketEventModel);
}
