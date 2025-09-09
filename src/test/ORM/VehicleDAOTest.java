package test.ORM;

import main.DomainModel.Vehicle;
import main.DomainModel.Location;
import main.ORM.VehicleDAO;
import main.ORM.LocationDAO;
import main.ORM.BookingDAO;
import main.ORM.TripDAO;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VehicleDAOTest {
    private VehicleDAO dao;
    private LocationDAO locationDAO;
    private BookingDAO bookingDAO;
    private TripDAO tripDAO;

    public VehicleDAOTest() {
        dao = new VehicleDAO();
        locationDAO = new LocationDAO();
        bookingDAO = new BookingDAO();
        tripDAO = new TripDAO();
    }

    @BeforeEach
    void setUp() throws SQLException {
        bookingDAO.removeAllBookings();
        tripDAO.removeAllTrips();
        dao.removeAllVehicles();
        locationDAO.removeAllLocations();
    }

    @AfterEach
    void tearDown() throws SQLException {
        bookingDAO.removeAllBookings();
        tripDAO.removeAllTrips();
        dao.removeAllVehicles();
        locationDAO.removeAllLocations();
    }

    @Test
    void insertVehicle() {
        try {
            Location location = locationDAO.addLocation(new Location("TestLocation", "123 Test St", 10));
            Vehicle v = new Vehicle(20, Vehicle.VehicleState.WORKING, location);
            Vehicle found = dao.insertVehicle(v);
            assertNotNull(found);
            assertEquals(20, found.getCapacity());
            assertEquals(Vehicle.VehicleState.WORKING, found.getState());
            assertEquals(location.getName(), found.getLocation().getName());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void findInLocation() {
        try {
            Location location = locationDAO.addLocation(new Location("LocA", "AddrA", 5));
            Vehicle v1 = dao.insertVehicle(new Vehicle(0, 10, Vehicle.VehicleState.WORKING, location));
            Vehicle v2 = dao.insertVehicle(new Vehicle(0, 15, Vehicle.VehicleState.MAINTENANCE, location));
            var vehicles = dao.findInLocation(location);
            assertNotNull(vehicles);
            assertTrue(vehicles.size() >= 2);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void findAvailableInLocation() {
        try {
            Location loc = locationDAO.addLocation(new Location("LocB", "AddrB", 8));
            Vehicle v1 = dao.insertVehicle(new Vehicle( 10, Vehicle.VehicleState.WORKING, loc));
            Vehicle v2 = dao.insertVehicle(new Vehicle( 15, Vehicle.VehicleState.OUT_OF_SERVICE, loc));
            List<Vehicle> available = dao.findAvailableInLocation(loc);
            assertTrue(available.stream().anyMatch(v -> v.getId() == v1.getId()));
            assertFalse(available.stream().anyMatch(v -> v.getId() == v2.getId()));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void updateStatus() {
        try {
            Location location = locationDAO.addLocation(new Location("LocC", "AddrC", 3));
            Vehicle v = dao.insertVehicle(new Vehicle(0, 12, Vehicle.VehicleState.WORKING, location));
            dao.updateVehicleState(v.getId(), Vehicle.VehicleState.MAINTENANCE);
            Vehicle updated = dao.findById(v.getId());
            assertEquals(Vehicle.VehicleState.MAINTENANCE.toString(), updated.getState().toString());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void removeVehicle() {
        try {
            Location location = locationDAO.addLocation(new Location("LocD", "AddrD", 2));
            Vehicle v = dao.insertVehicle(new Vehicle(0, 8, Vehicle.VehicleState.WORKING, location));
            dao.removeVehicle(v.getId());
            Vehicle found = dao.findById(v.getId());
            assertNull(found);
        } catch (Exception e) {
            fail(e);
        }
    }
}