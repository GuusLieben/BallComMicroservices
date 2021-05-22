package nl.avans.infrastructure.repositories.write_db;

import lombok.AllArgsConstructor;
import nl.avans.domain.models.Basket;
import nl.avans.domain.models.Product;
import nl.avans.infrastructure.repositories.ConnectionDB;
import nl.avans.infrastructure.repositories.interfaces.BasketRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class BasketRepositoryWriteDBMSSQL implements BasketRepository {
    private final ConnectionDB connectionDB;

    @Override
    public Basket get(UUID customerId) {
        return null;
    }

    @Override
    public void create(Basket basket) {

    }

    @Override
    public void update(Basket basket) {

    }

    @Override
    public void delete(UUID customerId) {

    }
}
