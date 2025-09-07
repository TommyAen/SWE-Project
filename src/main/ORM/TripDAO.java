package main.ORM;

import main.DomainModel.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripDAO {
    //ConnectionManager cm;
    public TripDAO() {
        //this.cm = new ConnectionManager();
    }

    // Create Methods
    public Trip insertTrip(Location origin, Location destination, Date date, Time time, User driver, Vehicle vehicle) throws SQLException {
        String insertSQL = "INSERT INTO trip (origin, destination, date, time, state, driver, vehicle) VALUES ( ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, origin.getId());
            preparedStatement.setInt(2, destination.getId());
            preparedStatement.setDate(3, date);
            preparedStatement.setTime(4, time);
            preparedStatement.setString(5, Trip.TripState.SCHEDULED.toString());
            preparedStatement.setInt(6, driver.getId());
            preparedStatement.setInt(7, vehicle.getId());
            preparedStatement.executeUpdate(); // FIXME: gestire eccezioni di violazione vincoli o altro??

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) { // id can be generated automatically by the database
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);
                    return new Trip(newId, origin, destination, date, time, driver, vehicle, Trip.TripState.SCHEDULED);
                } else {
                    throw new SQLException("Creating trip failed, no ID obtained.");
                }
            }
        }
    }

    public Trip insertTrip(Trip trip) throws SQLException {
        return insertTrip(trip.getOrigin(), trip.getDestination(), trip.getDate(), trip.getTime(), trip.getDriver(), trip.getVehicle());
    }

    public void addPassengerToTrip(int tripId, int userId) throws SQLException {
        // TODO:
    }
    //TODO: forse non ha senso addPassengere e RemovePassenger qua, magari meglio solo eliminare in booking

    // Read Methods
    private static Trip getTripFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int origin_id = resultSet.getInt("origin");
        int destination_id = resultSet.getInt("destination");
        LocationDAO locationDAO = new LocationDAO();
        Location origin = locationDAO.findById(origin_id);
        Location destination = locationDAO.findById(destination_id);
        Date date = resultSet.getDate("date");
        Time time = resultSet.getTime("time");
        Trip.TripState state = Trip.TripState.valueOf(resultSet.getString("state"));
        int user_id = resultSet.getInt("driver");
        UserDAO userDAO = new UserDAO();
        User driver = userDAO.findById(user_id);
        int vehicleId = resultSet.getInt("vehicle");
        VehicleDAO vehicleDAO = new VehicleDAO();
        Vehicle vehicle = vehicleDAO.findById(vehicleId);
        return new Trip(id, origin, destination, date, time, driver, vehicle, state);
    }

    public Trip findById(int tripId) throws SQLException {
        String selectSQL = "SELECT * FROM trip WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, tripId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return getTripFromResultSet(resultSet);
                } else {
                    return null; // FIXME: gestire No trip found with the given ID
                }
            }
        }
    }

    private List<Trip> getTripsFromQuery(String selectSQL) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Trip> trips = new ArrayList<>();
            while (resultSet.next()) {
                Trip trip = getTripFromResultSet(resultSet);
                trips.add(trip);
            }
            return trips;
        }
    }

    public List<Trip> findAll() throws SQLException {
        String selectSQL = "SELECT * FROM trip ORDER BY date, time";
        return getTripsFromQuery(selectSQL);
    }

    public List<Trip> findAvailableTrips(String fromLocation, String toLocation, Date date) throws SQLException {
        String selectSQL = "SELECT * FROM trip WHERE state = 'SCHEDULED' ORDER BY date, time";
        return getTripsFromQuery(selectSQL);
    }

    public List<Trip> findByDriver(int driverId) throws SQLException {
        String selectSQL = "SELECT * FROM trip WHERE driver_id = " + driverId;
        return getTripsFromQuery(selectSQL);
    }

    public List<Trip> getTripsFromStatus(Trip.TripState state) throws SQLException {
        String selectSQL = "SELECT id FROM trip WHERE state = " + state.toString();
        return getTripsFromQuery(selectSQL);
    }

    public List<Booking> getBookingsForTrip(int tripId) throws SQLException {
        String selectSQL = "SELECT * FROM booking WHERE trip_id = " + tripId;
        BookingDAO bookingDAO = new BookingDAO();
        return bookingDAO.getBookingsFromQuery(selectSQL);
    }


    public void updateTrip(Trip oldTrip, Trip newTrip) throws SQLException {
        String updateSQL = "UPDATE trip SET origin = ?, destination = ?, date = ?, time = ?, driver = ?, vehicle = ?, state = ? WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            //preparedStatement.setInt(1, newTrip.getId());
            preparedStatement.setInt(1, newTrip.getOrigin().getId());
            preparedStatement.setInt(2, newTrip.getDestination().getId());
            preparedStatement.setDate(3, newTrip.getDate());
            preparedStatement.setTime(4, newTrip.getTime());
            preparedStatement.setInt(5, newTrip.getDriver().getId());
            preparedStatement.setInt(6, newTrip.getVehicle().getId());
            preparedStatement.setString(7, newTrip.getState().toString());
            preparedStatement.setInt(8, oldTrip.getId());
            preparedStatement.executeUpdate();
        }
    } // TODO: forse era meglio passare direttamente i parametri che servono, invece di due oggetti Trip

    // Update Methods
    public void updateTripDate(int trip_id, Date newDate) throws SQLException {
        Trip trip = findById(trip_id);
        Trip newTrip = new Trip(trip.getId(), trip.getOrigin(), trip.getDestination(),
                newDate, trip.getTime(), trip.getDriver(), trip.getVehicle(), trip.getState());
        updateTrip(trip, newTrip);
    }
    public void updateTripTime(int trip_id, Time newTime) throws SQLException {
        Trip trip = findById(trip_id);
        Trip newTrip = new Trip(trip.getId(), trip.getOrigin(), trip.getDestination(),
                trip.getDate(), newTime, trip.getDriver(), trip.getVehicle(), trip.getState());
        updateTrip(trip, newTrip);
    }
    public void updateTripStatus(int trip_id, Trip.TripState newState) throws SQLException {
        Trip trip = findById(trip_id);
        Trip newTrip = new Trip(trip.getId(), trip.getOrigin(), trip.getDestination(),
                trip.getDate(), trip.getTime(), trip.getDriver(), trip.getVehicle(), newState);
        updateTrip(trip, newTrip);
    }
    public void updateTripDriver(int trip_id, User newDriver) throws SQLException {
        Trip trip = findById(trip_id);
        Trip newTrip = new Trip(trip.getId(), trip.getOrigin(), trip.getDestination(),
                trip.getDate(), trip.getTime(), newDriver, trip.getVehicle(), trip.getState());
        updateTrip(trip, newTrip);
    }

    public void removeTrip(int tripId) throws SQLException {
        String deleteSQL = "DELETE FROM trip WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, tripId);
            preparedStatement.executeUpdate();
        }
    }

    public int getSeatsForTrip(int tripId) throws SQLException {
        Trip trip = findById(tripId);
        Vehicle vehicle = trip.getVehicle();
        if (vehicle != null) return vehicle.getCapacity();
        else return 0; // TODO: gestire errore
    }

    public void removeAllTrips() throws SQLException {
        String deleteSQL = "DELETE FROM trip";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.executeUpdate();
        }
    }
}

