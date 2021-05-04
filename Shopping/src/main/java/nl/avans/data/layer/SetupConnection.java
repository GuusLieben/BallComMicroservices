package nl.avans.data.layer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SetupConnection {
    public void connect() {
        String url = "jdbc:sqlserver://localhost;databaseName=shopping";
        String username = "sa";
        String password = "password";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to SQL server");
        } catch (SQLException e) {
            System.out.println("Connection while connecting");
            e.printStackTrace();
        }
    }
}
