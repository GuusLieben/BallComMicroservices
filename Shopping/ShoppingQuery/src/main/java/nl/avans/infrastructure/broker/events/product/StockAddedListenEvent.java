package nl.avans.infrastructure.broker.events.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Product;
import nl.avans.infrastructure.broker.events.ListenEvent;
import nl.avans.infrastructure.repository.ProductRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class StockAddedListenEvent implements ListenEvent {
    private final ProductRepository productRepository;

    @JsonProperty
    private UUID productId;

    @JsonProperty
    private int amount;

    @Override
    public void execute() {
        Product product = productRepository.getById(productId);
        product.setAmount(product.getAmount() + amount);
        productRepository.update(product);
    }
}
