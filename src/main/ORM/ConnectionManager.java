package main.ORM;

import java.sql.*;

// TODO: capire quale livello di accesso è più appropriato per Connection Manager, oppure se mettere il costruttore di questa classe package-private
public class ConnectionManager {

    private static final String url = "jdbc:postgresql://localhost:5432/SWE?currentSchema=public";
    private static final String username = "postgres";
    private static final String password = "password";

    //TODO: con questo avremo una singola connessione per tutta l'app, ho scelto invece di aprire connesione ogni volta che serve
//    private static Connection connection = null;
//
//    // singleton instance
//    private static ConnectionManager instance = null;
//
//    private ConnectionManager(){}
//
//    public static ConnectionManager getInstance() {
//
//        if (instance == null) { instance = new ConnectionManager(); }
//
//        return instance;
//
//    }

    // TODO: questa classe potrebbe anche avere solo un metodo statico, e non servirebbe istanziarla, se si vuole fare si può fare private constructor

    public Connection getConnection() throws SQLException {
        // Exception handled in DAOs
        return DriverManager.getConnection(url, username, password);

    }
}