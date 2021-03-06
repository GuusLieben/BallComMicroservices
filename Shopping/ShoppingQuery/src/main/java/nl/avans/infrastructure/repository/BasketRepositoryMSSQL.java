package nl.avans.infrastructure.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Basket;

@Service
@RequiredArgsConstructor
public class BasketRepositoryMSSQL implements BasketRepository {
    private final ConnectionDB connectionDB;
    private ObjectMapper mapper = new ObjectMapper();

    {
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    @Override
    public Basket get(UUID customerId) {
        Basket basket = new Basket();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
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
            System.out.println(e.getMessage());
            System.out.println("Error get BasketRepositoryMSSQL");
            e.printStackTrace();
        } catch (JsonMappingException e) {
            System.out.println(e.getMessage());
            System.out.println("Error get BasketRepositoryMSSQL");
            e.printStackTrace();
        } catch (JsonProcessingException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Error get BasketRepositoryMSSQL");
            e.printStackTrace();
        }
        return basket;
    }

    @Override
    public void create(Basket basket) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection connection = connectionDB.connect();

            String sqlAppend = "INSERT INTO basket ([customerId], [data]) " +
                    "VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(sqlAppend);
            statement.setString(1, basket.getCustomer().getCustomerId().toString());
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            statement.setString(2, this.mapper.writeValueAsString(basket));
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Error create BasketRepositoryMSSQL");
            e.printStackTrace();
        } catch (JsonProcessingException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Error create BasketRepositoryMSSQL");
            e.printStackTrace();
        }
    }

    @Override
    public void update(Basket basket) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection connection = connectionDB.connect();

            String sqlAppend = "UPDATE basket SET data = ? WHERE customerId = ?;";
            PreparedStatement statement = connection.prepareStatement(sqlAppend);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            statement.setString(1, this.mapper.writeValueAsString(basket));
            statement.setString(2, basket.getCustomer().getCustomerId().toString());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Error update BasketRepositoryMSSQL");
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            System.out.println("Error update BasketRepositoryMSSQL");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Error update BasketRepositoryMSSQL");
            e.printStackTrace();
        }
    }
}
