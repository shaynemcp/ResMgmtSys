package com.umgc.swen646;

/**
 * Represents a user in the system for authentication and MFA.
 */
public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private String mfaSecret;
    private String email;

    /**
     * No-argument constructor.
     */
    public User() {}

    /**
     * Parameterized constructor.
     * @param id User ID
     * @param username Username
     * @param passwordHash Hashed password
     * @param mfaSecret MFA secret for TOTP
     */
    public User(Long id, String username, String passwordHash, String mfaSecret, String email) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.mfaSecret = mfaSecret;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getMfaSecret() {
        return mfaSecret;
    }

    public void setMfaSecret(String mfaSecret) {
        this.mfaSecret = mfaSecret;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
} 