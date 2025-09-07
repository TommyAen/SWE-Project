package main.BusinessLogic;


import main.DomainModel.User;
import main.ORM.UserDAO;

import java.sql.SQLException;

public class AuthController {

    private UserDAO userDAO;
    private User currentUser;

    public AuthController() {
        userDAO = new UserDAO();
        currentUser = null;
    }
    public AuthController(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.currentUser = null;
    }

    public void loginById(int id, String password) throws SQLException {
        User user = userDAO.findById(id);
        login(user, password);
    }
    public void loginByEmail(String email, String password) throws SQLException {
        User user = userDAO.findUserByEmail(email);
        login(user, password);
    }
    public void login(User user, String password) throws SQLException {
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
        }
        else {
            throw new IllegalArgumentException("Invalid user or password"); // TODO: scegliere/creare eccezione apposta?
        }
    }
    public boolean isLoggedIn(){
        return currentUser != null;
    }

    public void logout(){
        if (!isLoggedIn()) {
            throw new IllegalStateException("No user is currently logged in.");
        }
        else currentUser = null;
    }

    public boolean isCurrentUserAdmin() {
        return isLoggedIn() && currentUser.isAdmin();
    }
    public boolean isCurrentUserStudent() {
        return isLoggedIn() && currentUser.isStudent();
    }
    public User getCurrentUser() {
        if (!isLoggedIn()) {
            throw new IllegalStateException("No user is currently logged in.");
        }
        else return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("No user is currently logged in.");
        }
        this.currentUser = currentUser;
    }

    // FIXME: Validation forse da spostare in controller a parte?
    public void validateCredentials(String email, String password) {
        if (!validateEmail(email) || !validatePassword(password)) {
            throw new IllegalArgumentException("email or password not legal"); // FIXME: non so ne va bene
        }
        else {
            System.out.println("Credentials validated");
        }
    }
    public boolean validatePassword(String password) {
        // TODO: implement password validation logic (e.g., length, special characters, etc.)
        return true;
    }
    public boolean validateEmail(String email) {
        // TODO: implement email validation logic (e.g., regex pattern)
        return true;
    }
}

