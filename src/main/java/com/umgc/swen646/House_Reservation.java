package com.umgc.swen646;

import java.util.*;

/**
 * A subclass of com.umgc.swen646.Reservation which can include several floors, which may change the price
 */
public class House_Reservation extends Reservation {

    /**
     * Requests data for # of floors in the house and stores as attribute of House com.umgc.swen646.Reservation object
     */
    private int floors;

    public House_Reservation(int id, String accountNumber, String reservationNumber, String physicalAddress, String mailingAddress, Date reservationStart, int nights, int beds, int bedrooms, int bathrooms, double squareFootage, ReservationStatus status, String reservationType, int floors) {
        super(accountNumber, reservationNumber, physicalAddress, mailingAddress, reservationStart, nights, beds, bedrooms, bathrooms, squareFootage, status, reservationType);
        this.floors = floors;
    }

//  Creates a copy of a House com.umgc.swen646.Reservation for
    public House_Reservation clone() throws CloneNotSupportedException {
        House_Reservation houseReservation = (House_Reservation) super.clone();
        return houseReservation;    }

/**
* GETTERS & SETTERS
*/
    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

/**
 * TOSTRING
 * Converts the House com.umgc.swen646.Reservation object to user readable text, overrides com.umgc.swen646.Reservation toString method and adds
 * House com.umgc.swen646.Reservation attributes to com.umgc.swen646.Reservation's toString using super.toString + subsequent attributes
 * */
    @Override
    public String toString() {
        return "<house>" + super.toString() +
                "<floors>" + floors + "</floors>" +
                "</house>";
    }
}



