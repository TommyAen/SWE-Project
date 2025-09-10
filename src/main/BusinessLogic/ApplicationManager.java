package main.BusinessLogic;

import main.ORM.*;

public class ApplicationManager {

    // DAOs (single instances)
    private final UserDAO userDAO;
    private final BookingDAO bookingDAO;
    private final TripDAO tripDAO;
    private final VehicleDAO vehicleDAO;
    private final LocationDAO locationDAO;

    // Services/Controllers
    private final AuthController authController;
    private final UserController userController;
    private final TripController tripController;
    private final BookingController bookingController;
    private final VehicleController vehicleController;
    private final LocationController locationController;
    private final AdminController adminController;

    public ApplicationManager() {
        // Initialize DAOs
        this.userDAO = new UserDAO();
        this.bookingDAO = new BookingDAO();
        this.tripDAO = new TripDAO();
        this.vehicleDAO = new VehicleDAO();
        this.locationDAO = new LocationDAO();

        // Initialize Controllers/Services with DAOs
        this.authController = new AuthController(userDAO);
        this.userController = new UserController(authController, userDAO);
        this.locationController = new LocationController(locationDAO, authController);
        this.vehicleController = new VehicleController(authController, vehicleDAO, locationController);
        this.tripController = new TripController(tripDAO, vehicleController, locationController, authController);
        this.bookingController = new BookingController(authController, bookingDAO, tripController);
        this.adminController = new AdminController(authController, userDAO, bookingController, tripController);
    }
    // Getters for Controllers/Services

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public BookingDAO getBookingDAO() {
        return bookingDAO;
    }

    public TripDAO getTripDAO() {
        return tripDAO;
    }

    public VehicleDAO getVehicleDAO() {
        return vehicleDAO;
    }

    public LocationDAO getLocationDAO() {
        return locationDAO;
    }

    public AuthController getAuthController() {
        return authController;
    }

    public UserController getUserController() {
        return userController;
    }

    public TripController getTripController() {
        return tripController;
    }

    public BookingController getBookingController() {
        return bookingController;
    }

    public VehicleController getVehicleController() {
        return vehicleController;
    }

    public LocationController getLocationController() {
        return locationController;
    }

    public AdminController getAdminController() {
        return adminController;
    }
}
