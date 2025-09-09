package main.ORM;

import java.sql.*;

// TODO: capire quale livello di accesso è più appropriato per Connection Manager, oppure se mettere il costruttore di questa classe package-private
public class ConnectionManager {
    private static final String url = "jdbc:postgresql://localhost:5432/SWE?currentSchema=public";
    private static final String username = "postgres";
    private static final String password = "password";
    private static Connection connection = null;
    private static ConnectionManager instance = null;

    private ConnectionManager(){}

    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }
    public static Connection getConnection() throws SQLException {
        if (connection == null)
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
            }
        return connection;
    }
}