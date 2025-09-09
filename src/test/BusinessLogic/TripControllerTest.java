package test.BusinessLogic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import main.ORM.ConnectionManager;
import java.sql.*;
import main.BusinessLogic.*;
import main.DomainModel.*;
import main.ORM.*;


import static org.junit.jupiter.api.Assertions.*;

class TripControllerTest {
    private TripController tripController;
    private AuthController authController;
    private UserDAO userDAO;
    private LocationDAO locationDAO;
    private VehicleDAO vehicleDAO;
    private BookingDAO bookingDAO;
    private TripDAO tripDAO;

    @BeforeEach
    void setUp() {
        // Clean up tables before each test
        bookingDAO = new BookingDAO();
        tripDAO = new TripDAO();
        authController = new AuthController();
        tripController = new TripController(authController);
        userDAO = new UserDAO();
        locationDAO = new LocationDAO();
        vehicleDAO = new VehicleDAO();
        try {
            bookingDAO.removeAllBookings();
            tripDAO.removeAllTrips();
            vehicleDAO.removeAllVehicles();
            locationDAO.removeAllLocations();
            userDAO.removeAllUsers();
            // Insert test user and authenticate
            userDAO.insertStudent(1, "Driver", "Test", "driver@test.com", "password", "345fakeDL");
            authController.login(userDAO.findById(1), "password");
        } catch (Exception e) { fail(e); }
    }

    @AfterEach
    void tearDown() {
        try {
            bookingDAO.removeAllBookings();
            tripDAO.removeAllTrips();
            vehicleDAO.removeAllVehicles();
            locationDAO.removeAllLocations();
            userDAO.removeAllUsers();
        } catch (Exception e) { fail(e); }
    }

    @Test
    void createTrip() {
        try {
            Location origin = locationDAO.addLocation(new Location("Origin", "Addr1", 5));
            Location destination = locationDAO.addLocation(new Location("Destination", "Addr2", 5));
            Vehicle vehicle = vehicleDAO.insertVehicle(new Vehicle( 4, Vehicle.VehicleState.WORKING, origin));
            Trip trip = tripController.createTrip(vehicle.getId(), origin, destination, Date.valueOf("2025-09-07"), Time.valueOf("10:00:00"));
            assertNotNull(trip);
            assertEquals("Origin", trip.getOrigin().getName());
            assertEquals("Destination", trip.getDestination().getName());
        } catch (Exception e) { fail(e); }
    }

    @Test
    void findById() {
        try {
            Location origin = locationDAO.addLocation(new Location(0, "Origin", "Addr1", 5));
            Location destination = locationDAO.addLocation(new Location(0, "Destination", "Addr2", 5));
            Vehicle vehicle = vehicleDAO.insertVehicle(new Vehicle( 4, Vehicle.VehicleState.WORKING, origin));
            Trip trip = tripController.createTrip(vehicle.getId(), origin, destination, Date.valueOf("2025-09-07"), Time.valueOf("10:00:00"));
            Trip found = tripController.findById(trip.getId());
            assertNotNull(found);
            assertEquals(trip.getId(), found.getId());
        } catch (Exception e) { fail(e); }
    }

    @Test
    void modifyTrip() {
        try {
            Location origin = locationDAO.addLocation(new Location(0, "Origin", "Addr1", 5));
            Location destination = locationDAO.addLocation(new Location(0, "Destination", "Addr2", 5));
            Vehicle vehicle = vehicleDAO.insertVehicle(new Vehicle( 4, Vehicle.VehicleState.WORKING, origin));
            Trip trip = tripController.createTrip(vehicle.getId(), origin, destination, Date.valueOf("2025-09-07"), Time.valueOf("10:00:00"));

            Location newOrigin = locationDAO.addLocation(new Location(0, "NewOrigin", "Addr3", 5));
            Location newDestination = locationDAO.addLocation(new Location(0, "NewDestination", "Addr4", 5));
            Vehicle newVehicle = vehicleDAO.insertVehicle(new Vehicle( 4, Vehicle.VehicleState.WORKING, origin));

            User driver = authController.getCurrentUser();
            tripController.modifyTrip(trip.getId(), newOrigin, newDestination, Time.valueOf("12:00:00"), Date.valueOf("2025-09-08"), driver, newVehicle, Trip.TripState.SCHEDULED);
            Trip modified = tripController.findById(trip.getId());
            assertEquals("NewOrigin", modified.getOrigin().getName());
            assertEquals("NewDestination", modified.getDestination().getName());
            assertEquals(Date.valueOf("2025-09-08"), modified.getDate());
            assertEquals(Time.valueOf("12:00:00"), modified.getTime());
        } catch (Exception e) { fail(e); }
    }

    @Test
    void cancelTrip() {
        try {
            Location origin = locationDAO.addLocation(new Location(0, "Origin", "Addr1", 5));
            Location destination = locationDAO.addLocation(new Location(0, "Destination", "Addr2", 5));
            Vehicle vehicle = vehicleDAO.insertVehicle(new Vehicle( 4, Vehicle.VehicleState.WORKING, origin));

            Trip trip = tripController.createTrip(vehicle.getId(), origin, destination, Date.valueOf("2025-09-07"), Time.valueOf("10:00:00"));
            boolean result = tripController.cancelTrip(trip.getId());
            Trip found = tripController.findById(trip.getId());
            assertNull(found);
        } catch (Exception e) { fail(e); }
    }

    @Test
    void getAvailableSeatsAndIsFull() {
        try {
            Location origin = locationDAO.addLocation(new Location(0, "Origin", "Addr1", 5));
            Location destination = locationDAO.addLocation(new Location(0, "Destination", "Addr2", 5));
            Vehicle vehicle = vehicleDAO.insertVehicle(new Vehicle( 2, Vehicle.VehicleState.WORKING, origin));

            Trip trip = tripController.createTrip(vehicle.getId(), origin, destination, Date.valueOf("2025-09-07"), Time.valueOf("10:00:00"));
            int seats = tripController.getAvailableSeats(trip.getId());
            assertEquals(2, seats);
            assertFalse(tripController.isFull(trip));
        } catch (Exception e) { fail(e); }
    }
}