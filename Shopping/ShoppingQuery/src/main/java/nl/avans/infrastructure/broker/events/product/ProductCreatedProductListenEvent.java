package nl.avans.infrastructure.broker.events.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Product;
import nl.avans.infrastructure.broker.events.ListenEvent;
import nl.avans.infrastructure.repository.BasketRepository;
import nl.avans.infrastructure.repository.ProductRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class ProductCreatedProductListenEvent implements ListenEvent {
    @JsonProperty
    private UUID productId;

    @JsonProperty
    private String name;

    @JsonProperty
    private int amount;

    @JsonProperty
    private String description;

    @JsonProperty
    private double price;

    @JsonProperty
    private double weight;

    @JsonProperty
    private String brandName;

    @JsonProperty
    private String supplierName;

    @Override
    public void execute(ProductRepository productRepository, BasketRepository basketRepository) {
        Product product = new Product();
        product.setProductId(productId);
        product.setName(name);
        product.setAmount(amount);
        product.setDescription(description);
        product.setPrice(price);
        product.setWeight(weight);
        product.setBrandName(brandName);
        product.setSupplierName(supplierName);
        product.setDetailsViewed(0);

        productRepository.create(product);
    }
}
