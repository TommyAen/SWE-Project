package test.ORM;


import main.ORM.*;
import org.junit.jupiter.api.*;
import java.sql.SQLException;

public class UserDAOTest {

    private static UserDAO userDAO;

    @BeforeAll
    static void setup() {
        userDAO = new UserDAO();
    } //FIXME: forse va anche un pò popolata la tabella con mocks per fare test


    @Test
    void insertUser() { //this test also test insertStudent and insertAdmin
        try {
            userDAO.insertUser(12345, "Mario", "Rossi", "mariorossi@prova.com", "password123", "STUDENT");
            var user = userDAO.findUserByID(12345);
            Assertions.assertEquals("Mario", user.getName());
            Assertions.assertEquals("Rossi", user.getSurname());

        } catch (SQLException e) {
            Assertions.fail("Insertion failed: " + e.getMessage());
        }
    }

    @Test
    void findUserByID() {
    }

    @Test
    void addLicense() {
    }

    @Test
    void removeLicense() {
    }

    @Test
    void removeUserByID() {
    }

}
