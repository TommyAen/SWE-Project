package test.BusinessLogic;

import main.BusinessLogic.LocationController;
import main.BusinessLogic.AuthController;
import main.DomainModel.Location;
import main.ORM.LocationDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocaitonControllerTest {
    @Mock
    private LocationDAO locationDAO;
    @Mock
    private AuthController authController;
    @InjectMocks
    private LocationController locationController;

    private Location testLocation;

    @BeforeEach
    void setUp() {
        testLocation = new Location(1, "Biblioteca", "Via Roma 1", 10);
    }

    @Test
    void testAddLocation_Admin() throws SQLException {
        when(authController.isCurrentUserAdmin()).thenReturn(true);
        when(locationDAO.addLocation(any(Location.class))).thenReturn(testLocation);
        locationController.addLocation(testLocation.getId(), testLocation.getName(), testLocation.getAddress(), testLocation.getCarSpots());
        verify(locationDAO).addLocation(any(Location.class));
    }

    @Test
    void testAddLocation_NotAdmin() throws SQLException {
        when(authController.isCurrentUserAdmin()).thenReturn(false);
        assertThrows(SecurityException.class, () -> locationController.addLocation(testLocation.getId(), testLocation.getName(), testLocation.getAddress(), testLocation.getCarSpots()));
        verify(locationDAO, never()).addLocation(any(Location.class));
    }

    @Test
    void testFindLocationById() throws SQLException {
        when(locationDAO.findById(testLocation.getId())).thenReturn(testLocation);
        Location found = locationController.findLocationById(testLocation.getId());
        assertEquals(testLocation, found);
        verify(locationDAO).findById(testLocation.getId());
    }

    @Test
    void testUpdateLocationCapacity_Admin() throws SQLException {
        when(authController.isCurrentUserAdmin()).thenReturn(true);
        locationController.updateLocationCapacity(testLocation.getId(), 20);
        verify(locationDAO).updateCapacity(testLocation.getId(), 20);
    }
}
