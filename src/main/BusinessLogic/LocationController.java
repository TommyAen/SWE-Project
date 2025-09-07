package main.BusinessLogic;

import main.DomainModel.Location;
import main.ORM.LocationDAO;

import java.sql.SQLException;
import java.util.List;

public class LocationController {
    private LocationDAO locationDAO;
    public LocationController() {
        this.locationDAO = new LocationDAO();
    }

    // Add a new location
    public void addLocation(int id, String name, String address, int carSpots) throws SQLException {
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
        locationDAO.updateCapacity(id, newCapacity);
    }

    // Remove a location by ID
    public void removeLocation(int id) throws SQLException {
        locationDAO.removeLocation(id);
    }
}