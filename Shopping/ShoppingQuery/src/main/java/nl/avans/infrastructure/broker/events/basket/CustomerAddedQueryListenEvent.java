package nl.avans.infrastructure.broker.events.basket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Basket;
import nl.avans.domain.models.BasketItem;
import nl.avans.domain.models.Customer;
import nl.avans.infrastructure.broker.events.ListenEvent;
import nl.avans.infrastructure.repository.BasketRepository;
import nl.avans.infrastructure.repository.ProductRepository;

@RequiredArgsConstructor
public class CustomerAddedQueryListenEvent implements ListenEvent {
    @JsonProperty
    private UUID guid;

    @Override
    public void execute(ProductRepository productRepository, BasketRepository basketRepository) {
        Customer customer = new Customer();
        customer.setCustomerId(guid);
        Basket basket = new Basket();
        basket.setProducts(new ArrayList<BasketItem>());
        basket.setCustomer(customer);
        basketRepository.create(basket);
    }
}
