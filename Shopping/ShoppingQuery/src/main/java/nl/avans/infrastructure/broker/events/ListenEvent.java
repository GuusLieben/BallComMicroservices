package nl.avans.infrastructure.broker.events;

import nl.avans.infrastructure.repository.BasketRepository;
import nl.avans.infrastructure.repository.ProductRepository;

import java.io.Serializable;

public interface ListenEvent extends Serializable {
    void execute(ProductRepository productRepository, BasketRepository basketRepository);
}
