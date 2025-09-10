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


    public TripController(AuthController authController) {
        this.tripDAO = new TripDAO();
        this.vehicleController = new VehicleController(authController);
        this.locationController = new LocationController();
        this.authController = authController;
    }

    public TripController(TripDAO tripDAO, VehicleController vehicleController,
                          LocationController locationController, AuthController authController) {
        this.tripDAO = tripDAO;
        this.vehicleController = vehicleController;
        this.locationController = locationController;
        this.authController = authController;
    }

    private Trip buildTrip(String originName, String destinationName, String dateStr, String timeStr) { //FIXME: forse ha senso usare pattern Builder per creare/modificare Trip
        try {
            if (originName == null || destinationName == null || dateStr == null || timeStr == null) {
                System.out.println("Invalid trip details provided.");
                return null;
            }

            Location origin = locationController.findByName(originName);
            Location destination = locationController.findByName(destinationName);
            if (origin == null || destination == null){
                System.out.println("Invalid locations provided");
                return null;
            }
            Date date = Date.valueOf(dateStr);
            Time time = Time.valueOf(timeStr);
            Vehicle vehicle = vehicleController.listAvailableVehiclesForLocation(origin).stream().findFirst().orElse(null);
            if (vehicle == null) {
                System.out.println("No available vehicles at the origin location.");
                return null;
            }

            return new Trip(-1, origin, destination, date, time, authController.getCurrentUser(), vehicle, Trip.TripState.SCHEDULED);
        } catch (IllegalArgumentException e) {
            System.out.println("Error parsing date or time: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Trip createTrip(String originName, String destinationName, String dateStr, String timeStr) {
        try {
            if (authController.isLoggedIn()) {
                System.out.println("User must be authenticated.");
                return null;
            }
            Trip trip = buildTrip(originName, destinationName, dateStr, timeStr);
            if (trip == null) {
                return null; // Error message already printed in buildTrip
            }
            return tripDAO.insertTrip(trip);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    public void cancelTrip(Trip trip) throws SQLException {
        // fare update dello stato del trip a CANCELED
        // dopo update fare notifica a chi ha bookings su quel trip
        if (!authController.isCurrentUserAdmin()){
            throw new SecurityException("Only admins can cancel trips.");
        }
        tripDAO.updateTripStatus(trip.getId(), Trip.TripState.CANCELED);
    }


    public void listAvailableTrips(String fromLocation, String toLocation, Date date) throws SQLException {
        List<Trip> availableTrips =  tripDAO.findAvailableTrips(fromLocation, toLocation, new java.sql.Date(date.getTime())); // FIXME: dates
        for (Trip trip : availableTrips) {
            System.out.println(trip);
        }
    }
    public void listAllAvailableTrips() throws SQLException {
        List<Trip> availableTrips =  tripDAO.findAll(); // FIXME: dates
        for (Trip trip : availableTrips) {
            System.out.println(trip);
        }
    }
    public void listTripsInDateRange(Date startDate, Date endDate) throws SQLException {
        List<Trip> tripsInRange = tripDAO.findTripsInDateRange(startDate, endDate);
        for (Trip trip : tripsInRange) {
            System.out.println(trip);
        }
    }

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
