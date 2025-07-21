package com.umgc.swen646;
import com.umgc.swen646.UserDAO;
import com.umgc.swen646.User;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private static UserDAO userDAO;
    private static final String TEST_USERNAME = "testuser_junit" + System.currentTimeMillis();
    private static final String TEST_PASSWORD = "TestPassword123!";
    private static final String TEST_MFA_SECRET = "TESTSECRET";
    private static final String TEST_EMAIL = "testuser_junit" + System.currentTimeMillis() + "@example.com";

    @BeforeAll
    public static void setup() {
        userDAO = new UserDAO();
    }

    @Test
    @DisplayName("Test user registration (createUser)")
    public void testCreateUser() {
        boolean created = userDAO.createUser(TEST_USERNAME, TEST_PASSWORD, TEST_MFA_SECRET, TEST_EMAIL);
        assertTrue(created, "User should be created successfully");
        User user = userDAO.findByUsername(TEST_USERNAME);
        assertNotNull(user, "User should be found after creation");
        assertEquals(TEST_USERNAME, user.getUsername(), "Username should match");
        assertNotNull(user.getPasswordHash(), "Password hash should not be null");
        assertEquals(TEST_MFA_SECRET, user.getMfaSecret(), "MFA secret should match");
        assertEquals(TEST_EMAIL, user.getEmail(), "Email should match");
    }

    @Test
    @DisplayName("Test user authentication (verifyUser)")
    public void testVerifyUser() {
        // Ensure user exists
        if (userDAO.findByUsername(TEST_USERNAME) == null) {
            userDAO.createUser(TEST_USERNAME, TEST_PASSWORD, TEST_MFA_SECRET, TEST_EMAIL);
        }
        User user = userDAO.verifyUser(TEST_USERNAME, TEST_PASSWORD);
        assertNotNull(user, "User should be authenticated with correct password");
        assertEquals(TEST_USERNAME, user.getUsername(), "Authenticated username should match");
        assertEquals(TEST_EMAIL, user.getEmail(), "Authenticated email should match");
    }

    @AfterAll
    public static void cleanup() {
        // Optionally, delete the test user from the database
        // This requires implementing a deleteUser method in UserDAO
    }
} 