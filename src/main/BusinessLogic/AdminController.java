package main.BusinessLogic;

import main.DomainModel.User;
import main.ORM.*;

import java.sql.SQLException;
import java.util.List;

public class AdminController {

    private UserDAO userDAO;
    private BookingDAO bookingDAO;
    private TripDAO tripDAO;
    private AuthController authController;

    public AdminController(AuthController authController) {
        this.userDAO = new UserDAO();
        this.bookingDAO = new BookingDAO();
        this.tripDAO = new TripDAO();
        this.authController = authController;
    }

    public void removeUser(int userId) throws SQLException {
        if (authController.isLoggedIn() && authController.getCurrentUser().isAdmin()) {
            userDAO.removeUserByID(userId);
        }
        else {
            throw new IllegalStateException("Current user is not logged in or not an admin.");
        }
    }
    public void revokeLicense(int userId) throws SQLException {
        if (authController.isLoggedIn() && authController.getCurrentUser().isAdmin()) {
            userDAO.removeLicense(userId);
        }
    } // TODO
    public boolean removeTrip(int tripId) { return false; } // TODO
    public boolean removeBooking(int bookingId) { return false; } // TODO

    public List<User> getAllUsers() throws SQLException {
        if (authController.isLoggedIn() && authController.getCurrentUser().isAdmin()) {
            return userDAO.getAllUsers();
        } else {
            throw new IllegalStateException("Current user is not logged in or not an admin.");
        }
    } // TODO
    public List<User> getAllDrivers() { return null; } // TODO
    public List<User> getAllAdmins() { return null; } // TODO
    public List<User> getAllStudents() { return null; } // TODO


}
