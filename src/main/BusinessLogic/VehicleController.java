package main.BusinessLogic;

import main.DomainModel.Vehicle;
import main.DomainModel.Location;
import main.ORM.VehicleDAO;
import main.ORM.LocationDAO;

import java.sql.SQLException;
import java.util.List;

public class VehicleController {

    private VehicleDAO vehicleDAO;
    private LocationDAO locationDAO;
    private AuthController authController;

    public VehicleController(AuthController authController) {
        this.vehicleDAO = new VehicleDAO();
        this.locationDAO = new LocationDAO();
        this.authController = authController;
    }

    public void addVehicle(Vehicle vehicle) {
        try {
            vehicleDAO.insertVehicle(vehicle);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifyVehicle(Vehicle oldVehicle, Vehicle newVehicle) {
        try {
            vehicleDAO.updateVehicle(oldVehicle.getId(),newVehicle);
            // You may want to update other fields as needed
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean removeVehicle(int vehicleId) {
        try {
            vehicleDAO.removeVehicle(vehicleId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void viewVehicleDetails(int vehicleId) {
        try {
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
        // Assuming you have a method to update location in VehicleDAO
        // If not, you need to implement it in VehicleDAO
        return false; // TODO: Implement if needed
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
