package org.JavaPractice;

import java.util.UUID;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;


public class Users {
    private Connection connection;

    public Users(Connection connection) {
        this.connection = connection;
    }

    public User getUserById(String userID) throws SQLException {
        String query = "SELECT * FROM users WHERE userID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String uuid = rs.getString("userID");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    double balance = rs.getDouble("balance");
                    String type = rs.getString("type");
                    return new User(uuid, name, email, balance, type);
                } else {
                    return null;
                }
            }
        }
    }

    public User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (BCrypt.checkpw(password, storedHash)) {
                    String uuid = rs.getString("userID");
                    String name = rs.getString("name");
                    double balance = rs.getDouble("balance");
                    String type = rs.getString("type");
                    return new User(uuid, name, email, balance, type);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public boolean registerUser(String name, String email, String password, double balance, String type) {
        String uuid = UUID.randomUUID().toString();
        String hashedPassword = hashPassword(password);

        String sql = "INSERT INTO users(userID,name,email,password,balance,type) VALUES(?,?,?,?,?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, hashedPassword);
            pstmt.setDouble(5, balance);
            pstmt.setString(6, type);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void updateUser(User user) {
        String sql = "UPDATE users SET balance = ? WHERE userID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, user.getBalance());
            pstmt.setString(2, user.getUuid());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}