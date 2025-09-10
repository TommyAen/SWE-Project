package main.BusinessLogic;

import main.DomainModel.Booking;
import main.DomainModel.Trip;
import main.ORM.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BookingController {

    private final BookingDAO bookingDAO;
    private final TripController tripController;
    private final AuthController authController;

    public BookingController(AuthController authController) {
        this.bookingDAO = new BookingDAO();
        this.tripController = new TripController(authController);
        this.authController = authController;
    }

    public BookingController(AuthController authController, BookingDAO bookingDAO, TripController tripController) {
        this.bookingDAO = bookingDAO;
        this.tripController = tripController;
        this.authController = authController;
    }


    // Create a new booking
    public void createBooking(int tripID) {
        try {
            if (!authController.isLoggedIn()) {
                System.err.println("User must be logged in to create a booking.");
            }

            Trip trip = tripController.findById(tripID);
            if (tripController.isFull(trip)) {
                System.err.println("Trip is full.");
            }

            Booking newBooking = new Booking(authController.getCurrentUser(), Booking.BookingState.PENDING, trip);
            bookingDAO.insertBooking(newBooking);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a booking by ID
    public boolean cancelBooking(int bookingId) {
        try {
            Booking booking = bookingDAO.findBookingByID(bookingId);
            if (booking == null) {
                System.err.println("Booking not found: " + bookingId);
                return false;
            }

            if (!authController.isLoggedIn() ||
                !(authController.isCurrentUserAdmin() || authController.getCurrentUser().getId() == booking.getUser().getId())) {
                System.err.println("Not authorized to delete this booking.");
                return false;
            }

            booking.setState(Booking.BookingState.CANCELED);
            bookingDAO.updateBooking(booking); // FIXME: così manteniamo lo storico
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeBooking(int bookingId) {
        try {
            if (!authController.isLoggedIn() || !authController.isCurrentUserAdmin()) {
                System.err.println("Not authorized to remove this booking.");
                return;
            }
            bookingDAO.removeBooking(bookingId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Modify a booking
    public boolean modifyBooking(int bookingId, Booking.BookingState newState) {
        try {
            Booking booking = bookingDAO.findBookingByID(bookingId);
            if (booking == null) {
                System.err.println("Booking not found: " + bookingId);
                return false;
            }

            if (!authController.isLoggedIn() ||
                !(authController.isCurrentUserAdmin() || authController.getCurrentUser().getId() == booking.getUser().getId())) {
                System.err.println("Not authorized to modify this booking.");
                return false;
            }

            if (newState != null) booking.setState(newState);
            bookingDAO.updateBooking(booking);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Search for a booking by ID
    public Booking searchBooking(int bookingID) {
        try {
            return bookingDAO.findBookingByID(bookingID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // List bookings for a user
    public void listUserBookings(int userID) {
        try {
            List<Booking> bookings = bookingDAO.findBookingsByUserID(userID);
            if (bookings.isEmpty()) {
                System.out.println("No bookings found for user " + userID);
            } else {
                bookings.forEach(System.out::println);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // List bookings for a trip
    public void listTripBookings(int tripID) {
        try {
            List<Booking> bookings = bookingDAO.findBookingsByTripID(tripID);
            if (bookings.isEmpty()) {
                System.out.println("No bookings found for trip " + tripID);
            } else {
                bookings.forEach(System.out::println);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // View booking details
    public void viewBookingDetails(int bookingID) {
        Booking booking = searchBooking(bookingID);
        if (booking == null) {
            System.out.println("Booking not found: " + bookingID);
        } else {
            System.out.println(booking);
        }
    }

    // Get confirmed bookings for a trip
    public List<Booking> getConfirmedBookings(int tripID) {
        try {
            List<Booking> bookings = bookingDAO.findBookingsByTripID(tripID);
            return bookings.stream()
                           .filter(b -> b.getState() == Booking.BookingState.CONFIRMED)
                           .collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
