package com.umgc.swen646;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

/**
 * Data Access Object for User operations.
 */
public class UserDAO {

    /**
     * Finds a user by username.
     * @param username the username
     * @return User object or null if not found
     */
    public User findByUsername(String username) {
        String sql = "SELECT id, username, password_hash, mfa_secret, email FROM users WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getLong("id"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("mfa_secret"),
                    rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a new user with hashed password.
     * @param username the username
     * @param plainPassword the plaintext password
     * @param mfaSecret the MFA secret (nullable)
     * @return true if created successfully, false otherwise
     */
    public boolean createUser(String username, String plainPassword, String mfaSecret, String email) {
        String sql = "INSERT INTO users (username, password_hash, mfa_secret, email) VALUES (?, ?, ?, ?)";
        String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(plainPassword, org.mindrot.jbcrypt.BCrypt.gensalt());
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hashed);
            stmt.setString(3, mfaSecret);
            stmt.setString(4, email);
            int rows = stmt.executeUpdate();
            System.out.println("User creation successful: " + rows + " rows affected");
            return rows == 1;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error creating user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Verifies a user's credentials.
     * @param username the username
     * @param plainPassword the plaintext password
     * @return User if credentials are valid, null otherwise
     */
    public User verifyUser(String username, String plainPassword) {
        User user = findByUsername(username);
        if (user != null && BCrypt.checkpw(plainPassword, user.getPasswordHash())) {
            return user;
        }
        return null;
    }
} 