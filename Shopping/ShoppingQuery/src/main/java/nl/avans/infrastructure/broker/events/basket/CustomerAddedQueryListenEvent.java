package nl.avans.infrastructure.broker.events.basket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Basket;
import nl.avans.domain.models.Customer;
import nl.avans.infrastructure.broker.events.ListenEvent;
import nl.avans.infrastructure.repository.BasketRepository;
import nl.avans.infrastructure.repository.ProductRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class CustomerAddedQueryListenEvent implements ListenEvent {
    @JsonProperty
    private UUID customerId;

    @Override
    public void execute(ProductRepository productRepository, BasketRepository basketRepository) {
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        Basket basket = new Basket();
        basket.setCustomer(customer);
        basketRepository.create(basket);
    }
}
