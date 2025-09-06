package main.DomainModel;

import java.time.LocalDate;

public class Booking {
    public enum BookingState {
        PENDING, CONFIRMED, CANCELED
    }
    private int id;
    private User user;
    private BookingState state; //TODO: decidere se levare o no, forse è solo utile nel database?
    private Trip trip;

    // Constructors
    public Booking(User user, BookingState state, Trip trip) {
        this.user = user;
        this.state = state;
        this.trip = trip;
    }

    public Booking(int id, User user, BookingState state, Trip trip) {
        this.id = id;
        this.user = user;
        this.state = state;
        this.trip = trip;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public BookingState getState() { return state; }
    public void setState(BookingState state) { this.state = state; }

    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip = trip; }

    @Override
    public String toString() {
        return "Booking{id=" + id + ", user=" + user +
                ", state=" + state + ", trip=" + trip + '}';
    }
}
