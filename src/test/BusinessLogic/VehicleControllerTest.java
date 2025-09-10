package test.BusinessLogic;

import main.BusinessLogic.VehicleController;
import main.BusinessLogic.AuthController;
import main.BusinessLogic.LocationController;
import main.DomainModel.Vehicle;
import main.DomainModel.Location;
import main.ORM.VehicleDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleControllerTest {
    @Mock
    private VehicleDAO vehicleDAO;
    @Mock
    private LocationController locationController;
    @Mock
    private AuthController authController;
    @InjectMocks
    private VehicleController vehicleController;

    private Vehicle testVehicle;
    private Location testLocation;

    @BeforeEach
    void setUp() {
        testLocation = mock(Location.class);
        testVehicle = new Vehicle(1, 4, Vehicle.VehicleState.WORKING, testLocation);
    }

    @Test
    void testAddVehicle_Admin() throws SQLException {
        when(authController.isCurrentUserAdmin()).thenReturn(true);
        doNothing().when(vehicleDAO).insertVehicle(testVehicle);
        vehicleController.addVehicle(testVehicle);
        verify(vehicleDAO).insertVehicle(testVehicle);
    }

    @Test
    void testAddVehicle_NotAdmin() throws SQLException {
        when(authController.isCurrentUserAdmin()).thenReturn(false);
        assertThrows(SecurityException.class, () -> vehicleController.addVehicle(testVehicle));
        verify(vehicleDAO, never()).insertVehicle(any());
    }

    @Test
    void testModifyVehicle_Admin() throws SQLException {
        when(authController.isCurrentUserAdmin()).thenReturn(true);
        Vehicle newVehicle = new Vehicle(1, 5, Vehicle.VehicleState.WORKING, testLocation);
        doNothing().when(vehicleDAO).updateVehicle(testVehicle.getId(), newVehicle);
        vehicleController.modifyVehicle(testVehicle, newVehicle);
        verify(vehicleDAO).updateVehicle(testVehicle.getId(), newVehicle);
    }

    @Test
    void testRemoveVehicle_Admin() throws SQLException {
        when(authController.isCurrentUserAdmin()).thenReturn(true);
        doNothing().when(vehicleDAO).removeVehicle(testVehicle.getId());
        boolean result = vehicleController.removeVehicle(testVehicle.getId());
        verify(vehicleDAO).removeVehicle(testVehicle.getId());
        assertTrue(result);
    }

    @Test
    void testRemoveVehicle_NotAdmin() throws SQLException {
        when(authController.isCurrentUserAdmin()).thenReturn(false);
        assertThrows(SecurityException.class, () -> vehicleController.removeVehicle(testVehicle.getId()));
        verify(vehicleDAO, never()).removeVehicle(anyInt());
    }

    @Test
    void testViewVehicleDetails_Admin() throws SQLException {
        when(authController.isCurrentUserAdmin()).thenReturn(true);
        when(vehicleDAO.findById(testVehicle.getId())).thenReturn(testVehicle);
        vehicleController.viewVehicleDetails(testVehicle.getId());
        verify(vehicleDAO).findById(testVehicle.getId());
    }

    @Test
    void testModifyStatus() throws SQLException {
        when(vehicleDAO.findById(testVehicle.getId())).thenReturn(testVehicle);
        Vehicle.VehicleState newState = Vehicle.VehicleState.MAINTENANCE;
        Vehicle updatedVehicle = new Vehicle(testVehicle.getId(), testVehicle.getCapacity(), newState, testLocation);
        doNothing().when(vehicleDAO).updateVehicle(testVehicle.getId(), updatedVehicle);
        boolean result = vehicleController.modifyStatus(testVehicle.getId(), newState);
        verify(vehicleDAO).updateVehicle(eq(testVehicle.getId()), argThat(v -> v.getState() == newState));
        assertTrue(result);
    }

    @Test
    void testListAvailableVehiclesForLocation() throws SQLException {
        List<Vehicle> vehicles = Arrays.asList(testVehicle);
        when(vehicleDAO.findAvailableInLocation(testLocation)).thenReturn(vehicles);
        List<Vehicle> result = vehicleController.listAvailableVehiclesForLocation(testLocation);
        assertEquals(vehicles, result);
        verify(vehicleDAO).findAvailableInLocation(testLocation);
    }

    @Test
    void testIsVehicleAvailable() throws SQLException {
        when(vehicleDAO.isVehicleAvailable(testVehicle.getId())).thenReturn(true);
        boolean result = vehicleController.isVehicleAvailable(testVehicle.getId());
        assertTrue(result);
        verify(vehicleDAO).isVehicleAvailable(testVehicle.getId());
    }
}
