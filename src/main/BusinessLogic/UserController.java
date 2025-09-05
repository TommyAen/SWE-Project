package main.BusinessLogic;

import main.DomainModel.User;
import main.ORM.UserDAO;

import java.sql.SQLException;

public class UserController {

    private UserDAO userDAO;
    private AuthController authController;
    public UserController() {
        userDAO = new UserDAO();
        authController = new AuthController();
    }

    public boolean modifyProfile(){ return false; } // TODO
    public boolean deleteProfile(){ return false; } // TODO
    public User viewProfile(){ return null; } // TODO

    public boolean addLicense(){ return false; } // TODO
    public boolean updateLicense(){ return false; } // TODO
    public boolean removeLicense(){ return false; } // TODO

    public boolean verifyPassword(String password){ return false; } // TODO
    public boolean changePassword(String oldPassword, String newPassword){ return false; }

    public boolean verifyUserRole(User.UserRole role){ return false; } // TODO ?


}

