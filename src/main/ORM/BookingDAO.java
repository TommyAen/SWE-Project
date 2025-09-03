package main.ORM;

import main.DomainModel.*;
import java.sql.*;
import java.util.List;

public class BookingDAO {
    private ConnectionManager cm;

    public BookingDAO() {
        cm = new ConnectionManager();
    }

    // Insert Methods
    public void insertBooking(int userId, int locationId, Timestamp startTime, Timestamp endTime){}
    public void insertBooking(Booking b) throws SQLException {}

    // Read Methods
    public Booking findBookingByID(int id) throws SQLException { return null; }
    public List<Booking> findBookingsByUserID(int userId) throws SQLException { return null; }
    public List<Booking> findBookingsByLocationID(int locationId) throws SQLException { return null; }
    public List<Booking> findAllBookings() throws SQLException { return null;}

    // Update Methods
    public void updateBooking(Booking b) throws SQLException {}


    // Delete Methods




}
