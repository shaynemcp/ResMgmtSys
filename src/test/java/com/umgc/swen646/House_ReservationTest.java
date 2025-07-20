package com.umgc.swen646;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class House_ReservationTest {

    private House_Reservation houseReservation;
    private final double basePrice = 120.0;

    @BeforeEach
    void setUp() {
        Date startDate = new Date();
        houseReservation = new House_Reservation(
                1,
                "acc-house-123",
                "res-house-456",
                "123 House Lane",
                "PO Box 123",
                startDate,
                5, // nights
                4, // beds
                3, // bedrooms
                2, // bathrooms
                1200, // squareFootage > 900
                ReservationStatus.PENDING,
                "House",
                2  // floors
        );
    }

    @Test
    @DisplayName("Test constructor and getters")
    void testConstructorAndGetters() {
        assertAll("Verify constructor and getters",
                () -> assertEquals(1, houseReservation.getFloors()),
                () -> assertEquals("acc-house-123", houseReservation.getAccountNumber()),
                () -> assertEquals(1200, houseReservation.getSquareFootage()),
                () -> assertEquals(2, houseReservation.getFloors())
        );
    }

    @Test
    @DisplayName("Test the clone() method")
    void testClone() throws CloneNotSupportedException {
        House_Reservation clonedReservation = houseReservation.clone();

        // Verify that it's a new object
        assertNotSame(houseReservation, clonedReservation);

        // Verify that the fields are the same
        assertEquals(houseReservation.getReservationNumber(), clonedReservation.getReservationNumber());
        assertEquals(houseReservation.getFloors(), clonedReservation.getFloors());
        assertEquals(houseReservation.totalPrice(), clonedReservation.totalPrice());
    }

    @Test
    @DisplayName("Test setFloors() method")
    void testSetFloors() {
        houseReservation.setFloors(3);
        assertEquals(3, houseReservation.getFloors());
    }

    @Test
    @DisplayName("Test pricePerNight() with square footage > 900")
    void testPricePerNight_Over900SqFt() {
        // The pricePerNight method is inherited from Reservation
        // It should include the square footage fee
        double sqFtFee = 15.0;
        double expectedPrice = basePrice + sqFtFee;
        assertEquals(expectedPrice, houseReservation.pricePerNight());
    }

    @Test
    @DisplayName("Test pricePerNight() with square footage <= 900")
    void testPricePerNight_Under900SqFt() {
        houseReservation.setSquareFootage(850);
        assertEquals(basePrice, houseReservation.pricePerNight());
    }

    @Test
    @DisplayName("Test totalPrice() calculation")
    void testTotalPrice() {
        double expectedTotalPrice = houseReservation.pricePerNight() * houseReservation.getNights();
        assertEquals(expectedTotalPrice, houseReservation.totalPrice());
    }

    @Test
    @DisplayName("Test toString() method for correct format")
    void testToString() {
        String reservationString = houseReservation.toString();
        assertTrue(reservationString.startsWith("<house>"));
        assertTrue(reservationString.endsWith("</house>"));
        assertTrue(reservationString.contains("<floors>2</floors>"));
        assertTrue(reservationString.contains("<squareFootage>1200.0</squareFootage>"));
    }
}