package nl.avans.infrastructure.broker.events.basket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Basket;
import nl.avans.infrastructure.broker.events.ListenEvent;
import nl.avans.infrastructure.repository.BasketRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class BasketItemRemovedListenEvent implements ListenEvent {
    private final BasketRepository basketRepository;

    @JsonProperty
    private UUID customerId;

    @JsonProperty
    private UUID productId;

    @Override
    public void execute() {
        Basket basket = basketRepository.get(customerId);
        for(int index = 0; index < basket.getProducts().size(); index++) {
            if (basket.getProducts().get(index).getProduct().getProductId().equals(productId)) {
                basket.getProducts().remove(index);
                break;
            }
        }
        basketRepository.update(basket);
    }
}
