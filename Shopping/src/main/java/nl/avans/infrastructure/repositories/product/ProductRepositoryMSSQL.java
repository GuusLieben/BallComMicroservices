package nl.avans.infrastructure.repositories.product;

import nl.avans.domain.models.Product;
import nl.avans.infrastructure.repositories.SetupConnection;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class ProductRepositoryMSSQL implements ProductRepository {

    @Override
    public ArrayList<Product> getAll() {
        ArrayList<Product> products = new ArrayList();
        try {
            Connection connection = SetupConnection.connect();

            String sql = "SELECT * FROM product";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet dbResult = statement.executeQuery();
            while(dbResult.next()) {
                Product product = new Product(
                        UUID.fromString(dbResult.getString("id")),
                        dbResult.getString("name"),
                        dbResult.getString("description"),
                        dbResult.getDouble("price"),
                        dbResult.getInt("amount"),
                        dbResult.getString("supplier"),
                        dbResult.getString("brand"),
                        dbResult.getInt("details_viewed")
                );
                product.setName(dbResult.getString("name"));
                product.setDescription(dbResult.getString("description"));
                product.setPrice(dbResult.getDouble("price"));
                product.setAmount(dbResult.getInt("amount"));
                product.setSupplier(dbResult.getString("supplier"));
                product.setBrand(dbResult.getString("brand"));

                products.add(product);
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public Product getById(UUID id) {
        Product product = new Product();
        try {
            Connection connection = SetupConnection.connect();

            String sql = "SELECT * FROM product WHERE id = (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id.toString());
            ResultSet dbResult = statement.executeQuery();
            while(dbResult.next()) {
                product.setName(dbResult.getString("name"));
                product.setDescription(dbResult.getString("description"));
                product.setPrice(dbResult.getDouble("price"));
                product.setAmount(dbResult.getInt("amount"));
                product.setSupplier(dbResult.getString("supplier"));
                product.setBrand(dbResult.getString("brand"));

                return product;
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public Product create(Product product) {
        return null;
    }

    @Override
    public Product update(Product product) {
        return null;
    }

    @Override
    public void delete(UUID productId) {

    }
}
