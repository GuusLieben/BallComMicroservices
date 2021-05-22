package nl.avans.infrastructure.broker.events.listen.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.avans.domain.models.Basket;
import nl.avans.domain.models.Customer;
import nl.avans.infrastructure.broker.events.listen.ListenEvent;
import nl.avans.infrastructure.repositories.RepositoryAbstractFactory;
import nl.avans.infrastructure.repositories.interfaces.BasketRepository;

import java.util.UUID;

public class CustomerAddedQueryListenEvent implements ListenEvent {
    @JsonProperty
    private UUID customerId;

    public void execute(RepositoryAbstractFactory repositoryAbstractFactory) {
        BasketRepository basketRepository = repositoryAbstractFactory.createBasketRepository();
        Customer customer = new Customer();
        customer.setId(customerId);
        Basket basket = new Basket();
        basket.setCustomer(customer);
        basketRepository.create(basket);
    }
}
