package main.ORM;

import main.DomainModel.*;

import java.sql.PreparedStatement;
import java.sql.*;
import java.sql.Time;
import java.util.List;

public class VehicleDAO {
    //private final ConnectionManager cm;
    public VehicleDAO() {
        //this.cm = new ConnectionManager();
    }

    // Create Methods
    public void insertVehicle(int capacity, Vehicle.VehicleState state, Location location) throws SQLException {
        String insertSQL = "INSERT INTO vehicle (capacity, state, location) VALUES ( ?, ?, ?)";
        try(Connection connection=ConnectionManager.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, capacity);
            preparedStatement.setString(2, state.toString());
            preparedStatement.setInt(3, location.getId());

            preparedStatement.executeUpdate();
        }
    } // TODO: come aggiorno anche tabella location? Lo deve fare il controller ?

    public void insertVehicle(Vehicle v) throws SQLException {
        insertVehicle(v.getCapacity(), v.getState(), v.getLocation()); // FIXME: giusto?
    } // TODO: come aggiorno anche tabella location? Lo deve fare il controller

    // Read Methods
    public Vehicle findById(int id) throws SQLException {
        String selectSQL = "SELECT * FROM vehicle WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int vehicleId = resultSet.getInt("id");
                    int capacity = resultSet.getInt("capacity");
                    Vehicle.VehicleState state = Vehicle.VehicleState.valueOf(resultSet.getString("state"));
                    int loc_id = resultSet.getInt("location");
                    LocationDAO locationDAO = new LocationDAO();
                    Location location = locationDAO.findById(loc_id);
                    return new Vehicle(vehicleId, capacity, state, location);
                } else {
                    return null; // Vehicle not found
                }
            }
        }
    }
    public List<Vehicle> findInLocation(String location) throws SQLException { return null; }
    public List<Vehicle> findAvailable() throws SQLException { return null; }
    public List<Vehicle> findAvailableInLocation(Location location) throws SQLException { return null; }

    // Update Methods
    public void updateStatus(int vehicleId, String status) throws SQLException {}

    // Delete Methods
    public void removeVehicle(int vehicleId) throws SQLException {}

    public boolean isVehicleAvailable(int vehicleId) throws SQLException { // FIXME: aggiungere controllo su orario?? o semplicemente se è WORKING o no?
        Vehicle vehicle = findById(vehicleId);
        return vehicle.getState() != Vehicle.VehicleState.WORKING;
    }
}