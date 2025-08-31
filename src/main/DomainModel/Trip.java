package main.DomainModel;

import main.DomainModel.*;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

public class Trip {

    private int id;
    private Location initialLocation;
    private Location finalLocation;
    private LocalDate date;
    private LocalTime time;
    private User driver;
    private Vehicle vehicle;
    private List<Booking> bookings;

    // Constructors
    public Trip() {
        this.bookings = new ArrayList<>();
    }

    public Trip(int id, Location initialLocation, Location finalLocation,
                LocalDate date, LocalTime time, User driver, Vehicle vehicle) {
        this.id = id;
        this.initialLocation = initialLocation;
        this.finalLocation = finalLocation;
        this.date = date;
        this.time = time;
        this.driver = driver;
        this.vehicle = vehicle;
        this.bookings = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Location getInitialLocation() { return initialLocation; }
    public void setInitialLocation(Location initialLocation) {
        this.initialLocation = initialLocation;
    }

    public Location getFinalLocation() { return finalLocation; }
    public void setFinalLocation(Location finalLocation) {
        this.finalLocation = finalLocation;
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public User getDriver() { return driver; }
    public void setDriver(User driver) { this.driver = driver; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    // Additional Methods
    public int getAvailableSeats() {
        return vehicle.getCapacity() - bookings.size();
    }
    public boolean isFull() {
        return bookings.size() >= vehicle.getCapacity();
    }

    @Override
    public String toString() {
        return "Trip{id=" + id + ", starting from=" + initialLocation +
                ", to=" + finalLocation + ", date=" + date +
                ", time=" + time + ", driver=" + driver + "}";
    }
}