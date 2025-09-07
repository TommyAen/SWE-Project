package test.ORM;

import main.DomainModel.Vehicle;
import main.DomainModel.Location;
import main.ORM.VehicleDAO;
import main.ORM.LocationDAO;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VehicleDAOTest {
        private VehicleDAO dao;

        public  VehicleDAOTest() {
            dao = new VehicleDAO();
        }

        @BeforeAll
        static void setUp() throws SQLException {
            VehicleDAO setupVDAO = new VehicleDAO();
            setupVDAO.removeAllVehicles();
            LocationDAO setupLDAO = new LocationDAO();
            setupLDAO.removeAllLocations();
        }
    @Test
    void insertVehicle() {
        try {
            LocationDAO locationDAO = new LocationDAO();
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
        Location location = new Location("LocA", "AddrA", 5);
        try {
            LocationDAO locationDAO = new LocationDAO();
            location = locationDAO.addLocation(location);
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
        Location loc = locationDAO.addLocation(new Location("LocB", "AddrB", 8));
        Vehicle v1 = dao.insertVehicle(new Vehicle(0, 10, Vehicle.VehicleState.WORKING, loc));
        Vehicle v2 = dao.insertVehicle(new Vehicle(0, 15, Vehicle.VehicleState.OUT_OF_SERVICE, loc));
        List<Vehicle> available = dao.findAvailableInLocation(loc);
        assertTrue(available.contains(v1));
        assertFalse(available.contains(v2));
    }   


    @Test
    void updateStatus() {
        Location location = new Location("LocC", "AddrC", 3);
        try {
            LocationDAO locationDAO = new LocationDAO();
            location = locationDAO.addLocation(location);
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
        Location location = new Location("LocD", "AddrD", 2);
        try {
            LocationDAO locationDAO = new LocationDAO();
            location = locationDAO.addLocation(location);
            Vehicle v = dao.insertVehicle(new Vehicle(0, 8, Vehicle.VehicleState.WORKING, location));
            dao.removeVehicle(v.getId());
            Vehicle found = dao.findById(v.getId());
            assertNull(found);
        } catch (Exception e) {
            fail(e);
        }
    }
}