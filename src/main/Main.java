package main;

import main.BusinessLogic.*;
import main.DomainModel.*;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Main class to run the application.
 */
public class Main {

    private AuthController authController;
    private UserController userController;
    private TripController tripController;
    private BookingController bookingController;
    private VehicleController vehicleController;
    private AdminController adminController;
    private ApplicationManager applicationManager;
    private Scanner scanner;

    public Main() {
        applicationManager = new ApplicationManager();
        this.authController = applicationManager.getAuthController();
        this.userController = applicationManager.getUserController();
        this.tripController = applicationManager.getTripController();
        this.bookingController = applicationManager.getBookingController();
        this.vehicleController = applicationManager.getVehicleController();
        this.adminController = applicationManager.getAdminController();
        this.scanner = new Scanner(System.in);
    }

    public void start() throws SQLException {
        System.out.println("=== UNIVERSITY CAR SHARING  ===");
        while (true) {
            try {
                if (!authController.isLoggedIn()) {
                    showMainMenu();
                } else {
                    if (authController.isCurrentUserAdmin()) {
                        //showAdminMenu();
                    } else {
                        showUserMenu();
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void showMainMenu() throws SQLException {
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            int choice = getIntInput();
            switch (choice) {
                case 1:
                    handleLogin();
                    if (authController.isLoggedIn()) return;
                    break;
                case 2:
                    handleRegister();
                    if (authController.isLoggedIn()) return;
                    break;
                case 3:
                    System.out.println("Thanks for using Car Sharing System!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option! Try again.");
            }
        }
    }

    // Helper for integer input
    private int getIntInput() {
        while (true) {
            try {
                String input = scanner.nextLine();
                if (input == null || input.trim().isEmpty()) continue;
                return Integer.parseInt(input.trim());
            } catch (Exception e) {
                System.out.print("Invalid input. Enter a number: ");
            }
        }
    }

    // Helper for string input
    private String getStringInput() {
        String input = scanner.nextLine();
        return (input == null) ? "" : input.trim();
    }

    private void handleLogin() throws SQLException {
        System.out.print("Email: ");
        String email = getStringInput();
        System.out.print("Password: ");
        String password = getStringInput();
        authController.loginByEmail(email, password);
        if (authController.isLoggedIn()) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed. Try again.");
        }
    }

    private void handleRegister() throws SQLException {
        System.out.print("Id: ");
        int id = getIntInput();
        System.out.print("Name: ");
        String name = getStringInput();
        System.out.print("Surname: ");
        String surname = getStringInput();
        System.out.print("Email: ");
        String email = getStringInput();
        System.out.print("Password: ");
        String password = getStringInput();
        System.out.print("License (leave blank if none): ");
        String license = getStringInput();
        User.UserRole role = User.UserRole.STUDENT; // default student
        userController.register(id, name, surname, email, password, license, role);
        if (authController.isLoggedIn()) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed. Try again.");
        }
    }

    private void showUserMenu() throws SQLException {
        while (true) {
            System.out.println("\n=== USER MENU ===");
            System.out.println("1. View Available Trips");
            System.out.println("2. My Bookings");
            System.out.println("3. Book a Trip");
            System.out.println("4. Logout");
            System.out.println("5. View Profile");
            System.out.println("6. Create new Trip (if driver)");
            System.out.print("Choose option: ");
            int choice = getIntInput();
            switch (choice) {
                case 1:
                    System.out.println("Available trips:");
                    tripController.listAllAvailableTrips();
                    break;
                case 2:
                    System.out.println("Your bookings:");
                    bookingController.listUserBookings(authController.getCurrentUserId());
                    break;
                case 3:
                    System.out.print("Trip ID to book: ");
                    int tripId = getIntInput();
                    System.out.println("Booking trip...");
                    bookingController.createBooking(tripId);
                    System.out.println("Booking attempted. Check your bookings for confirmation.");
                    break;
                case 4:
                    authController.logout();
                    System.out.println("Logged out.");
                    return;
                case 5:
                    System.out.println("Your profile:");
                    userController.viewProfile();
                    break;
                case 6:
                    if (userController.hasLicense(authController.getCurrentUserId())) {
                        System.out.print("Origin: ");
                        String origin = getStringInput();
                        System.out.print("Destination: ");
                        String destination = getStringInput();
                        System.out.print("Date (YYYY-MM-DD): ");
                        String date = getStringInput();
                        System.out.print("Time (HH:MM): ");
                        String time = getStringInput();
                        System.out.println("Creating trip...");
                        Trip trip = tripController.createTrip(origin, destination, date, time);
                        if (trip != null) {
                            System.out.println("Trip created: " + trip);
                        } else {
                            System.out.println("Trip creation failed.");
                        }
                    } else {
                        System.out.println("You need a valid driver's license to create a trip.");
                    }
                    break;
                default:
                    System.out.println("Invalid option! Try again.");
            }
        }
    }



    public static void main(String[] args) throws SQLException {
        Main app = new Main();
        app.start();
    }
}
