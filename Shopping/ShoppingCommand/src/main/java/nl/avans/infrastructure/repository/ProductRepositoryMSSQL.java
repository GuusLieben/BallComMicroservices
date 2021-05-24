package nl.avans.infrastructure.repository;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.events.product.ProductEventModel;
import nl.avans.domain.services.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductRepositoryMSSQL implements ProductRepository {
    private final ConnectionDB connectionDB;
    private ObjectMapper mapper = new ObjectMapper();

    {
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    @Override
    public ArrayList<ProductEventModel> getById(UUID id) {
        ArrayList<ProductEventModel> ProductEventModels = new ArrayList<>();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection connection = connectionDB.connect();

            String sql = "SELECT productId, event, data FROM product WHERE productId = (?) ORDER BY [date] ASC;";
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
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Error getById ProductRepositoryMSSQL");
            e.printStackTrace();
        }
        return ProductEventModels;
    }

    @Override
    public void create(ProductEventModel eventModel) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection connection = connectionDB.connect();

            String sqlAppend = "INSERT INTO product ([productId], [event], [data]) " +
                    "VALUES (?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sqlAppend);
            statement.setString(1, eventModel.getProductId().toString());
            statement.setString(2, eventModel.getEvent());
            statement.setString(3, eventModel.getData());
            statement.execute();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Error create ProductRepositoryMSSQL");
            e.printStackTrace();
        }
    }
}
