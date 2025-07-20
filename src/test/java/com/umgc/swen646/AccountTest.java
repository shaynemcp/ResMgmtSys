package com.umgc.swen646;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;
    private List<Reservation> reservations;

    @BeforeEach
    void setUp() {
        // Create a list of reservations to associate with the account
        reservations = new ArrayList<>();
        Reservation res1 = new Hotel_Reservation(
                "acc-001", "res-h-001", "1 Hotel St", "PO Box 1", new Date(),
                2, 1, 1, 1, 500, ReservationStatus.CONFIRMED, "Hotel", false);
        Reservation res2 = new Cabin_Reservation(
                "acc-001", "res-c-001", "2 Cabin Rd", "PO Box 1", new Date(),
                4, 4, 2, 1, 1100, ReservationStatus.PENDING, "Cabin", true, false);
        reservations.add(res1);
        reservations.add(res2);

        // Initialize a sample Account object before each test
        account = new Account(
                "acc-001",
                "123 Test Street, Testville",
                "555-0101",
                "test.subject@email.com",
                reservations
        );
    }

    @Test
    @DisplayName("Test constructor for correct field initialization")
    void testConstructorAndGetters() {
        assertAll("Verify that all fields are set correctly by the constructor",
                () -> assertEquals("acc-001", account.getAccountNumber()),
                () -> assertEquals("123 Test Street, Testville", account.getMailingAddress()),
                () -> assertEquals("555-0101", account.getPhoneNumber()),
                () -> assertEquals("test.subject@email.com", account.getEmail()),
                () -> assertNotNull(account.getReservations()),
                () -> assertEquals(2, account.getReservations().size(), "Reservations list should have two items."),
                () ->assertSame(reservations, account.getReservations(), "The reservation list should be the same object.")
        );
    }

    @Test
    @DisplayName("Test setAccountNumber updates the account number")
    void setAccountNumber() {
        account.setAccountNumber("acc-002");
        assertEquals("acc-002", account.getAccountNumber());
    }

    @Test
    @DisplayName("Test setMailingAddress updates the mailing address")
    void setMailingAddress() {
        account.setMailingAddress("456 New Address, New City");
        assertEquals("456 New Address, New City", account.getMailingAddress());
    }

    @Test
    @DisplayName("Test setPhoneNumber updates the phone number")
    void setPhoneNumber() {
        account.setPhoneNumber("555-0202");
        assertEquals("555-0202", account.getPhoneNumber());
    }

    @Test
    @DisplayName("Test setEmail updates the email address")
    void setEmail() {
        account.setEmail("new.email@web.com");
        assertEquals("new.email@web.com", account.getEmail());
    }

    @Test
    @DisplayName("Test setReservations updates the list of reservations")
    void setReservations() {
        List<Reservation> newReservations = new ArrayList<>();
        Reservation newRes = new House_Reservation(1, "acc-001", "res-hs-001", "3 House Ave",
                "PO Box 1", new Date(), 7, 6, 4, 3, 2000,
                ReservationStatus.PENDING, "House", 3);
        newReservations.add(newRes);

        account.setReservations(newReservations);

        assertEquals(1, account.getReservations().size());
        assertEquals("res-hs-001", account.getReservations().getFirst().getReservationNumber());
    }

    @Test
    @DisplayName("Test setReservations with a null value")
    void setReservations_NullList() {
        account.setReservations(null);
        assertNull(account.getReservations(), "Reservations list should be null after setting it to null.");
    }

    @Test
    @DisplayName("Test toString() for correct XML-like format")
    void testToString() {
        String accountString = account.toString();

        assertAll("Verify toString format",
                () -> assertTrue(accountString.contains("<accountNumber>acc-001</accountNumber>")),
                () -> assertTrue(accountString.contains("<mailingAddress>123 Test Street, Testville</mailingAddress>")),
                () -> assertTrue(accountString.contains("<phoneNumber>555-0101</phoneNumber>")),
                () -> assertTrue(accountString.contains("<email>test.subject@email.com</email>")),
                () -> assertTrue(accountString.contains("<reservations>" + reservations.toString() + "</reservations>"))
        );
    }
}
