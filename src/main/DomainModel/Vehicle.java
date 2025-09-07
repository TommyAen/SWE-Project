package main.DomainModel;

public class Vehicle {
    public enum VehicleState {
        WORKING, MAINTENANCE, OUT_OF_SERVICE
    }

    private int id;
    private int capacity;
    private VehicleState state;
    private Location location;

    // Constructors
    public Vehicle(int id, int capacity, VehicleState state) {
        this.id = id;
        this.capacity = capacity;
        this.state = state;
        this.location = null; // Default location is null
    }
    // constructor for new vehicles (id will be set by the database)
    public Vehicle(int capacity, VehicleState state, Location location) {
        this.id = 0;
        this.capacity = capacity;
        this.state = state;
        this.location = location;
    }

    public Vehicle(int id, int capacity, VehicleState state, Location location) {
        this.id = id;
        this.capacity = capacity;
        this.state = state;
        this.location = location;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public VehicleState getState() { return state; }
    public void setState(VehicleState state) { this.state = state; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    @Override
    public String toString() {
        return "Vehicle{id='" + id + "', capacity=" + capacity +
                ", state=" + state + ", location=" + location + "}";
    }
}
