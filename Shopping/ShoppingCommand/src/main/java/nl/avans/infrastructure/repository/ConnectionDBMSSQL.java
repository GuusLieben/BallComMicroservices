package nl.avans.infrastructure.repository;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class ConnectionDBMSSQL implements ConnectionDB {
    public Connection connect() {
        Connection connection = null;

        String url = "jdbc:sqlserver://sqlserver;databaseName=shopping-writedb";
        String username = "sa";
        String password = "8jkGh47hnDw89Haq8LN2";

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to SQL server");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Connection while connecting");
            e.printStackTrace();
        }
        return connection;
    }
}
