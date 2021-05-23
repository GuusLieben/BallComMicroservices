package nl.avans.domain.services.aggregate;

import nl.avans.domain.models.events.basket.BasketEventModel;
import nl.avans.domain.models.models.Basket;

import java.util.ArrayList;

public interface AggregateBasket {
    Basket aggregate(ArrayList<BasketEventModel> events);
}
