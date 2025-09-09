package main.ORM;

import main.DomainModel.Location;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {
    private Connection connection;
    public LocationDAO() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Insert Methods
    public Location addLocation(Location location) throws SQLException {
        String insertSQL = "INSERT INTO location (name, address, parking_spots) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, location.getName());
            preparedStatement.setString(2, location.getAddress());
            preparedStatement.setInt(3, location.getCarSpots());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    return new Location(generatedId, location.getName(), location.getAddress(), location.getCarSpots());
                } else {
                    throw new SQLException("Creating location failed, no ID obtained.");
                }
            }
        }
    }

    // Read Methods
    public Location findById(int id) throws SQLException {
        String selectSQL = "SELECT * FROM location WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Location(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4)
                    );
                }
            }
        }
        return null;
    }

    public Location findByName(String name) throws SQLException {
        String selectSQL = "SELECT * FROM location WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, name);
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setInt(1, newCapacity);
            preparedStatement.setInt(2, locationId);
            preparedStatement.executeUpdate();
        }
    }

    // Delete Methods
    public void removeLocation(int locationId) throws SQLException {
        String deleteSQL = "DELETE FROM location WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, locationId);
            preparedStatement.executeUpdate();
        }
    }
    public void removeAllLocations() throws SQLException {
        String deleteSQL = "DELETE FROM location";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.executeUpdate();
        }
    }

    // Additional Methods
    public double distanceFrom(Location a, Location b) {
        // Placeholder implementation for distance calculation
        return 0.0;
    }
}