package nl.avans.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.events.product.ProductEventModel;
import nl.avans.domain.services.repository.ProductRepository;
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
    public ArrayList<ProductEventModel> getById(UUID id) {
        ArrayList<ProductEventModel> ProductEventModels = new ArrayList<>();
        try {
            Connection connection = connectionDB.connect();

            String sql = "SELECT productId, event, data FROM product WHERE productId = (?) ORDER BY [date] DESC;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id.toString());
            ResultSet dbResult = statement.executeQuery();
            while(dbResult.next()) {
                ProductEventModel productEventModel = new ProductEventModel();
                productEventModel.setProductId(UUID.fromString(dbResult.getString("productId")));
                productEventModel.setEvent(dbResult.getString("event"));
                productEventModel.setData(dbResult.getString("data"));
                ProductEventModels.add(productEventModel);
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
        return ProductEventModels;
    }

    @Override
    public void create(ProductEventModel eventModel) {
        try {
            Connection connection = connectionDB.connect();

            String sqlAppend = "INSERT INTO product ([productId], [event], [data]) " +
                    "VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sqlAppend);
            statement.setString(1, eventModel.getProductId().toString());
            statement.setString(2, eventModel.getEvent());
            statement.setString(3, eventModel.getData());
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}
