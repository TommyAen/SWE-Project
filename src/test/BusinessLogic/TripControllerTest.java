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

    @BeforeEach
    void setUp() {
        // Clean up tables before each test
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM trip");
            stmt.executeUpdate("DELETE FROM vehicle");
            stmt.executeUpdate("DELETE FROM location");
            stmt.executeUpdate("DELETE FROM \"User\" ");
        } catch (Exception e) {}
        authController = new AuthController();
        tripController = new TripController(authController);
        userDAO = new UserDAO();
        locationDAO = new LocationDAO();
        vehicleDAO = new VehicleDAO();
        // Insert test user and authenticate
        try {
            userDAO.insertStudent(1, "Driver", "Test", "driver@test.com", "password", "345fakeDL");
            authController.login(userDAO.findById(1), "password");
        } catch (Exception e) { fail(e); }
    }

    @AfterEach
    void tearDown() {
        // Clean up tables after each test
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM trip");
            stmt.executeUpdate("DELETE FROM vehicle");
            stmt.executeUpdate("DELETE FROM location");
            stmt.executeUpdate("DELETE FROM \"User\" ");
        } catch (Exception e) {}
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