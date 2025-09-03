package main.ORM;

import main.DomainModel.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LocationDAO {
    private final ConnectionManager cm;

    public LocationDAO() {
        this.cm = new ConnectionManager();
//        try {
//            this.connection = ConnectionManager.getInstance().getConnection();
//        } catch (SQLException | ClassNotFoundException e) {
//            System.err.println("Error: " + e.getMessage());
//        }
    }

    // Insert Methods


    public void addLocation(Location l) throws SQLException {}
    // Read Methods
    public Location findByName(String name) throws SQLException { return null; }
    public List<Location> findAll() throws SQLException { return null; }

    // Update Methods
    public void updateCapacity(int locationId, int newCapacity) throws SQLException {}

    // Delete Methods
    public void removeLocation(int locationId) throws SQLException {}

    // Additional Methods
    public double distanceFrom(Location a, Location b) { return 0.0; }
}