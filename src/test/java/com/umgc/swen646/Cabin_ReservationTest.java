package com.umgc.swen646;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Cabin_ReservationTest {


    @Test
    void PricePerNight_WithKitchenAndMultipleBathrooms() {
        // Arrange
        Cabin_Reservation reservation = new Cabin_Reservation("ACC1", "RES1", "ADDR1", "ADDR2", new Date(), 5, 2, 2, 3, 1200, ReservationStatus.PENDING, "Cabin", true, false);
        double expectedPrice = reservation.getBasePrice() + reservation.fullKitchenFee + (reservation.getBathrooms() * reservation.bathroomFee);

        // Act
        double actualPrice = reservation.pricePerNight();

        // Assert
        assertEquals(expectedPrice, actualPrice, "Price should include kitchen and bathroom fees.");
    }

    @Test
    void testPricePerNight_WithKitchenAndOneBathroom() {
        // Arrange
        Cabin_Reservation reservation = new Cabin_Reservation("ACC2", "RES2", "ADDR1", "ADDR2", new Date(), 5, 2, 2, 1, 800, ReservationStatus.PENDING, "Cabin", true, true);
        double expectedPrice = reservation.getBasePrice() + reservation.fullKitchenFee;

        // Act
        double actualPrice = reservation.pricePerNight();

        // Assert
        assertEquals(expectedPrice, actualPrice, "Price should include only the kitchen fee.");
    }

    @Test
    void testPricePerNight_WithoutKitchenAndMultipleBathrooms() {
        // Arrange
        Cabin_Reservation reservation = new Cabin_Reservation("ACC3", "RES3", "ADDR1", "ADDR2", new Date(), 5, 2, 2, 4, 1500, ReservationStatus.PENDING, "Cabin", false, false);
        double expectedPrice = reservation.getBasePrice() + (reservation.getBathrooms() * reservation.bathroomFee);

        // Act
        double actualPrice = reservation.pricePerNight();

        // Assert
        assertEquals(expectedPrice, actualPrice, "Price should include only the bathroom fees.");
    }

    @Test
    void testPricePerNight_WithNoExtras() {
        // Arrange
        Cabin_Reservation reservation = new Cabin_Reservation("ACC4", "RES4", "ADDR1", "ADDR2", new Date(), 5, 2, 2, 1, 700, ReservationStatus.PENDING, "Cabin", false, true);
        double expectedPrice = reservation.getBasePrice();

        // Act
        double actualPrice = reservation.pricePerNight();

        // Assert
        assertEquals(expectedPrice, actualPrice, "Price should be the base price.");
    }

    @Test
    void totalPrice() {
        // Arrange
        Cabin_Reservation reservation = new Cabin_Reservation("ACC5", "RES5", "ADDR1", "ADDR2", new Date(), 3, 2, 1, 1, 600, ReservationStatus.PENDING, "Cabin", false, false);
        double expectedTotalPrice = reservation.getBasePrice() * reservation.getNights();

        // Act
        double actualTotalPrice = reservation.totalPrice();

        // Assert
        assertEquals(expectedTotalPrice, actualTotalPrice, "Total price should be pricePerNight multiplied by the number of nights.");
    }

}