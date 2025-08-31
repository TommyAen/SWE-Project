package main.DomainModel;

import java.util.List;

public class User {
    public enum UserRole {
        ADMIN, Student
    }

    private int id;
    private String name;
    private String surname; // TODO: da implementare
    private String email;
    private String password = ""; // TODO: secondo me non ha senso tenere la password in chiaro, basta nel database
    private boolean license = false;
    private UserRole role;
    //private List<Booking> bookings; NON MESSO perché almeno la lista dei bookings rimane solo nel database e non c'p rischio di inconsistenza

    public User() {}

    public User(int id, String name, String surname, String email, String password, UserRole role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password; //TODO: valuratre se ha senso
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public List<String> getName() { return List.of(name, surname); }
    public void setName(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean getLicense() { return license; }
    public void setLicense(boolean license) { this.license = license; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

}










