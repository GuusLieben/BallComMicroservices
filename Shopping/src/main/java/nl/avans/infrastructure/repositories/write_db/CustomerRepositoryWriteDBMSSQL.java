package nl.avans.infrastructure.repositories.write_db;

import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Customer;
import nl.avans.infrastructure.repositories.ConnectionDB;
import nl.avans.infrastructure.repositories.interfaces.CustomerRepository;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class CustomerRepositoryWriteDBMSSQL implements CustomerRepository {
    private final ConnectionDB connectionDB;

    @Override
    public void create(Customer customer) {
        try {
            Connection connection = connectionDB.connect();

            String sqlAppend = "INSERT INTO customer ([id], [event]) " +
                    "VALUES (?,?)";
            PreparedStatement statementAppend = connection.prepareStatement(sqlAppend);
            statementAppend.setString(1, customer.getId().toString());
            statementAppend.setString(2, customer.getEvent());
            statementAppend.execute();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}
