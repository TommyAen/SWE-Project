package main.BusinessLogic;

import main.DomainModel.Location;
import main.DomainModel.Trip;
import main.DomainModel.User;
import main.DomainModel.Vehicle;
import main.ORM.*;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;
import java.util.List;

public class TripController {
    private TripDAO tripDAO;
    private VehicleController vehicleController;
    private LocationController locationController;
    private AuthController authController;


    public TripController(AuthController authController) { // TODO: parlare di dependency injections, e deciderle se passarle al costruttore o altro
        this.tripDAO = new TripDAO();
        this.vehicleController = new VehicleController(authController);
        this.locationController = new LocationController();
        this.authController = authController;
    }

    //FIXME: forse ha senso usare pattern Builder per creare/modificare Trip
    public Trip createTrip(int vehicleId, Location origin, Location destination, Date date, Time time) {
        try {
            if (authController.getCurrentUser() == null) {
                System.out.println("No authenticated user found.");
                return null;
            }

            if (origin == null || destination == null || date == null || time == null) {
                System.out.println("Invalid trip details provided.");
                return null;
            }

            if (!vehicleController.isVehicleAvailable(vehicleId)) {
                System.out.println("Vehicle is not available.");
                return null;
            }

            VehicleDAO vehicleDAO = new VehicleDAO();
            Vehicle vehicle = vehicleDAO.findById(vehicleId);
            if (vehicle == null) {
                System.out.println("Vehicle not found.");
                return null;
            }

            User user = authController.getCurrentUser();
            int user_id = user.getId();
            UserDAO userDAO = new UserDAO();
            if (!userDAO.hasLicense(user_id)) {
                System.out.println("Only drivers can create trips.");
                return null;
            }

            // User that creates the trip is the driver
            return tripDAO.insertTrip(origin, destination, date, time, user, vehicle);
        } catch (SQLException e) {
            System.err.println("Error creating trip: " + e.getMessage());
            return null;
        }
    }
    public void modifyTrip(int tripId, Location newOrigin, Location newDestination, Time newTime, Date newDate, User newDriver,Vehicle newVehicle, Trip.TripState newState) throws SQLException {
        Trip trip = tripDAO.findById(tripId);
        Trip newTrip = new Trip(trip.getId(), newOrigin, newDestination, newDate, newTime, newDriver,newVehicle, newState);
        tripDAO.updateTrip(trip, newTrip);
        // TODO: gestire errori
    }

    public Trip findById(int tripId) {
        try {
            return tripDAO.findById(tripId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public boolean cancelTrip(int tripId) {
        // fare update dello stato del trip a CANCELED
        // dopo update fare notifica a chi ha bookings su quel trip

        try{ // remove trip from database: necessario??
            tripDAO.removeTrip(tripId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    } // TODO




    public boolean viewTripDetails(int tripId) {
        return false;
    } // TODO

    public void listAvailableTrips(String fromLocation, String toLocation, Date date) throws SQLException {
        List<Trip> availableTrips =  tripDAO.findAvailableTrips(fromLocation, toLocation, new java.sql.Date(date.getTime())); // FIXME: dates
        for (Trip trip : availableTrips) {
            System.out.println(trip);
        }
    } // TODO

    public boolean addPassenger(int tripId, int userId) {
        // prima di tutto controllare che il trip non sia pieno
        // poi aggiungere il passeggero facendo una nuova booking
        // infine decrementare i posti disponibili del trip
        return false;
    } // TODO

    public boolean assignDriver(int tripId, int driverId) {
        return false;
    } // TODO

    public boolean updateTripStatus(int tripId, String status) {
        return false;
    } //

    public List<Trip> getTripsByDate(String date) {
        return null;
    } // TODO

    public boolean removePassenger(int tripId, int userId) {
        // rimuovere il passeggero facendo delete della booking
        // infine incrementare i posti disponibili del trip
        return false;
    } // TODO

    // Additional Methods
    public int getAvailableSeats(int trip_id) throws SQLException {
        BookingDAO bookingDAO = new BookingDAO();

        int seats = tripDAO.getSeatsForTrip(trip_id);
        int occupiedSeats = bookingDAO.countBookingsForTrip(trip_id);

        return seats - occupiedSeats;

    } //
    public boolean isFull(Trip trip) throws SQLException {
        return getAvailableSeats(trip.getId()) == 0;
    }
}
