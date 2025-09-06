package main.DomainModel;

public class Location {
    private int id;
    private String name;
    private String address;
    private int carSpots; // TODO: da mettere nel diagramma delle classi

    public Location() {}

    public Location(int id, String name, String address, int carSpots) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.carSpots = carSpots; // Default car spots // FIXME : gestire carspots in DAO ECC
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getCarSpots() { return carSpots; }
    public void setCarSpots(int carSpots) { this.carSpots = carSpots; }

    @Override
    public String toString() {
        return "Location{id=" + id + ", name='" + name + "', address='" + address + "'}";
    }
}
