package test.ORM;

import main.DomainModel.*;
import main.ORM.LocationDAO;
import main.ORM.*;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;

class TripDAOTest {

    private static TripDAO tripDAO;
    private Trip testTrip;
    private Location origin;
    private Location destination;
    private Vehicle vehicle;
    private User driver;

    public TripDAOTest() {
        origin = null;
        destination = null;
        driver = null;
        vehicle = null;
        tripDAO = new TripDAO();
        testTrip = null;

    }

    @BeforeAll
    static void setup() {
        tripDAO = new TripDAO();
    }

    @BeforeEach
    void setupDatabase() {
        try {
            LocationDAO locationDAO = new LocationDAO();
            VehicleDAO vehicleDAO = new VehicleDAO();
            UserDAO userDAO = new UserDAO();
            tripDAO.removeAllTrips();
            vehicleDAO.removeAllVehicles();
            locationDAO.removeAllLocations();
            userDAO.removeAllUsers();
            // Ensure locations exist
            origin = locationDAO.addLocation(new Location(1, "Origin City", "123 Main St", 7));
            destination = locationDAO.addLocation(new Location(2, "Destination City", "456 Elm St", 5));
            // Ensure vehicle exists
            vehicle = vehicleDAO.insertVehicle(new Vehicle(1, 4, Vehicle.VehicleState.WORKING, origin));
            // Ensure user exists
            userDAO.insertStudent(1, "Driver", "Test", "driver@test.com", "password");
            userDAO.addLicense(1, "ABSDL2");
            driver = userDAO.findById(1);
        } catch (SQLException e) {
            fail("Database setup failed: " + e.getMessage());
        }
    }

    @AfterEach
    void teardownDatabase() {
        try {
            tripDAO.removeAllTrips();
            VehicleDAO vehicleDAO = new VehicleDAO();
            LocationDAO locationDAO = new LocationDAO();
            UserDAO userDAO = new UserDAO();
            vehicleDAO.removeAllVehicles();
            locationDAO.removeAllLocations();
            userDAO.removeAllUsers();
        } catch (SQLException ignored) {}
    }

    @Test
    void insertTrip() {
        try {
            LocationDAO locationDAO = new LocationDAO();

            VehicleDAO vehicleDAO = new VehicleDAO();
            UserDAO userDAO = new UserDAO();
            testTrip = tripDAO.insertTrip(
                    locationDAO.findById(origin.getId()),
                    locationDAO.findById(destination.getId()),
                    Date.valueOf("2025-09-06"),
                    Time.valueOf("10:00:00"),
                    driver,
                    vehicle);
            assertNotNull(testTrip);
            assertEquals("Origin City", testTrip.getOrigin().getName());
            assertEquals("Destination City", testTrip.getDestination().getName());
        } catch (SQLException e) {
            fail("Insertion failed: " + e.getMessage());
        }
    }

    @Test
    void findById() {
        try {
            // Insert a trip first
            Trip insertedTrip = tripDAO.insertTrip(
                origin,
                destination,
                Date.valueOf("2025-09-06"),
                Time.valueOf("10:00:00"),
                driver,
                vehicle
            );
            assertNotNull(insertedTrip);
            // Find by id
            Trip foundTrip = tripDAO.findById(insertedTrip.getId());
            assertNotNull(foundTrip);
            assertEquals(insertedTrip.getId(), foundTrip.getId());
            assertEquals("Origin City", foundTrip.getOrigin().getName());
            assertEquals("Destination City", foundTrip.getDestination().getName());
            assertEquals(Date.valueOf("2025-09-06"), foundTrip.getDate());
            assertEquals(Time.valueOf("10:00:00"), foundTrip.getTime());
        } catch (SQLException e) {
            fail("findById failed: " + e.getMessage());
        }
    }

    @Test
    void updateTripDate() {
        try {
            // Insert a trip first
            Trip insertedTrip = tripDAO.insertTrip(
                origin,
                destination,
                Date.valueOf("2025-09-06"),
                Time.valueOf("10:00:00"),
                driver,
                vehicle
            );
            assertNotNull(insertedTrip);
            // Update the date
            Date newDate = Date.valueOf("2025-10-10");
            tripDAO.updateTripDate(insertedTrip.getId(), newDate);
            Trip updatedTrip = tripDAO.findById(insertedTrip.getId());
            assertNotNull(updatedTrip);
            assertEquals(newDate, updatedTrip.getDate());
            assertEquals("Origin City", updatedTrip.getOrigin().getName());
            assertEquals("Destination City", updatedTrip.getDestination().getName());
        } catch (SQLException e) {
            fail("updateTripDate failed: " + e.getMessage());
        }
    }
}