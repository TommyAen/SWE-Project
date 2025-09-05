package main.BusinessLogic;

import main.DomainModel.Trip;
import main.ORM.*;

import java.util.List;

public class TripController {
    private TripDAO tripDAO;
    private VehicleDAO vehicleDAO;
    private LocationDAO locationDAO;
    private AuthController authController;

    public TripController(AuthController authController) { // TODO: parlare di dependency injections, e deciderle se passarle al costruttore o altro
        this.tripDAO = new TripDAO();
        this.vehicleDAO = new VehicleDAO();
        this.locationDAO = new LocationDAO();
        this.authController = authController;
    }

    //FIXME: forse ha senso usare pattern Builder per creare/modificare Trip
    public boolean createTrip(int vehicleId, String startLocation, String endLocation, String departureTime, int availableSeats) {
        return false; // TODO
    }
    public boolean modifyTrip(int tripId, String newStartLocation, String newEndLocation, String newDepartureTime, int newAvailableSeats) {
        return false; // TODO
    }
    public boolean cancelTrip(int tripId) { return false; } // TODO
    public boolean viewTripDetails(int tripId) { return false; } // TODO
    public boolean listAvailableTrips(String fromLocation, String toLocation, String date) { return false; } // TODO

    public boolean assignDriver(int tripId, int driverId) { return false; } // TODO
    public boolean updateTripStatus(int tripId, String status) { return false; } //

    public List<Trip> getTripsByDate(String date) { return null; } // TODO



}
