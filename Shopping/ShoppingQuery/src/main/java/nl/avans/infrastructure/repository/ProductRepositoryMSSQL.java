package nl.avans.infrastructure.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Product;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductRepositoryMSSQL implements ProductRepository {
    private final ConnectionDB connectionDB;
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public ArrayList<Product> get() {
        ArrayList<Product> products = new ArrayList();
        try {
            Connection connection = connectionDB.connect();
            String sql = "SELECT data FROM product;";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet dbResult = statement.executeQuery();
            while(dbResult.next()) {
                String json = dbResult.getString("data");
                Product product = mapper.readValue(json, Product.class);
                products.add(product);
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
        return products;
    }

    @Override
    public Product getById(UUID productId) {
        Product product = new Product();
        try {
            Connection connection = connectionDB.connect();
            String sql = "SELECT data FROM product WHERE productId = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, productId.toString());
            ResultSet dbResult = statement.executeQuery();
            while(dbResult.next()) {
                String json = dbResult.getString("data");
                product = mapper.readValue(json, Product.class);
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
        return product;
    }

    @Override
    public void create(Product product) {
        try {
            Connection connection = connectionDB.connect();

            String sqlAppend = "INSERT INTO product ([productId], [data]) " +
                    "VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(sqlAppend);
            statement.setString(1, product.getProductId().toString());
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            statement.setString(2, this.mapper.writeValueAsString(product));
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Product product) {
        try {
            Connection connection = connectionDB.connect();

            String sqlAppend = "UPDATE product SET data = ? WHERE productId = ?;";
            PreparedStatement statement = connection.prepareStatement(sqlAppend);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            statement.setString(1, this.mapper.writeValueAsString(product));
            statement.setString(2, product.getProductId().toString());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
