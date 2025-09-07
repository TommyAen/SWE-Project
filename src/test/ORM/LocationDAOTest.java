package test.ORM;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import main.DomainModel.Location;
import main.ORM.LocationDAO;
import static org.junit.jupiter.api.Assertions.*;

class LocationDAOTest {


    @BeforeAll
    static void setUp() {
        try {
            LocationDAO setupDAO = new LocationDAO();
            setupDAO.removeAllLocations();
        } catch (Exception e) {
            fail("Setup failed: " + e.getMessage());
        }
    }

    @Test
    void addLocation() {
        try {
            LocationDAO locationDAO = new LocationDAO();
            Location location = locationDAO.addLocation(new Location("Test Location", "123 Test St", 10));
            Location retrievedLocation = locationDAO.findById(location.getId());
            assertNotNull(retrievedLocation, "Location should be added and retrievable.");
            assertEquals("Test Location", retrievedLocation.getName(), "Location name should match.");
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    void findByName() {
        try {
            LocationDAO locationDAO = new LocationDAO();
            Location location = locationDAO.addLocation(new Location(2, "Find Me", "456 Find St", 5));

            Location retrievedLocation = locationDAO.findByName("Find Me");
            assertNotNull(retrievedLocation, "Location should be found by name.");
            assertEquals("Find Me", retrievedLocation.getName(), "Location name should match.");
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    void updateCapacity() {
        try {
            LocationDAO locationDAO = new LocationDAO();
            Location location = locationDAO.addLocation(new Location(3, "Update Capacity", "789 Update St", 20));

            locationDAO.updateCapacity(location.getId(), 15);
            Location updatedLocation = locationDAO.findById(location.getId());
            assertEquals(15, updatedLocation.getCarSpots(), "Location capacity should be updated.");
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    void removeLocation() {
        try {
            LocationDAO locationDAO = new LocationDAO();
            Location location = new Location(4, "Remove Me", "101 Remove St", 8);
            locationDAO.addLocation(location);

            locationDAO.removeLocation(4);
            Location removedLocation = locationDAO.findById(4);
            assertNull(removedLocation, "Location should be removed.");
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}