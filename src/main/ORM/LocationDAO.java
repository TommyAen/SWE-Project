package main.ORM;

import main.DomainModel.Location;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {
    //private final ConnectionManager cm;

    public LocationDAO() {
        // Default constructor
    }

    // Insert Methods
    public void addLocation(Location location) throws SQLException {
        String insertSQL = "INSERT INTO location (name, address, parking_spots) VALUES (?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, location.getId());
            preparedStatement.setString(2, location.getName());
            preparedStatement.setString(3, location.getAddress());
            preparedStatement.setInt(4, location.getCarSpots());
            preparedStatement.executeUpdate();
        }
    }

    // Read Methods
    public Location findById(int id) throws SQLException {
        String selectSQL = "SELECT * FROM location WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Location(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("address"),
                        resultSet.getInt("parking_spots")
                    );
                }
            }
        }
        return null;
    }

    public List<Location> findAll() throws SQLException {
        String selectSQL = "SELECT * FROM location";
        List<Location> locations = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                locations.add(new Location(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("address"),
                    resultSet.getInt("car_spots")
                ));
            }
        }
        return locations;
    }

    // Update Methods
    public void updateCapacity(int locationId, int newCapacity) throws SQLException {
        String updateSQL = "UPDATE location SET parking_spots = ? WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setInt(1, newCapacity);
            preparedStatement.setInt(2, locationId);
            preparedStatement.executeUpdate();
        }
    }

    // Delete Methods
    public void removeLocation(int locationId) throws SQLException {
        String deleteSQL = "DELETE FROM location WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, locationId);
            preparedStatement.executeUpdate();
        }
    }

    // Additional Methods
    public double distanceFrom(Location a, Location b) {
        // Placeholder implementation for distance calculation
        return 0.0;
    }
}