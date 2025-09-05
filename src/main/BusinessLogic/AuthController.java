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

    public boolean register(){
        return false; // TODO
    }
    public boolean login(String username, String password) throws SQLException { return false; } // TODO

    public void logout(){} // TODO

    public boolean isLoggedIn(){ //TODO: è un metodo di UserController?
        return currentUser != null;
    }
    public boolean isCurrentUserAdmin() {
        return isLoggedIn() && currentUser.isAdmin();
    }
    public boolean isCurrentUserStudent() {
        return isLoggedIn() && currentUser.isStudent();
    }
    public User getCurrentUser() {
        return currentUser;
    }


    // FIXME: Validation forse da spostare in controller a parte?
    public boolean validatePassword(String password) {
        // TODO: implement password validation logic (e.g., length, special characters, etc.)
        return false;
    }
    public boolean validateEmail(String email) {
        // TODO: implement email validation logic (e.g., regex pattern)
        return false;
    }

    //TODO: decidere se tenere hash di password o password in chiaro

}
