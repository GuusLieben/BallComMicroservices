package nl.avans.infrastructure.repositories.read_db;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Basket;
import nl.avans.infrastructure.repositories.ConnectionDB;
import nl.avans.infrastructure.repositories.interfaces.BasketRepository;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketRepositoryReadDBMSSQL implements BasketRepository {
    private final ConnectionDB connectionDB;
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Basket get(UUID customerId) {
        Basket basket = new Basket();
        try {
            Connection connection = connectionDB.connect();

            String sql = "SELECT data FROM basket WHERE customerId = (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customerId.toString());
            ResultSet dbResult = statement.executeQuery();
            while(dbResult.next()) {
                String json = dbResult.getString("data");
                basket = mapper.readValue(json, Basket.class);
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return basket;
    }

    @Override
    public void create(Basket basket) {
        try {
            Connection connection = connectionDB.connect();

            String sqlAppend = "INSERT INTO basket ([customerId], [data]) " +
                    "VALUES (?,?)";
            PreparedStatement statementAppend = connection.prepareStatement(sqlAppend);
            statementAppend.setString(1, basket.getCustomer().getId().toString());
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            statementAppend.setString(2, this.mapper.writeValueAsString(basket));
            statementAppend.execute();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Basket basket) {

    }

    @Override
    public void delete(UUID customerId) {

    }
}
