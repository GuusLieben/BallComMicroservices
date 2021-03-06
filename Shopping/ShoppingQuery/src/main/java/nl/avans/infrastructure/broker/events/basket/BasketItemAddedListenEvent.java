package nl.avans.infrastructure.broker.events.basket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Basket;
import nl.avans.domain.models.BasketItem;
import nl.avans.domain.models.Product;
import nl.avans.infrastructure.broker.events.ListenEvent;
import nl.avans.infrastructure.repository.BasketRepository;
import nl.avans.infrastructure.repository.ProductRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class BasketItemAddedListenEvent implements ListenEvent {
    @JsonProperty
    private UUID customerId;

    @JsonProperty
    private UUID productId;

    @JsonProperty
    private int amount;

    @Override
    public void execute(ProductRepository productRepository, BasketRepository basketRepository) {
        Basket basket = basketRepository.get(customerId);
        Product product = productRepository.getById(productId);
        if(basket != null && basket.getProducts() != null && product != null) {
            BasketItem basketItem = new BasketItem();
            basketItem.setAmount(amount);
            basketItem.setProduct(product);
            basket.getProducts().add(basketItem);
            basketRepository.update(basket);
        }
    }
}
