package nl.avans.infrastructure.repositories.read_db;

import nl.avans.infrastructure.repositories.ConnectionDB;
import nl.avans.infrastructure.repositories.RepositoryAbstractFactory;
import nl.avans.infrastructure.repositories.interfaces.BasketRepository;
import nl.avans.infrastructure.repositories.interfaces.CustomerRepository;
import nl.avans.infrastructure.repositories.interfaces.ProductRepository;

public class RepositoryReadDBFactory implements RepositoryAbstractFactory {
    private ConnectionDB connectionDB = new SetupConnectionReadDB();

    @Override
    public CustomerRepository createCustomerRepository() {
        return null;
    }

    @Override
    public ProductRepository createProductRepository() {
        return null;
    }

    @Override
    public BasketRepository createBasketRepository() {
        return new BasketRepositoryReadDBMSSQL(connectionDB);
    }
}
