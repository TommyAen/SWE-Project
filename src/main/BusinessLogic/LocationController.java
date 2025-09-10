package main.BusinessLogic;

import main.DomainModel.Location;
import main.ORM.LocationDAO;

import java.sql.SQLException;
import java.util.List;

public class LocationController {
    private LocationDAO locationDAO;
    private AuthController authController;

    public LocationController() {
        this.locationDAO = new LocationDAO();
        this.authController = new AuthController();
    }
    public LocationController(LocationDAO dao, AuthController authController) {
        this.locationDAO = dao;
        this.authController = authController;
    }

    // Add a new location
    public void addLocation(int id, String name, String address, int carSpots) throws SQLException {
        if (!authController.isCurrentUserAdmin()){
            throw new SecurityException("Only admins can add locations.");
        }
        Location location = new Location(id, name, address, carSpots);
        locationDAO.addLocation(location);
    }

    // Find a location by ID
    public Location findLocationById(int id) throws SQLException {
        return locationDAO.findById(id);
    }

    // Find all locations
    public List<Location> findAllLocations() throws SQLException {
        return locationDAO.findAll();
    }

    // Update the capacity of a location
    public void updateLocationCapacity(int id, int newCapacity) throws SQLException {
        if (!authController.isCurrentUserAdmin()){
            throw new SecurityException("Only admins can update location capacity.");
        }
        locationDAO.updateCapacity(id, newCapacity);
    }

    // Remove a location by ID
    public void removeLocation(int id) throws SQLException {
        if (!authController.isCurrentUserAdmin()){
            throw new SecurityException("Only admins can remove locations.");
        }
        locationDAO.removeLocation(id);
    }
    public Location findByName(String originName) throws SQLException {
        return locationDAO.findByName(originName);
    }
}