package nl.avans.infrastructure.repository;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class ConnectionDBMSSQL implements ConnectionDB {
    public Connection connect() {
        Connection connection = null;

        String url = "jdbc:sqlserver://localhost;databaseName=shopping-writedb";
        String username = "sa";
        String password = "password";

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to SQL server");
        } catch (SQLException e) {
            System.out.println("Connection while connecting");
            e.printStackTrace();
        }
        return connection;
    }
}
