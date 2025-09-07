package main.DomainModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class User {
    public enum UserRole {
        ADMIN, STUDENT
    }

    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String license;
    private UserRole role;

    public User(int id, String name, String surname, String email, String pwd, String license_num, UserRole role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = pwd;
        this.license = license_num;
        this.role = role;
    }
    // without License
    public User(int id, String name, String surname, String email, String pwd, UserRole role) {
        this(id, name, surname, email, pwd, null, role);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public List<String> getFullName() { return List.of(name, surname); } // FIXME: ??
    public String getName(){ return name; }
    public String getSurname(){ return surname; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setFullName(String name, String surname) {
        setName(name);
        setSurname(surname);
    }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getLicense() { return license; }
    public void setLicense(String license) { this.license = license; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public boolean isStudent() {
        return role == UserRole.STUDENT;
    }

    public boolean hasLicense() {
        return license!=null;
    }
}










