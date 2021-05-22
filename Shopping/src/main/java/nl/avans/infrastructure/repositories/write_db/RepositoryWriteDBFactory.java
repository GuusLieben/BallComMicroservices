package nl.avans.infrastructure.repositories.write_db;

import nl.avans.infrastructure.repositories.ConnectionDB;
import nl.avans.infrastructure.repositories.RepositoryAbstractFactory;
import nl.avans.infrastructure.repositories.interfaces.BasketRepository;
import nl.avans.infrastructure.repositories.interfaces.CustomerRepository;
import nl.avans.infrastructure.repositories.interfaces.ProductRepository;

public class RepositoryWriteDBFactory implements RepositoryAbstractFactory {
    private ConnectionDB connectionDB = new SetupConnectionWriteDB();

    @Override
    public CustomerRepository createCustomerRepository() {
        return new CustomerRepositoryWriteDBMSSQL(connectionDB);
    }

    @Override
    public ProductRepository createProductRepository() {
        return new ProductRepositoryWriteDBMSSQL(connectionDB);
    }

    @Override
    public BasketRepository createBasketRepository() {
        return new BasketRepositoryWriteDBMSSQL(connectionDB);
    }
}
