package test.BusinessLogic;

import main.BusinessLogic.AuthController;
import main.BusinessLogic.UserController;
import main.DomainModel.User;
import main.ORM.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;
    private AuthController authController;
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        authController = new AuthController();
        userDAO = new UserDAO();
        userDAO.removeAllUsers();
        userController = new UserController(authController);
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Clean up database after each test
        userDAO.removeAllUsers();

    }

    @Test
    void testRegister() throws SQLException {
        // Test successful registration
        userController.register(1, "John", "Doe", "john.doe@example.com", "password123", "B12345", User.UserRole.STUDENT);
        User registeredUser = userDAO.findUserByEmail("john.doe@example.com");
        assertNotNull(registeredUser);
        assertEquals("John", registeredUser.getName());
        authController.loginById(1, "password123");
        assertTrue(authController.isLoggedIn());
    }

    @Test
    void testDeleteThisProfile() throws SQLException {
        // Register and log in a user
        userController.register(1, "John", "Doe", "john.doe@example.com", "password123", "B12345", User.UserRole.STUDENT);

        // Delete the profile
        userController.deleteThisProfile();

        // FIXME: assertNull(userDAO.findUserByEmail("john.doe@example.com")); non gestisce eccezioni
        assertFalse(authController.isLoggedIn());
    }

    @Test
    void testDeleteProfile() throws SQLException {
        // Register an admin and log in
//        userController.register(1, "Admin", "User", "admin@example.com", "adminpass", "A12345", User.UserRole.ADMIN);
//        authController.loginById(1, "adminpass");

        // Register another user
        userController.register(2, "John", "Doe", "john.doe@example.com", "password123", "B12345", User.UserRole.STUDENT);

        // Delete the second user as admin
        userController.deleteThisProfile();
        assertNull(userDAO.findUserByEmail("john.doe@example.com"));
    }

    @Test
    void testLogin() throws SQLException {
        // Register a user
        userController.register(1, "John", "Doe", "john.doe@example.com", "password123", "B12345", User.UserRole.STUDENT);
        User registeredUser = userDAO.findUserByEmail("john.doe@example.com");
        authController.loginById(1, "password123");
        assertTrue(authController.isLoggedIn());
    }
}