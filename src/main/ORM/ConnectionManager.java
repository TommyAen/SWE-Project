package main.ORM;

import java.sql.*;

// TODO: capire quale livello di accesso è più appropriato per Connection Manager, oppure se mettere il costruttore di questa classe package-private
public class ConnectionManager {

    private static final String url = "jdbc:postgresql://localhost:5432/SWE?currentSchema=public";
    private static final String username = "postgres";
    private static final String password = "password";

    // FIXME: singleton ?
    private ConnectionManager(){} // Item 4 Effective Java: ConnectionManager is a utility class and should not be instantiated

//    public static ConnectionManager getInstance() {
//        if (instance == null) {
//            instance = new ConnectionManager();
//        }
//        return instance;
//    }

    public static Connection getConnection() throws SQLException {
        // Exception handled in DAOs
        return DriverManager.getConnection(url, username, password); // change this if needed a connection pool

    }
}