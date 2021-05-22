package nl.avans.infrastructure.repositories.write_db;

import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Product;
import nl.avans.infrastructure.repositories.ConnectionDB;
import nl.avans.infrastructure.repositories.interfaces.ProductRepository;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductRepositoryWriteDBMSSQL implements ProductRepository {
    private final ConnectionDB connectionDB;

    @Override
    public ArrayList<Product> getAll() {
        ArrayList<Product> products = new ArrayList();
        try {
            Connection connection = connectionDB.connect();
            String sql = "SELECT * FROM product";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet dbResult = statement.executeQuery();
            while(dbResult.next()) {
//                Product product = new Product(
//                        UUID.fromString(dbResult.getString("id")),
//                        dbResult.getString("name"),
//                        dbResult.getString("description"),
//                        dbResult.getDouble("price"),
//                        dbResult.getInt("amount"),
//                        dbResult.getString("supplier"),
//                        dbResult.getString("brand"),
//                        dbResult.getInt("details_viewed")
//                );
//                product.setName(dbResult.getString("name"));
//                product.setDescription(dbResult.getString("description"));
//                product.setPrice(dbResult.getDouble("price"));
//                product.setAmount(dbResult.getInt("amount"));
//                product.setSupplier(dbResult.getString("supplier"));
//                product.setBrand(dbResult.getString("brand"));
//
//                products.add(product);
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
            Connection connection = connectionDB.connect();

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
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public void create(Product product) {
        try {
            Connection connection = connectionDB.connect();

            String sqlAppend = "INSERT INTO product ([id], [event], [name], [description], [price], [amount], [supplier], [brand], [details_viewed]) " +
                    "VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement statementAppend = connection.prepareStatement(sqlAppend);
            statementAppend.setString(1, product.getId().toString());
            statementAppend.setString(2, product.getEvent());
            statementAppend.setString(3, product.getName());
            statementAppend.setString(4, product.getDescription());
            statementAppend.setDouble(5, product.getPrice());
            statementAppend.setInt(6, product.getAmount());
            statementAppend.setString(7, product.getSupplier());
            statementAppend.setString(8, product.getBrand());
            statementAppend.setInt(9, product.getDetailsViewed());
            statementAppend.execute();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }

    @Override
    public void update(Product product) {
        throw(new UnsupportedOperationException("Update is not supported with event sourcing. Please use create."));
    }

    @Override
    public void delete(UUID productId) {
        throw(new UnsupportedOperationException("Delete is not supported with event sourcing. Please use create."));
    }

    @Override
    public void updateStock(UUID productId, int amount) {
        throw(new UnsupportedOperationException("Update stock is not supported with event sourcing. Please use create."));
    }
}
