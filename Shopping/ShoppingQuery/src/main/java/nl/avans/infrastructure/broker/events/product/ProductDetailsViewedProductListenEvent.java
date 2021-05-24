package nl.avans.infrastructure.broker.events.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Product;
import nl.avans.infrastructure.broker.events.ListenEvent;
import nl.avans.infrastructure.repository.BasketRepository;
import nl.avans.infrastructure.repository.ProductRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class ProductDetailsViewedProductListenEvent implements ListenEvent {
    @JsonProperty
    private UUID productId;

    @Override
    public void execute(ProductRepository productRepository, BasketRepository basketRepository) {
        Product product = productRepository.getById(productId);
        product.setDetailsViewed(product.getDetailsViewed() + 1);
        productRepository.update(product);
    }
}
