package com.umgc.swen646;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class Hotel_ReservationTest {

    private Hotel_Reservation reservationWithKitchenette;
    private Hotel_Reservation reservationWithoutKitchenette;
    private Date startDate;

    // Constants from the Reservation and Hotel_Reservation classes
    private final double basePrice = 120.0;
    private final double sqFtFee = 15.0;
    private final double flatFee = 50.0;
    private final double kitchenetteFee = 10.0;

    @BeforeEach
    void setUp() {
        startDate = new Date(); // Use a consistent date for tests

        // Reservation with all fees applicable (kitchenette and > 900 sq ft)
        reservationWithKitchenette = new Hotel_Reservation(
                "acc-hotel-123",
                "res-hotel-001",
                "101 Hotel Drive",
                "PO Box 101",
                startDate,
                3, // nights
                2, // beds
                1, // bedrooms
                1, // bathrooms
                1000, // squareFootage > 900
                ReservationStatus.CONFIRMED,
                "Hotel",
                true  // hasKitchenette
        );

        // Reservation without kitchenette and under 900 sq ft
        reservationWithoutKitchenette = new Hotel_Reservation(
                "acc-hotel-456",
                "res-hotel-002",
                "202 Hotel Blvd",
                "PO Box 202",
                startDate,
                4, // nights
                1, // bed
                1, // bedroom
                1, // bathroom
                800, // squareFootage <= 900
                ReservationStatus.PENDING,
                "Hotel",
                false // hasKitchenette
        );
    }

    @Test
    @DisplayName("Test constructor and getters for correct initialization")
    void testConstructorAndGetters() {
        assertAll("Verify constructor values",
                () -> assertEquals("res-hotel-001", reservationWithKitchenette.getReservationNumber()),
                () -> assertTrue(reservationWithKitchenette.isHasKitchenette()),
                () -> assertEquals(1000, reservationWithKitchenette.getSquareFootage()),
                () -> assertEquals(flatFee, reservationWithKitchenette.getFlatFee()),
                () -> assertEquals(kitchenetteFee, reservationWithKitchenette.getKitchenetteFee())
        );
    }

    @Test
    @DisplayName("Test clone() method creates a perfect copy")
    void testClone() throws CloneNotSupportedException {
        Hotel_Reservation clonedReservation = reservationWithKitchenette.clone();

        // Ensure it's a different object instance
        assertNotSame(reservationWithKitchenette, clonedReservation, "Clone should be a new object.");

        // Ensure the values are identical
        assertEquals(reservationWithKitchenette.getReservationNumber(), clonedReservation.getReservationNumber());
        assertEquals(reservationWithKitchenette.isHasKitchenette(), clonedReservation.isHasKitchenette());
        assertEquals(reservationWithKitchenette.totalPrice(), clonedReservation.totalPrice());
    }

    @Test
    @DisplayName("Test pricePerNight with kitchenette and > 900 sq ft")
    void testPricePerNight_WithKitchenette_Over900SqFt() {
        double expectedPrice = basePrice + sqFtFee + flatFee + kitchenetteFee;
        assertEquals(expectedPrice, reservationWithKitchenette.pricePerNight(), "Price should include all fees.");
    }

    @Test
    @DisplayName("Test pricePerNight with kitchenette and <= 900 sq ft")
    void testPricePerNight_WithKitchenette_Under900SqFt() {
        reservationWithKitchenette.setSquareFootage(850); // Set sq ft to be under the threshold
        double expectedPrice = basePrice + flatFee + kitchenetteFee;
        assertEquals(expectedPrice, reservationWithKitchenette.pricePerNight(), "Price should not include the square footage fee.");
    }

    @Test
    @DisplayName("Test pricePerNight without kitchenette and > 900 sq ft")
    void testPricePerNight_WithoutKitchenette_Over900SqFt() {
        reservationWithoutKitchenette.setSquareFootage(1100); // Set sq ft to be over the threshold
        double expectedPrice = basePrice + sqFtFee + flatFee;
        assertEquals(expectedPrice, reservationWithoutKitchenette.pricePerNight(), "Price should not include the kitchenette fee.");
    }

    @Test
    @DisplayName("Test pricePerNight without kitchenette and <= 900 sq ft")
    void testPricePerNight_WithoutKitchenette_Under900SqFt() {
        double expectedPrice = basePrice + flatFee;
        assertEquals(expectedPrice, reservationWithoutKitchenette.pricePerNight(), "Price should only be base price plus flat fee.");
    }

    @Test
    @DisplayName("Test totalPrice() calculates correctly over multiple nights")
    void testTotalPrice() {
        double expectedTotalPrice = reservationWithKitchenette.pricePerNight() * reservationWithKitchenette.getNights();
        assertEquals(expectedTotalPrice, reservationWithKitchenette.totalPrice(), "Total price should be nightly price times number of nights.");
    }

    @Test
    @DisplayName("Test toString() contains hotel-specific XML tags")
    void testToString() {
        String reservationString = reservationWithKitchenette.toString();
        assertAll("Verify toString() output",
                () -> assertTrue(reservationString.startsWith("<hotel>"), "String should start with <hotel> tag."),
                () -> assertTrue(reservationString.endsWith("</hotel>"), "String should end with </hotel> tag."),
                () -> assertTrue(reservationString.contains("<hasKitchenette>true</hasKitchenette>"), "Should contain hasKitchenette tag."),
                () -> assertTrue(reservationString.contains("<flatFee>" + flatFee + "</flatFee>"), "Should contain flatFee tag.")
        );
    }
}
