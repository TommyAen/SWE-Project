package main.ORM;

import main.DomainModel.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    //private final ConnectionManager cm;

    public UserDAO() {
        //this.cm = new ConnectionManager();
//        try {
//            this.connection = ConnectionManager.getInstance().getConnection();
//        } catch (SQLException | ClassNotFoundException e) {
//            System.err.println("Error: " + e.getMessage());
//        }
    }

    // INSERT Methods
    // TODO: aggiungere controllo su NOT NULL
    private void insertUser(int id, String name, String surname, String email, String password, String license, User.UserRole role) throws SQLException {
        String insertSQL = "INSERT INTO \"User\" (id, name, surname, email, password, role, license) VALUES (?, ?, ?, ?,?, ?, ?)";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL))
        {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, surname);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, password);
            preparedStatement.setString(6, role.toString());
            preparedStatement.setString(7, license);

            preparedStatement.executeUpdate(); // FIXME: gestire eccezioni di violazione vincoli o altro??
        }
    }
    public void insertStudent(int id,String name, String surname, String email,String password, String license) throws SQLException {
        insertUser(id, name, surname, email, password, license,User.UserRole.STUDENT);
    }
    public void insertStudent(int id,String name, String surname, String email, String password) throws SQLException {
        insertStudent(id, name, surname, email, password,null); // for students without license
    }
    public void insertAdmin(int id,String name, String surname, String email, String password) throws SQLException {
        insertUser(id, name, surname, email, password,null, User.UserRole.ADMIN); // Admins don't need license
        // TODO: menziona in relazione che gli admin non hanno license
    }

    public void insertStudent(User student) throws SQLException {
        insertUser(student.getId(), student.getName(), student.getSurname(), student.getEmail(), student.getPassword(), student.getLicense(), student.getRole());
    }
    public void insertAdmin(User admin) throws SQLException {
        insertUser(admin.getId(), admin.getName(), admin.getSurname(), admin.getEmail(), admin.getPassword(), null, User.UserRole.ADMIN);
    }

    public void insertUser(int id, String name, String surname, String email, String password, User.UserRole role) throws SQLException {
        insertUser(id, name, surname, email, password, null, role); // for users without license
    }

    // Read Methods
    public User findById(int id) throws SQLException {
        String selectSQL = "SELECT id, name, id, name, surname, email, password, role, license FROM \"User\" WHERE id = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String surname = resultSet.getString("surname");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String license = resultSet.getString("license");
                    User.UserRole role = User.UserRole.valueOf(resultSet.getString("role"));

                    return new User(id, name, surname, email,password, license, role); //FIXME: metteri costruttore vero
                } else {
                    throw new SQLException("User not found with id: " + id);
                }
            }
        }
    }

    public User findUserByEmail(String email) throws SQLException {
        String selectSQL = "SELECT * FROM \"User\" WHERE email = ?";

        // FIXME: codice un pò duplicato, forse si può fare un metodo privato che prende la query e il parametro
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String surname = resultSet.getString("surname");
                    int id = resultSet.getInt("id");
                    String password = resultSet.getString("password");
                    String license = resultSet.getString("license");
                    User.UserRole role = User.UserRole.valueOf(resultSet.getString("role"));

                    return new User(id, name, surname, email,password, license, role);
                } else {
                    throw new SQLException("User not found with email: " + email);
                }
            }
        }
    }

    // "Get all" methods : FIXME forse alcune da spostare in AdminDAO
    private void getUsersFromQuery(String selectSQL, List<User> userList) throws SQLException { // to avoid code duplication
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)){

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String surname = resultSet.getString("surname");
                    String email = resultSet.getString("email");
                    String license = resultSet.getString("license");
                    String password = resultSet.getString("password");
                    User.UserRole role = User.UserRole.valueOf(resultSet.getString("role"));
                    User user = new User(id, name, surname, email, password, license, role);
                    userList.add(user);
                }
            }
        }
    }

    public List<User> getAllUsers() throws SQLException {
        String selectSQL = "SELECT * FROM \"User\""; //FIXME: sarebbe meglio cambiare * con i nomi degli attributi necessari

        List<User> users = new ArrayList<>();

        getUsersFromQuery(selectSQL, users);
        return users;
    }
    public List<User> getAllStudents() throws SQLException {
        String selectSQL = "SELECT * FROM \"User\" WHERE role = 'STUDENT'";

        List<User> users = new ArrayList<>();

        getUsersFromQuery(selectSQL, users);
        return users;
    }
    public List<User> getAllAdmins() throws SQLException {
        String selectSQL = "SELECT * FROM \"User\" WHERE role = 'ADMIN'";

        List<User> users = new ArrayList<>();

        getUsersFromQuery(selectSQL, users);
        return users;
    }
    public List<User> getAllLicensedStudents() throws SQLException {
        String selectSQL = "SELECT * FROM \"User\" WHERE role = 'STUDENT' AND license IS NOT NULL";

        List<User> users = new ArrayList<>();

        getUsersFromQuery(selectSQL, users);
        return users;
    }
    public List<User> getAllUnlicensedStudents() throws SQLException {
        String selectSQL = "SELECT * FROM \"User\" WHERE role = 'STUDENT' AND license IS NULL";

        List<User> users = new ArrayList<>();

        getUsersFromQuery(selectSQL, users);
        return users;
    }
    public List<User> getAllUsersByRole(String role) throws SQLException {
        return null; // TODO?
    }

    // Update Methods
    private static void updateQuery(int user_id, String newVal, String updateSQL) throws SQLException {
        // query must have two parameters: user_id  and newVAL in this exact order
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL))
        {
            preparedStatement.setString(1, newVal);
            preparedStatement.setInt(2, user_id); //
            preparedStatement.executeUpdate();
        }
    }
    public void updateUserEmail(int user_id, String newEmail) throws SQLException {
        String updateSQL = "UPDATE \"User\" SET email = ? WHERE id = ?";
        updateQuery(user_id, newEmail, updateSQL);
    }
    public void updateUserPassword(int user_id, String newPassword) throws SQLException {
        String updateSQL = "UPDATE \"User\" SET password = ? WHERE id = ?";
        updateQuery(user_id, newPassword, updateSQL);
    }
    public void addLicense(int user_id, String license) throws SQLException {
        String updateSQL = "UPDATE \"User\" SET license = ? WHERE id = ?";
        updateQuery(user_id, license, updateSQL);
    }
    public boolean hasLicense(int user_id) throws SQLException {
         String selectSQL = "SELECT license FROM \"User\" WHERE id = ?";
         try (Connection connection = ConnectionManager.getConnection();
              PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                preparedStatement.setInt(1, user_id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String license = resultSet.getString("license");
                        return license != null;
                    } else {
                        throw new SQLException("User not found with id: " + user_id);
                    }
                }
         }
    }

    // Delete Methods
    public void removeUserByID(int id) throws SQLException { // FIXME: dovrebbe fare cascata su bookings e licenses
        String deleteSQL = "DELETE FROM \"User\" WHERE id = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL))
        {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
    public void removeUserByEmail(String email) throws SQLException { // FIXME: dovrebbe fare cascata su bookings e licenses
        String deleteSQL = "DELETE FROM \"User\" WHERE email = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL))
        {
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
        }
    }
    public void removeLicense(int user_id) throws SQLException {
        String updateSQL = "UPDATE \"User\" SET license = ? WHERE id = ?";
        updateQuery(user_id, null, updateSQL);
    }
}
