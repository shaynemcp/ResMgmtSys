package com.umgc.swen646;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * Utility class for managing database connections using db.properties.
 */
public class DBUtil {
    private static final String PROPERTIES_FILE = "/db.properties";
    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream input = DBUtil.class.getResourceAsStream(PROPERTIES_FILE)) {
            Properties prop = new Properties();
            if (input == null) {
                throw new RuntimeException("Unable to find " + PROPERTIES_FILE);
            }
            prop.load(input);
            url = prop.getProperty("db.url");
            user = prop.getProperty("db.user");
            password = prop.getProperty("db.password");
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load database properties", ex);
        }
    }

    /**
     * Returns a new database connection.
     * @return Connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
} 