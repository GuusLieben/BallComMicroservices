package nl.avans.infrastructure.repositories;

import nl.avans.infrastructure.repositories.interfaces.BasketRepository;
import nl.avans.infrastructure.repositories.interfaces.CustomerRepository;
import nl.avans.infrastructure.repositories.interfaces.ProductRepository;

public interface RepositoryAbstractFactory {
    CustomerRepository createCustomerRepository();
    ProductRepository createProductRepository();
    BasketRepository createBasketRepository();
}
