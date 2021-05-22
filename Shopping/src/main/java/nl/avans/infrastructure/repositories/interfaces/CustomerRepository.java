package nl.avans.infrastructure.repositories.interfaces;

import nl.avans.domain.models.Customer;

public interface CustomerRepository {
    void create(Customer customer);
}
