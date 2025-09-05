package main.BusinessLogic;

import main.DomainModel.Booking;
import main.ORM.*;

import java.util.List;

public class BookingController {

    private BookingDAO bookingDAO;
    private TripDAO tripDAO;
    private AuthController authController;

    public BookingController(AuthController authController) {
        this.bookingDAO = new BookingDAO();
        this.tripDAO = new TripDAO();
        this.authController = authController;
    }

    public Booking buildBooking(int tripID) {
        return null;
    }

    public boolean addBooking(Booking booking) {return false;}
    public boolean deleteBooking(Booking booking) {return false;}
    public boolean modifyBooking(Booking booking) {return false;}

    public Booking searchBooking(int bookingID) {return null; }

    public boolean listUserBookings(int userID) {return false; }
    public boolean listTripBookings(int tripID) {return false; }

    public void viewBookingDetails(int bookingID) {}

    public List<Booking> getConfirmedBookings(int tripID) {return null; }
}
