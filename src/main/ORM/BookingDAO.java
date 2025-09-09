package main.ORM;

import main.DomainModel.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    private Connection connection;
    public BookingDAO() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Insert Methods
    public void insertBooking(User user, Trip trip, Booking.BookingState state) throws SQLException {
        String insertSQL = "INSERT INTO booking (user_id, trip, state) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, trip.getId());
            preparedStatement.setString(3, state.toString());

            preparedStatement.executeUpdate();
        }
    }
    public void insertBooking(Booking new_booking) throws SQLException {
        insertBooking(new_booking.getUser(), new_booking.getTrip(), new_booking.getState());
    }

    // Read Methods
    public Booking findBookingByID(int id) throws SQLException {
        String selectSQL = "SELECT user_id, trip, state FROM booking WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    UserDAO userDAO = new UserDAO();
                    User user = userDAO.findById(userId);

                    int tripId = rs.getInt("trip");
                    TripDAO tripDAO = new TripDAO();
                    Trip trip = tripDAO.findById(tripId);

                    Booking.BookingState state = Booking.BookingState.valueOf(rs.getString("state"));

                    return new Booking(id, user, state, trip);
                }
                return null; // TODO: gestire errore
            }
        }
    }


    public List<Booking> getBookingsFromQuery(String selectSQL) {
        List<Booking> bookingList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)){

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int user_id = resultSet.getInt("user_id");
                    UserDAO userDAO = new UserDAO();
                    User user = userDAO.findById(user_id);

                    int trip_id = resultSet.getInt("trip");
                    TripDAO tripDAO = new TripDAO();
                    Trip trip = tripDAO.findById(trip_id);

                    Booking.BookingState state = Booking.BookingState.valueOf(resultSet.getString("state"));

                    Booking booking = new Booking(id, user, state, trip);
                    bookingList.add(booking);
                }
                return bookingList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Booking> findBookingsByUserID(int userId) throws SQLException {
        String selectSQL = "SELECT * FROM booking WHERE user = " + userId;
        return getBookingsFromQuery(selectSQL);
    }

    public List<Booking> findBookingsByTripID(int tripId) throws SQLException {
        String selectSQL = "SELECT * FROM booking WHERE trip = " + tripId;
        return getBookingsFromQuery(selectSQL);
    }

    public int countBookingsForTrip(int tripId) throws SQLException {
        String countSQL = "SELECT COUNT(*) AS total FROM booking WHERE trip = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(countSQL)) {
            preparedStatement.setInt(1, tripId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return the count form "COUNT" query
                }
                return 0;
            }
        }
    }

    // Update Methods
    public void updateBooking(Booking b) throws SQLException {
        // TODO
    }

    // Delete Methods
    public void removeBooking(int id) throws SQLException {
        String deleteSQL = "DELETE FROM booking WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // Utility method to remove all bookings
    public void removeAllBookings() throws SQLException {
        String sql = "DELETE FROM booking";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        }
    }

}