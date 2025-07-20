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
        String sql = "SELECT id, username, password_hash, mfa_secret FROM users WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getLong("id"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("mfa_secret")
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
    public boolean createUser(String username, String plainPassword, String mfaSecret) {
        String sql = "INSERT INTO users (username, password_hash, mfa_secret) VALUES (?, ?, ?)";
        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hashed);
            stmt.setString(3, mfaSecret);
            int rows = stmt.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
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