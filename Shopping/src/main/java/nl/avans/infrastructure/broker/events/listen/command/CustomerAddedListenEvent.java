package nl.avans.infrastructure.broker.events.listen.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import nl.avans.domain.models.Customer;
import nl.avans.infrastructure.broker.events.listen.ListenEvent;
import nl.avans.infrastructure.repositories.RepositoryAbstractFactory;
import nl.avans.infrastructure.repositories.interfaces.CustomerRepository;

import java.util.UUID;

@Data
public class CustomerAddedListenEvent implements ListenEvent {
    @JsonProperty
    private UUID customerId;

    @Override
    public void execute(RepositoryAbstractFactory repositoryAbstractFactory) {
        CustomerRepository customerRepository = repositoryAbstractFactory.createCustomerRepository();
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setEvent("Created");
        customerRepository.create(customer);
    }
}
