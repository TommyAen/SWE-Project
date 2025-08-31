package main.DomainModel;

import java.time.LocalDate;

public class Booking {
    public enum BookingState {
        CONFIRMED, CANCELED
    }

    private int id;
    private User user;
    private BookingState state; //TODO: decidere se levare o no, forse è solo utile nel database?
    private LocalDate date;
    private Trip trip;

    // Constructors
    public Booking() {}

    public Booking(int id, User user, BookingState state, LocalDate date, Trip trip) {
        this.id = id;
        this.user = user;
        this.state = state;
        this.date = date;
        this.trip = trip;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public BookingState getState() { return state; }
    public void setState(BookingState state) { this.state = state; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip = trip; }

    /*
    // Business methods
    public void confirm() {
        this.state = BookingState.CONFERMATA;
    }

    public void cancel() {
        this.state = BookingState.ANNULLATA;
    }

    public boolean isConfirmed() {
        return state == BookingState.CONFERMATA;
    }

     */
    @Override
    public String toString() {
        return "Booking{id=" + id + ", user=" + user +
                ", state=" + state + ", date=" + date + "}";
    }
}
