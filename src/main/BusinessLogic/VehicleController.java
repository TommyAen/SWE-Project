package main.BusinessLogic;

import main.DomainModel.Vehicle;
import main.DomainModel.Location;
import main.ORM.VehicleDAO;
import main.ORM.LocationDAO;

import java.sql.SQLException;
import java.util.List;

public class VehicleController {

    private VehicleDAO vehicleDAO;
    private LocationController locationController;
    private AuthController authController;

    public VehicleController(AuthController authController) {
        this.vehicleDAO = new VehicleDAO();
        this.locationController = new LocationController();
        this.authController = authController;
    }

    public VehicleController(AuthController authController, VehicleDAO vehicleDAO, LocationController locationDAO) {
        this.vehicleDAO = vehicleDAO;
        this.locationController = locationDAO;
        this.authController = authController;
    }

    public void addVehicle(Vehicle vehicle) {
        try {
            if (!authController.isCurrentUserAdmin()) {
                throw new SecurityException("Only admins can add vehicles.");
            }
            vehicleDAO.insertVehicle(vehicle);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifyVehicle(Vehicle oldVehicle, Vehicle newVehicle) {
        try {
            if (!authController.isCurrentUserAdmin()) {
                throw new SecurityException("Only admins can modify vehicles.");
            }
            vehicleDAO.updateVehicle(oldVehicle.getId(),newVehicle);
            // You may want to update other fields as needed
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean removeVehicle(int vehicleId) {
        try {
            if (!authController.isCurrentUserAdmin()) {
                throw new SecurityException("Only admins can remove vehicles.");
            }
            vehicleDAO.removeVehicle(vehicleId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void viewVehicleDetails(int vehicleId) {
        try {
            if (!authController.isCurrentUserAdmin()) {
                throw new SecurityException("Only admins can view vehicle details.");
            }
            Vehicle vehicle = vehicleDAO.findById(vehicleId);
            System.out.println(vehicle);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean modifyStatus(int vehicleId, Vehicle.VehicleState newState) {
        try {

            Vehicle vehicle = vehicleDAO.findById(vehicleId);
            if (vehicle == null) {
                return false; // Vehicle not found
            }
            Vehicle newVehicle = new Vehicle(vehicle.getId(), vehicle.getCapacity(), newState, vehicle.getLocation());
            vehicleDAO.updateVehicle(vehicleId, newVehicle);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changeVehicleLocation(int vehicleId, Location newLocation) {
        if (newLocation == null) {
            return false; // Invalid location
        }
        try {
            Vehicle vehicle = vehicleDAO.findById(vehicleId);
            if (vehicle == null) {
                return false; // Vehicle not found
            }
            Vehicle newVehicle = new Vehicle(vehicle.getId(), vehicle.getCapacity(), vehicle.getState(), newLocation);
            vehicleDAO.updateVehicle(vehicleId, newVehicle);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Vehicle> listAllVehicles() {
        // Assuming you have a method to list all vehicles in VehicleDAO
        return null; // TODO: Implement if needed
    }

    public List<Vehicle> listAvailableVehiclesForLocation(Location location) {
        try {
            return vehicleDAO.findAvailableInLocation(location);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isVehicleAvailable(int vehicleId) throws SQLException {
        return vehicleDAO.isVehicleAvailable(vehicleId);
    }

}
