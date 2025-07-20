package com.umgc.swen646;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Simple test to verify database connection.
 */
public class DBConnectionTest {
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        
        try {
            Connection conn = DBUtil.getConnection();
            System.out.println("✅ Database connection successful!");
            System.out.println("Database: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("Version: " + conn.getMetaData().getDatabaseProductVersion());
            conn.close();
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed:");
            System.err.println("Error: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
    }
} 