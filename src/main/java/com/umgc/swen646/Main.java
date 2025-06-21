package com.umgc.swen646;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static com.umgc.swen646.Reservation.DATE_FORMATTER;


public class Main {
    public static void main (String[] args) {
//        Account account1 = new Account("ac-100000000", "456 Oak Avenue, Chicago, IL 60614",
//                "202-432-9595", "john.doe@email.com",null );
//
//        // Create a sample date for the reservation start
//        Date startDate = new Date(); // Current date
//
//        // Create a sample com.umgc.swen646.Hotel_Reservation
//        Hotel_Reservation sampleHotelReservation = new Hotel_Reservation(
//                "AC-123456789", // accountNumber
//                "RES-987654321", // reservationNumber
//                "123 Main St, Anytown, USA", // physicalAddress
//                "456 PO Box, Anytown, USA", // mailingAddress
//                startDate, // reservationStart
//                5, // nights
//                2, // beds
//                1, // bedrooms
//                1, // bathrooms
//                450.0, // squareFootage (less than 900)
//                ReservationStatus.CONFIRMED, // status
//                "Hotel", // reservationType
//                true // hasKitchenette
//        );
//
//        try {
//            Hotel_Reservation clone = sampleHotelReservation.clone();
//            System.out.println("Sample Hotel Reservation Created:");
//            System.out.println(sampleHotelReservation);
//            System.out.println("Total Price: $" + sampleHotelReservation.totalPrice());
//            System.out.println("Price Per Night: $" + sampleHotelReservation.pricePerNight());
//        } catch (CloneNotSupportedException e) {
//            throw new RuntimeException(e);
//        }
        Manager manager = new Manager();
        String futureDateString = "2026-07-15"; // A date in the future
        Date specificFutureDate = null;
        try {
            specificFutureDate = DATE_FORMATTER.parse(futureDateString);
            System.out.println("Specific Future Date: " + specificFutureDate);
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
        }


        manager.loadData();
        manager.updateAccount(new UpdateAccountBuilder("ac-100000000").build().withNewEmail("newEmailExample@email.com"));
        manager.createReservation("ac-100000000", "res-0000000001", "456 Reservation Rd, Minnesota, USA",
                "456 Reservation Rd, Minnesota, USA", specificFutureDate, 7, 4, 4,4, 3000,
                ReservationStatus.PENDING, "House", true, false, false, 3);
       manager.createReservation("ac-100000000", "res-0000000002", "456 Reservation Rd, Minnesota, USA",
                "700 Reservation Rd, Minnesota, USA", new Date(), 2, 4, 4,4, 3000,
                ReservationStatus.PENDING, "House", true, true, true, 3);
//        manager.cancelReservation("ac-100000000", "res-0000000002");
        manager.changeReservation("ac-100000000", manager.getReservation("ac-100000000", "res-0000000001"));
        manager.displayLoadedData();
    }
}