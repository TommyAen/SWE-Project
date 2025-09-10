package test.BusinessLogic;

import main.BusinessLogic.*;
import main.DomainModel.*;
import main.ORM.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripControllerTest {

    @Mock private TripDAO tripDAO;
    @Mock private VehicleController vehicleController;
    @Mock private LocationController locationController;
    @Mock private AuthController authController;
    @Mock private BookingDAO bookingDAO;

    @InjectMocks
    private TripController tripController; // userà il costruttore con dipendenze mockate

    private Location origin;
    private Location destination;
    private Vehicle vehicle;
    private User driver;
    private Trip trip;

    @BeforeEach
    void setUp() throws SQLException {
        origin = new Location("Origin", "Addr1", 5);
        destination = new Location("Destination", "Addr2", 5);
        vehicle = new Vehicle(1, Vehicle.VehicleState.WORKING, origin);
        driver = new User(1, "Driver", "Test", "driver@test.com", "driver", "pwd", User.UserRole.ADMIN);
        trip = new Trip(1, origin, destination, Date.valueOf("2025-09-07"),
                Time.valueOf("10:00:00"), driver, vehicle, Trip.TripState.SCHEDULED);

        lenient().when(locationController.findByName("Origin")).thenReturn(origin);
        lenient().when(locationController.findByName("Destination")).thenReturn(destination);
        lenient().when(vehicleController.listAvailableVehiclesForLocation(origin)).thenReturn(List.of(vehicle));
        lenient().when(authController.getCurrentUser()).thenReturn(driver);
        lenient().when(tripDAO.insertTrip(any(Trip.class))).thenReturn(trip);
        lenient().when(tripDAO.findById(1)).thenReturn(trip);
        lenient().when(tripDAO.getSeatsForTrip(1)).thenReturn(trip.getVehicle().getCapacity());
        lenient().when(bookingDAO.countBookingsForTrip(1)).thenReturn(2);
    }

    @Test
    void testCreateTrip() throws SQLException {
        when(authController.isLoggedIn()).thenReturn(false); // utente autenticato
        Trip created = tripController.createTrip("Origin", "Destination", "2025-09-07", "10:00:00");
        assertNotNull(created);
        assertEquals("Origin", created.getOrigin().getName());
        verify(tripDAO).insertTrip(any(Trip.class));
    }

    @Test
    void testFindById() {
        Trip found = tripController.findById(1);
        assertNotNull(found);
        assertEquals(1, found.getId());
    }

    @Test
    void testCancelTrip() throws SQLException {
        doNothing().when(tripDAO).removeTrip(1);
        tripController.cancelTrip(tripDAO.findById(1));
        verify(tripDAO).removeTrip(1);
    }
}
