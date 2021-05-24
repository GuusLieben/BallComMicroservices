package nl.avans.infrastructure.repository;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.events.basket.BasketEventModel;
import nl.avans.domain.services.repository.BasketRepository;

@Service
@RequiredArgsConstructor
public class BasketRepositoryDBMSSQL implements BasketRepository {
    private final ConnectionDB connectionDB;

    @Override
    public ArrayList<BasketEventModel> getById(UUID customerId) {
        ArrayList<BasketEventModel> basketEventModels = new ArrayList<>();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection connection = connectionDB.connect();

            String sql = "SELECT customerId, event, data FROM basket WHERE customerId = (?) ORDER BY [date] ASC;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customerId.toString());
            ResultSet dbResult = statement.executeQuery();
            while(dbResult.next()) {
                BasketEventModel basketEventModel = new BasketEventModel();
                basketEventModel.setCustomerId(UUID.fromString(dbResult.getString("customerId")));
                basketEventModel.setEvent(dbResult.getString("event"));
                basketEventModel.setData(dbResult.getString("data"));
                basketEventModels.add(basketEventModel);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Error getById BasketRepositoryDBMSSQL");
            e.printStackTrace();
        }
        return basketEventModels;
    }

    @Override
    public void create(BasketEventModel basketEventModel) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection connection = connectionDB.connect();

            String sqlAppend = "INSERT INTO basket ([customerId], [event], [data]) " +
                    "VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sqlAppend);
            statement.setString(1, basketEventModel.getCustomerId().toString());
            statement.setString(2, basketEventModel.getEvent());
            statement.setString(3, basketEventModel.getData());
            statement.execute();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Error create BasketRepositoryDBMSSQL");
            e.printStackTrace();
        }
    }
}
