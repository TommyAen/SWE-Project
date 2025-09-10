package main.BusinessLogic;

import main.DomainModel.User;
import main.ORM.UserDAO;

import java.sql.SQLException;

public class UserController {

    private final UserDAO userDAO;
    private AuthController authController;
    public UserController(AuthController authController) {
        userDAO = new UserDAO();
        this.authController = authController;
    }
    public UserController(AuthController authController, UserDAO userDAO) {
        this.userDAO = userDAO;
        this.authController = authController;
    }

    public void register(int user_id,String name, String surname, String email, String password, String license,User.UserRole role) throws SQLException { // riceve i dati da interfaccia utente
        authController.validateCredentials(email, password);

        if(userDAO.findUserByEmail(email) != null) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = new User(user_id, name, surname, email,password, license, role);
        if (user.getRole().equals(User.UserRole.STUDENT)) userDAO.insertStudent(user);
        else if (user.getRole().equals(User.UserRole.ADMIN)) userDAO.insertAdmin(user);
        else throw new IllegalArgumentException("Invalid user role");
        authController.loginById(user_id, password);
    }

    // Profile
    public void deleteThisProfile() throws SQLException {
        if (!authController.isLoggedIn()) {
            throw new IllegalStateException("No user is currently logged in.");
        }
        // TODO: check if user is drivers for some trips
        userDAO.removeUserByEmail(authController.getCurrentUser().getEmail()); // all informations will be deleted cascade (even bookings)
        authController.logout();
    }
    public void deleteProfile(int userId) throws SQLException {
        if (!authController.isCurrentUserAdmin()) {
            throw new IllegalStateException("Current user is not logged in or not an admin.");
        }
        userDAO.removeUserByID(userId); // TODO: check if user is drivers for some trips
    }

    public void viewProfile(){
        System.out.print(authController.getCurrentUser());
    }
    public boolean hasLicense(int user_id) throws SQLException {
        return userDAO.hasLicense(user_id);
        }
    public void addLicense(String license_num) throws SQLException {
        userDAO.addLicense(authController.getCurrentUser().getId(), license_num);
    }
    public void removeLicense() throws SQLException {
        userDAO.removeLicense(authController.getCurrentUser().getId());
    }

    public boolean changePassword(String oldPassword, String newPassword){
        // TODO: validazione password?
        if (!authController.isLoggedIn()) {
            throw new IllegalStateException("No user is currently logged in.");
        }
        if (!authController.getCurrentUser().getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Old password does not match.");
        }
        try {
            userDAO.updateUserPassword(authController.getCurrentUser().getId(), newPassword);
            authController.getCurrentUser().setPassword(newPassword);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

