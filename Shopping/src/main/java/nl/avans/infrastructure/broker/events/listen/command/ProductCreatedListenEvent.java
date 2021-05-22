package nl.avans.infrastructure.broker.events.listen.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Product;
import nl.avans.infrastructure.broker.events.listen.ListenEvent;
import nl.avans.infrastructure.repositories.RepositoryAbstractFactory;
import nl.avans.infrastructure.repositories.interfaces.ProductRepository;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class ProductCreatedListenEvent implements ListenEvent {
    @JsonProperty
    private UUID productId;

    @JsonProperty
    private String name;

    @JsonProperty
    private String description;

    @JsonProperty
    private double price;

    @JsonProperty
    private int amount;

    @JsonProperty
    private String supplier;

    @JsonProperty
    private String brandName;

    @Override
    public void execute(RepositoryAbstractFactory repositoryAbstractFactory) {
        ProductRepository repository = repositoryAbstractFactory.createProductRepository();
        Product product = new Product();
        product.setProductId(productId);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setAmount(amount);
        product.setSupplier(supplier);
        product.setBrand(brandName);
        product.setDetailsViewed(0);
        product.setEvent("Created");
        repository.create(product);
    }
}
