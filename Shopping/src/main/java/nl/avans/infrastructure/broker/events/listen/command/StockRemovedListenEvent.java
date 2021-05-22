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
public class StockRemovedListenEvent implements ListenEvent {
    @JsonProperty
    private UUID productId;

    @JsonProperty
    private int amount;

    @Override
    public void execute(RepositoryAbstractFactory repositoryAbstractFactory) {
        ProductRepository repository = repositoryAbstractFactory.createProductRepository();
        Product product = new Product();
        product.setId(productId);
        product.setAmount(0 - amount);
        product.setEvent("Removed " + amount + " from stock");
        repository.create(product);
    }
}
