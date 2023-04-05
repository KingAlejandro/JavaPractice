package org.JavaPractice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;


public class Users {
    private Connection connection;

    public Users(Connection connection) {
        this.connection = connection;
    }

    public void registerUser(String name, String email, String password, double balance) {
        String uuid = UUID.randomUUID().toString();
        String hashedPassword = hashPassword(password);

        String sql = "INSERT INTO users(uuid,name,email,password,balance) VALUES(?,?,?,?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, hashedPassword);
            pstmt.setDouble(5, balance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}