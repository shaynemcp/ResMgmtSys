package com.umgc.swen646;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static com.umgc.swen646.Reservation.DATE_FORMATTER;


public class Main {
    public static void main (String[] args) {

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