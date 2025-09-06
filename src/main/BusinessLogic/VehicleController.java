package main.BusinessLogic;

import main.DomainModel.Vehicle;
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

    public boolean addVehicle(){ return false; } // TODO
    public boolean modifyVehicle(){ return false; } // TODO
    public boolean removeVehicle(){ return false; } // TODO
    public boolean viewVehicleDetails(){ return false; } // TODO



    public boolean modifyStatus(){ return false; } // TODO
    public void changeVehicleLocation(){} // TODO

    public List<Vehicle> listAllVehicles(){ return null; } // TODO

    public List<Vehicle> listAvailableVehiclesForLocation(){ return null; } // TODO

    public boolean isVehicleAvailable(int vehicleId) throws SQLException {
        return vehicleDAO.isVehicleAvailable(vehicleId);
    }

}
