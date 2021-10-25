package sample.dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Sets up and configures the JDBC driver and connects to the database.*/
public class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    public static Connection connection;  // Connection Interface

    /**
     * Establishes a connection to the database. Called once when the application first runs.*/
    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            // Password
            String password = "Passw0rd!";
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * Closes the connection to the database. Runs when the user closes the application.*/
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }


}