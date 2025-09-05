package main.BusinessLogic;

import main.DomainModel.User;
import main.ORM.*;

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

    public boolean removeUser(int userId) { return false; } // TODO
    public boolean revokeLicense(int userId) { return false; } // TODO

    public List<User> getAllUsers() { return null; } // TODO
    public List<User> getAllDrivers() { return null; } // TODO
    public List<User> getAllAdmins() { return null; } // TODO
    public List<User> getAllStudents() { return null; } // TODO


}
