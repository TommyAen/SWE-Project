package main.ORM;

import main.DomainModel.*;
import java.sql.SQLException;
import java.util.List;

public class TripDAO {
    ConnectionManager cm;
    public TripDAO() {
        this.cm = new ConnectionManager();
    }

    // Create Methods
    public void addTrip(Trip trip) throws SQLException {}
    public void addPassengerToTrip(int tripId, int userId) throws SQLException {}
    //TODO: forse non ha senso addPassengere e RemovePassenger qua, magari meglio solo eliminare in booking

    // Read Methods
    public Trip findById(int tripId) throws SQLException { return null; }
    public List<Trip> findByDate(String date) throws SQLException { return null; }
    public List<Trip> findAll() throws SQLException { return null; }
    public List<Trip> findByDriver(int driverId) throws SQLException { return null; }

    public List<Booking> getBookingsForTrip(int tripId) throws SQLException { return null; }
    public String getTripStatus(int tripId) throws SQLException { return null; }
    public int getAvailableSeats(int tripId) throws SQLException { return 0; }
    public String getTripDestination(int tripId) throws SQLException { return null; }
    public String getTripOrigin(int tripId) throws SQLException { return null; }
    public String getTripDate(int tripId) throws SQLException { return null; }
    public String getTripTime(int tripId) throws SQLException { return null; }


    // Update Methods
    public void updateTrip(Trip old_trip, Trip new_trip) throws SQLException {}

    // Delete Methods
    public void cancelTrip(int tripId) throws SQLException {}

    // Other Methods
    public List<Trip> getAllDrivers(int trip_id) throws SQLException { return null; }


}