package nl.avans.infrastructure.broker.events.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Product;
import nl.avans.infrastructure.broker.events.ListenEvent;
import nl.avans.infrastructure.repository.ProductRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class ProductDetailsViewedListenEvent implements ListenEvent {
    private final ProductRepository productRepository;

    @JsonProperty
    private UUID productId;

    @Override
    public void execute() {
        Product product = productRepository.getById(productId);
        product.setDetailsViewed(product.getDetailsViewed() + 1);
        productRepository.update(product);
    }
}
