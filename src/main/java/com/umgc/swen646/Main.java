package com.umgc.swen646;

import java.util.Date;


public class Main {
    public static void main (String[] args) {
        Account account1 = new Account("ac-100000000", "456 Oak Avenue, Chicago, IL 60614",
                "202-432-9595", "john.doe@email.com",null );

        // Create a sample date for the reservation start
        Date startDate = new Date(); // Current date

        // Create a sample com.umgc.swen646.Hotel_Reservation
        Hotel_Reservation sampleHotelReservation = new Hotel_Reservation(
                "AC-123456789", // accountNumber
                "RES-987654321", // reservationNumber
                "123 Main St, Anytown, USA", // physicalAddress
                "456 PO Box, Anytown, USA", // mailingAddress
                startDate, // reservationStart
                5, // nights
                2, // beds
                1, // bedrooms
                1, // bathrooms
                450.0, // squareFootage (less than 900)
                ReservationStatus.CONFIRMED, // status
                "Hotel", // reservationType
                true // hasKitchenette
        );

        try {
            Hotel_Reservation clone = sampleHotelReservation.clone();
            System.out.println("Sample Hotel Reservation Created:");
            System.out.println(sampleHotelReservation);
            System.out.println("Total Price: $" + sampleHotelReservation.totalPrice());
            System.out.println("Price Per Night: $" + sampleHotelReservation.pricePerNight());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }



    }
}