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
    static void setup() throws SQLException {
        VehicleDAO setupVDAO = new VehicleDAO();
        setupVDAO.removeAllVehicles();
        LocationDAO setupLDAO = new LocationDAO();
        setupLDAO.removeAllLocations();
        UserDAO setupUDAO = new UserDAO();
        setupUDAO.removeAllUsers();
        TripDAO setupTDAO = new TripDAO();
        setupTDAO.removeAllTrips();
    }

    @BeforeEach
    void setupDatabase() {
        try {
            LocationDAO locationDAO = new LocationDAO();
            VehicleDAO vehicleDAO = new VehicleDAO();
            UserDAO userDAO = new UserDAO();
            TripDAO tripDAO = new TripDAO();

            // Ensure locations exist
            origin = locationDAO.addLocation(new Location(1, "Origin City", "123 Main St", 7));
            destination = locationDAO.addLocation(new Location(2, "Destination City", "456 Elm St", 5));

            // Ensure vehicle exists
            vehicle = vehicleDAO.insertVehicle(new Vehicle(1, 4, Vehicle.VehicleState.WORKING, origin));

            // Ensure user exists
            userDAO.insertStudent(1, "Driver", "Test", "driver@test.com", "password");
            userDAO.addLicense(1, "ABSDL2");
            driver = userDAO.findById(1);

            // Ensure trip exists

        } catch (SQLException e) {
            fail("Database setup failed: " + e.getMessage());
        }
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
    void findById() {}
        // TODO




    @Test
    void updateTripDate() {
        try {
            Date newDate = Date.valueOf("2025-10-01");
            tripDAO.updateTripDate(1, newDate);
            Trip updatedTrip = tripDAO.findById(1);
            assertEquals(newDate, updatedTrip.getDate());
        } catch (SQLException e) {
            fail("Update failed: " + e.getMessage());
        }
    }
}