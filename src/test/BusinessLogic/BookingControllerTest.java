package test.BusinessLogic;

import main.BusinessLogic.BookingController;
import main.BusinessLogic.AuthController;
import main.DomainModel.*;
import main.ORM.BookingDAO;
import main.ORM.TripDAO;
import main.ORM.UserDAO;
import main.ORM.VehicleDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BookingControllerTest {

    private BookingController bookingController;
    private AuthController authController;
    private BookingDAO bookingDAO;
    private TripDAO tripDAO;
    private Trip testTrip;
    private User testUser;

    // TODO: refactor with @Mock

}
