package test.ORM;


import main.DomainModel.User;
import main.ORM.UserDAO;
import org.junit.jupiter.api.*;
import java.sql.SQLException;

public class UserDAOTest {

    private static UserDAO userDAO;

    @BeforeAll
    static void setup() {
        userDAO = new UserDAO();
    }

    @AfterAll
    static void teardown() {
        // Remove all users from the table after tests
        try {
            userDAO.removeAllUsers();
        } catch (Exception ignored) {}
    }


    // Unit tests for UserDAO methods
    // Note: These tests assume a test database is set up and accessible.

    @Test
    void insertUser() {
        try {
            userDAO.insertStudent(12345, "Mario", "Rossi", "mariorossi@prova.com", "password123");
            var user = userDAO.findById(12345);
            Assertions.assertEquals("Mario", user.getName());
            Assertions.assertEquals("Rossi", user.getSurname());
        } catch (SQLException e) {
            Assertions.fail("Insertion failed: " + e.getMessage());
        } finally {
            try {
                userDAO.removeUserByID(12345);
            } catch (SQLException ignored) {}
        }
    }

    @Test
    void updateUserEmail() {
        try {
            userDAO.insertStudent(23456, "Luigi", "Bianchi", "luigibianchi@prova.com", "password456");
            userDAO.updateUserEmail(23456, "luigi.new@prova.com");
            var user = userDAO.findById(23456);
            Assertions.assertEquals("luigi.new@prova.com", user.getEmail());
        } catch (SQLException e) {
            Assertions.fail("Update failed: " + e.getMessage());
        } finally {
            try {
                userDAO.removeUserByID(23456);
            } catch (SQLException ignored) {}
        }
    }

    @Test
    void findUserByID() {
        try {
            User.UserRole role = User.UserRole.ADMIN;
            userDAO.insertUser(45678, "Anna", "Neri", "annaneri@prova.com", "password000",role);
            var user = userDAO.findById(45678);
            Assertions.assertEquals(45678, user.getId());
            Assertions.assertEquals("Anna", user.getName());
            Assertions.assertEquals("Neri", user.getSurname());
            Assertions.assertEquals("annaneri@prova.com", user.getEmail());
            Assertions.assertEquals(role, user.getRole());
        } catch (SQLException e) {
            Assertions.fail("Find by ID failed: " + e.getMessage());
        } finally {
            try {
                userDAO.removeUserByID(45678);
            } catch (SQLException ignored) {}
        }
    }

    @Test
    void addLicense() {
        try {
            userDAO.insertStudent(56789, "Paolo", "Blu", "paoloblu@prova.com", "password111", null);
            userDAO.addLicense(56789, "LICENSE123");
            var user = userDAO.findById(56789);
            // If User class has getLicense(), use it. Otherwise, this is a placeholder:
            Assertions.assertEquals("LICENSE123", user.getLicense());
        } catch (SQLException e) {
            Assertions.fail("Add license failed: " + e.getMessage());
        } finally {
            try {
                userDAO.removeUserByID(56789);
            } catch (SQLException ignored) {}
        }
    }

    @Test
    void removeLicense() {
        try {
            userDAO.insertStudent(67890, "Sara", "Gialli", "saragialli@prova.com", "password222", "LICENSE456");
            userDAO.removeLicense(67890);
            Assertions.assertFalse(userDAO.hasLicense(67890));
        } catch (SQLException e) {
            Assertions.fail("Remove license failed: " + e.getMessage());
        } finally {
            try {
                userDAO.removeUserByID(67890);
            } catch (SQLException ignored) {}
        }
    }

    @Test
    void removeUserByID() {
        try {
            userDAO.insertStudent(34567, "Giovanni", "Verdi", "giovanniverdi@prova.com", "password789");
            userDAO.removeUserByID(34567);
            Assertions.assertNull(userDAO.findById(34567)); // FIXME: ??
        } catch (SQLException e) {
            Assertions.fail("Delete failed: " + e.getMessage());
        }
    }
}
