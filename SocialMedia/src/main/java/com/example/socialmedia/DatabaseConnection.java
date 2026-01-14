package com.example.socialmedia;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Database configurations
    private static final String URL = "jdbc:mysql://localhost:3306/social_media";
    private static final String USER = "root"; // Your MySQL username (usually root)
    private static final String PASSWORD = "root"; // CHANGE THIS to your MySQL password

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // This loads the driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // This attempts to connect
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found! Check your pom.xml");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        return connection;
    }
}