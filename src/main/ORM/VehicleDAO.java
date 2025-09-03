package main.ORM;

import main.DomainModel.*;
import java.sql.SQLException;
import java.util.List;

public class VehicleDAO {
    private final ConnectionManager cm;
    public VehicleDAO() {
        this.cm = new ConnectionManager();
    }

    // Create Methods
    public void insertVehicle(Vehicle v, String location) throws SQLException {} // TODO: come aggiorno anche tabella location?

    // Read Methods
    public List<Vehicle> findInLocation(String location) throws SQLException { return null; }
    public List<Vehicle> findAvailable() throws SQLException { return null; }
    public List<Vehicle> findAvailableInLocation(Location location) throws SQLException { return null; }

    // Update Methods
    public void updateStatus(int vehicleId, String status) throws SQLException {}

    // Delete Methods
    public void removeVehicle(int vehicleId) throws SQLException {}

}