package test.BusinessLogic;

import main.BusinessLogic.BookingController;
import main.BusinessLogic.AuthController;
import main.BusinessLogic.TripController;
import main.DomainModel.*;
import main.ORM.BookingDAO;
import main.ORM.TripDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingDAO bookingDAO;
    @Mock
    private TripDAO tripDAO;
    @Mock
    private AuthController authController;
    @Mock
    private TripController tripController;
    @InjectMocks
    private BookingController bookingController;

    private Trip testTrip;
    private User testUser;

    @BeforeEach
    void setUp() throws SQLException {
        testUser = new User(1, "Mario", "Rossi", "mario@unifi.it", "pass", "LIC123", User.UserRole.STUDENT);
        Location from = mock(Location.class);
        Location to = mock(Location.class);
        Vehicle vehicle = mock(Vehicle.class);
        java.sql.Date date = java.sql.Date.valueOf("2025-09-10");
        java.sql.Time time = java.sql.Time.valueOf("10:00:00");
        testTrip = new Trip( 29,from, to, date, time, testUser, vehicle, Trip.TripState.SCHEDULED);when(authController.isLoggedIn()).thenReturn(true);
        when(authController.getCurrentUser()).thenReturn(testUser);
        when(tripController.findById(testTrip.getId())).thenReturn(testTrip);
        when(tripController.isFull(testTrip)).thenReturn(false);
    }

    @AfterEach
    void tearDown() {
        // No DB, nothing to clean
    }

    @Test
    void testBookTripFull() throws SQLException {
        // Trip is full
        when(tripController.isFull(testTrip)).thenReturn(true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setErr(new PrintStream(out));
        bookingController.createBooking(testTrip.getId());
        System.setErr(System.err);
        String output = out.toString();
        assertTrue(output.contains("Trip is full"));
        verify(bookingDAO, never()).insertBooking(any());
    }

    @Test
    void testBookCancel() throws Exception {
        // Book and then cancel
        Booking booking = new Booking(testUser, Booking.BookingState.PENDING, testTrip);
        booking.setId(1);

        when(bookingDAO.findBookingByID(booking.getId())).thenReturn(booking);
        when(authController.isCurrentUserAdmin()).thenReturn(true);
        // Book
        bookingController.createBooking(testTrip.getId());
        verify(bookingDAO).insertBooking(any());
        // Cancel
        boolean cancelled = bookingController.cancelBooking(booking.getId());
        verify(bookingDAO).updateBooking(argThat(b -> b.getState() == Booking.BookingState.CANCELED));
        assertTrue(cancelled);
    }

}
