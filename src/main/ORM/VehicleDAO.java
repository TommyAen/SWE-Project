package main.ORM;

import main.DomainModel.*;

import java.sql.PreparedStatement;
import java.sql.*;

import java.util.List;

public class VehicleDAO {
    //private final ConnectionManager cm;
    public VehicleDAO() {
        //this.cm = new ConnectionManager();
    }

    // Create Methods
    public Vehicle insertVehicle(int capacity, Vehicle.VehicleState state, Location location) throws SQLException {
        String insertSQL = "INSERT INTO vehicle (capacity, state, location) VALUES ( ?, ?, ?)";
        try(Connection connection=ConnectionManager.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, capacity);
            preparedStatement.setString(2, state.toString());
            preparedStatement.setInt(3, location.getId());

            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    return new Vehicle(generatedId, capacity, state, location);
                } else {
                    throw new SQLException("Creating vehicle failed, no ID obtained.");
                }
            }
        }
    } // TODO: come aggiorno anche tabella location? Lo deve fare il controller ?

    public Vehicle insertVehicle(Vehicle v) throws SQLException {
        return insertVehicle(v.getCapacity(), v.getState(), v.getLocation()); // FIXME: giusto?
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
    public List<Vehicle> findInLocation(Location location) throws SQLException {
        String selectSQL = "SELECT * FROM vehicle WHERE location = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, location.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Vehicle> vehicles = new java.util.ArrayList<>();
                while (resultSet.next()) {
                    int vehicleId = resultSet.getInt("id");
                    int capacity = resultSet.getInt("capacity");
                    Vehicle.VehicleState state = Vehicle.VehicleState.valueOf(resultSet.getString("state"));
                    vehicles.add(new Vehicle(vehicleId, capacity, state, location));
                }
                return vehicles;
            }
        }
    }
    public List<Vehicle> findAvailable() throws SQLException {
        String selectSQL = "SELECT * FROM vehicle WHERE state != 'OUT_OF_SERVICE'";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Vehicle> vehicles = new java.util.ArrayList<>();
            LocationDAO locationDAO = new LocationDAO();
            while (resultSet.next()) {
                int vehicleId = resultSet.getInt("id");
                int capacity = resultSet.getInt("capacity");
                Vehicle.VehicleState state = Vehicle.VehicleState.valueOf(resultSet.getString("state"));
                int loc_id = resultSet.getInt("location");
                Location location = locationDAO.findById(loc_id);
                vehicles.add(new Vehicle(vehicleId, capacity, state, location));
            }
            return vehicles;
        }
    }
    public List<Vehicle> findAvailableInLocation(Location location) throws SQLException {
        List<Vehicle> vehicles = findAvailable();
        for(Vehicle v: vehicles) {
            if(v.getLocation().getId() != location.getId()) {
                vehicles.remove(v);
            }
        }
        return vehicles;
    }

    // Update Methods
    public void updateVehicle(int vehicleId, Vehicle newVehicle) throws SQLException {
        String updateSQL = "UPDATE vehicle SET capacity = ?, state = ?, location = ? WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setInt(1, newVehicle.getCapacity());
            preparedStatement.setString(2, newVehicle.getState().toString());
            if (newVehicle.getLocation() != null) {
                preparedStatement.setInt(3, newVehicle.getLocation().getId());
            } else {
                preparedStatement.setNull(3, Types.INTEGER); // FIXME: gestire meglio
            }
            preparedStatement.setInt(4, vehicleId);

            preparedStatement.executeUpdate();
        }
    }

    public void updateVehicleState(int vehicleId, Vehicle.VehicleState newState) throws SQLException {
        Vehicle vehicle = findById(vehicleId);
        if (vehicle != null) {
            vehicle.setState(newState);
            updateVehicle(vehicleId, vehicle);
        } // TODO: else vehicle not found, handle as needed
    }

    // Delete Methods
    public void removeVehicle(int vehicleId) throws SQLException {
        String deleteSQL = "DELETE FROM vehicle WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, vehicleId);
            preparedStatement.executeUpdate();
        }
    }

    public void removeAllVehicles() throws SQLException {
        String deleteSQL = "DELETE FROM vehicle";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.executeUpdate();
        }
    }

    public boolean isVehicleAvailable(int vehicleId) throws SQLException { // FIXME: aggiungere controllo su orario?? o semplicemente se è WORKING o no?
//        Vehicle vehicle = findById(vehicleId);
//        return vehicle.getState() != Vehicle.VehicleState.WORKING;
        // TODO
        return true;
    }
}