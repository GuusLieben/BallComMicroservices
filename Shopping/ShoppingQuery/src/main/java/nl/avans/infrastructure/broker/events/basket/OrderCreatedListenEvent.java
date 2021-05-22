package nl.avans.infrastructure.broker.events.basket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Basket;
import nl.avans.infrastructure.broker.events.ListenEvent;
import nl.avans.infrastructure.repository.BasketRepository;

import java.util.ArrayList;
import java.util.UUID;

@RequiredArgsConstructor
public class OrderCreatedListenEvent implements ListenEvent {
    private final BasketRepository basketRepository;

    @JsonProperty
    private UUID customerId;

    @Override
    public void execute() {
        Basket basket = basketRepository.get(customerId);
        basket.setProducts(new ArrayList<>());
        basketRepository.update(basket);
    }
}
