package com.umgc.swen646;

import java.util.*;

/**
 * A subclass of com.umgc.swen646.Reservation which can include options for a full kitchen, loft or neither.
 */
public class Cabin_Reservation extends Reservation {
    public Cabin_Reservation(String accountNumber, String reservationNumber, String physicalAddress, String mailingAddress, Date reservationStart, int nights, int beds, int bedrooms, int bathrooms, double squareFootage, ReservationStatus status, String reservationType, boolean hasKitchen, boolean hasLoft) {
        super(accountNumber, reservationNumber, physicalAddress, mailingAddress, reservationStart, nights, beds, bedrooms, bathrooms, squareFootage, status, reservationType);
        this.hasKitchen = hasKitchen;
        this.hasLoft = hasLoft;
    }

    /**
     * Sets the boolean value which determines if the reservation requires an extra kitchen fee
     */
    private boolean hasKitchen;
    // Additional $20 fee applied if Cabin com.umgc.swen646.Reservation is requested with a full kitchen
    final double fullKitchenFee = 20;
    // Additional fee of $5 per bathroom on top of a com.umgc.swen646.Reservation's initially allotted bathroom.
    final double bathroomFee = 5;

    /**
     * Sets the boolean value which determines if the reservation requires an extra loft fee
     */
    private boolean hasLoft;

    /**
     * Clone replicates the Cabin com.umgc.swen646.Reservation object which is used by the UI, prevents user from directly modifying an implemented com.umgc.swen646.Reservation object
     * */
    public Cabin_Reservation clone() throws CloneNotSupportedException {
        Cabin_Reservation cabinReservation = (Cabin_Reservation) super.clone();
        return cabinReservation;
    }

    /**
     * Calculates the nightly price of a Cabin com.umgc.swen646.Reservation based on base fee & add-ons per night to include additional bathrooms or full kitchen
     */
    @Override
    public double pricePerNight() {
        if(hasKitchen && getBathrooms() > 1)
            return getBasePrice() + fullKitchenFee + (getBathrooms() * bathroomFee);
        else if (hasKitchen)
            return getBasePrice() + fullKitchenFee;
        else if (getBathrooms() > 1)
            return getBasePrice() + (getBathrooms() * bathroomFee);
        else
            return getBasePrice();
    }

    /**
     * Caluclates the total price of a Cabin com.umgc.swen646.Reservation after 'x' nights
     * */
    @Override
    public double totalPrice() {
        return pricePerNight() * getNights();
    }

    /**
     * GETTERS & SETTERS
     * */
    public boolean isHasKitchen() {
        return hasKitchen;
    }

    public void setHasKitchen(boolean hasKitchen) {
        this.hasKitchen = hasKitchen;
    }

    public boolean isHasLoft() {
        return hasLoft;
    }

    public void setHasLoft(boolean hasLoft) {
        this.hasLoft = hasLoft;
    }

   /**
    * ToString converts the Cabin com.umgc.swen646.Reservation object to a user readable String
    * */
    @Override
    public String toString() {
        return "com.umgc.swen646.Cabin_Reservation{" +
                super.toString() +
                "hasKitchen=" + hasKitchen +
                ", fullKitchenFee=" + fullKitchenFee +
                ", bathroomFee=" + bathroomFee +
                ", hasLoft=" + hasLoft +
                "} ";
    }
}