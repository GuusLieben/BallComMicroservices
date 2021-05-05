package nl.avans.infrastructure.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SetupConnection {
    public static Connection connect() {
        Connection connection = null;

        String url = "jdbc:sqlserver://localhost;databaseName=shopping";
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
