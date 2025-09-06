package main.DomainModel;

import java.sql.Time;
import java.sql.Date;

public class Trip {

    public enum TripState {
        SCHEDULED,
        ONGOING,
        COMPLETED,
        CANCELED
    }
    private int id;
    private Location origin;
    private Location destination;
    private Date date;
    private Time time;
    private User driver;
    private Vehicle Vehicle;
    private TripState state;


    public Trip(int id, Location origin, Location destination,
                Date date, Time time, User driver_id, Vehicle vehicle_id, TripState state) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.driver = driver_id;
        this.Vehicle = vehicle_id;
        this.state = state;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Location getOrigin() { return origin; }
    public void setOrigin(Location origin) { this.origin = origin;}

    public Location getDestination() { return destination; }
    public void setDestination(Location destination) { this.destination = destination;}

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public Time getTime() { return time; }
    public void setTim(Time time) { this.time = time; }

    public User getDriver() { return driver; }
    public void setDriver(User driver) { this.driver = driver; }

    public Vehicle getVehicle() { return Vehicle; }
    public void setVehicle(Vehicle vehicle) { this.Vehicle = vehicle; }

    public TripState getState() { return state; }
    public void setState(TripState state) { this.state = state; }




    @Override
    public String toString() {
        return "Trip{id=" + id + ", starting from=" + origin +
                ", to=" + destination + ", date=" + date +
                ", time=" + time + ", driver=" + driver + "}";
    }
}