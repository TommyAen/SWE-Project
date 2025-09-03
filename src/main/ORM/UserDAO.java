package main.ORM;

import main.DomainModel.*;
import java.sql.*;
import java.util.List;

public class UserDAO {

    private final ConnectionManager cm;

    public UserDAO() {
        this.cm = new ConnectionManager();
//        try {
//            this.connection = ConnectionManager.getInstance().getConnection();
//        } catch (SQLException | ClassNotFoundException e) {
//            System.err.println("Error: " + e.getMessage());
//        }
    }

    // Insert Methods
    public void insertUser(String name, String surname, String email, String password, String role) throws SQLException {
        String insertSQL = "INSERT INTO \"User\" (name, surname, email, password, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = cm.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL))
        {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, role);
            preparedStatement.executeUpdate();
        } catch (SQLException e) { // TODO: forse da levare tutti questi catch?
            throw new RuntimeException(e);
        }
    }
    public void insertStudent(String name, String surname, String email, String password) throws SQLException {
        insertUser(name, surname, email, password, "STUDENT");
    }
    public void insertAdmin(String name, String surname, String email, String password) throws SQLException {
        insertUser(name, surname, email, password, "ADMIN");
    }

    // Read Methods
    public User findUserByID(int id) throws SQLException {
        String selectSQL = "SELECT * FROM \"User\" WHERE id = ?";

        try (Connection connection = cm.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(resultSet);
                } else {
                    throw new SQLException("User not found with id: " + id);
                }
            }
        }
    }

    public User findUserByEmail(String email) throws SQLException {
        String selectSQL = "SELECT * FROM \"User\" WHERE email = ?";

        try (Connection connection = cm.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(resultSet);
                } else {
                    throw new SQLException("User not found with email: " + email);
                }
            }
        }
    }

    // "Get all" methods
    public List<User> getAllUsers() throws SQLException {
        return null; // TODO
    }

    public List<User> getAllStudents() throws SQLException {
        return null; // TODO
    }
    public List<User> getAllAdmins() throws SQLException {
        return null; // TODO
    }

    public List<User> getAllLicensedStudents() throws SQLException {
        return null; // TODO
    }
    public List<User> getAllUnlicensedStudents() throws SQLException {
        return null; // TODO
    }
    public List<User> getAllUsersByRole(String role) throws SQLException {
        return null; // TODO
    }

    // Update Methods
    public void updateUserEmail(int id, String newEmail) throws SQLException {
        // TODO
    }
    public void updateUserPassword(int id, String newPassword) throws SQLException {
        // TODO
    }

    // Tabella a parte per License?
    public void addLicense(int id) throws SQLException {
        // TODO
    }
    public void removeLicense(int id) throws SQLException {
        // TODO
    }

    // Delete Methods
    public void removeUserByID(int id) throws SQLException {
        String deleteSQL = "DELETE FROM \"User\" WHERE id = ?";

        try (Connection connection = cm.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL))
        {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeUserByEmail(String email) throws SQLException {
        String deleteSQL = "DELETE FROM \"User\" WHERE email = ?";

        try (Connection connection = cm.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL))
        {
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Additional Methods
    public int getUserPassword(String email) throws SQLException {
        return 0;// TODO
    }

}
