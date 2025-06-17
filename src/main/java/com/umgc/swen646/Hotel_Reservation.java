package com.umgc.swen646;

import java.util.*;

/**
 * A subclass of com.umgc.swen646.Reservation which can include the option of a kitchenette or not
 */
public class Hotel_Reservation extends Reservation {
    /**
     * Sets the boolean value which determines if the reservation requires an extra kitchenette fee
     */
    private final boolean hasKitchenette;
    /**
     * If a Hotel includes a kitchenette, they reservation will have a $10 fee
     */
    private final double kitchenetteFee = 10;
    /**
     * Every Hotel com.umgc.swen646.Reservation will incur a flat fee of $50
     */
    private final double flatFee = 50;





    /**
     * Constructor for the reservation type Hotel com.umgc.swen646.Reservation
     */
    public Hotel_Reservation(int id, String accountNumber, String reservationNumber, String physicalAddress, String mailingAddress, Date reservationStart, int nights, int beds, int bedrooms, int bathrooms, double squareFootage, ReservationStatus status, String reservationType, boolean hasKitchenette) {
        super(id, accountNumber, reservationNumber, physicalAddress, mailingAddress, reservationStart, nights, beds, bedrooms, bathrooms, squareFootage, status, reservationType);
        this.hasKitchenette = hasKitchenette;
    }

    public Hotel_Reservation clone() throws CloneNotSupportedException {
        Hotel_Reservation hotelReservation = (Hotel_Reservation) super.clone();
        return hotelReservation;    }
    /**
     * Calculates the sum of a Hotel com.umgc.swen646.Reservation based on base fee & add-ons per night
     */
    @Override
    public double totalPrice() {
        return pricePerNight() * getNights();
    }
    @Override
    public double pricePerNight() {
        double newPrice;
        if(getSquareFootage() > 900)
            newPrice = getBasePrice() + getSqFtFee() + flatFee;
        else newPrice = getBasePrice() + flatFee;

        if(hasKitchenette)
            return newPrice + kitchenetteFee;
        else return newPrice;
    }

    /**
     * Getters
     * */
    public boolean isHasKitchenette() {
        return hasKitchenette;
    }

    public double getKitchenetteFee() {
        return kitchenetteFee;
    }

    public double getFlatFee() {
        return flatFee;
    }

    /**
     *  TOSTRING
     *  * Converts the Hotel com.umgc.swen646.Reservation object to user readable text, overrides com.umgc.swen646.Reservation toString method and adds
     *  * Hotel com.umgc.swen646.Reservation attributes to com.umgc.swen646.Reservation's toString using super.toString + subsequent attributes
     * */
    @Override
    public String toString() {
        return "com.umgc.swen646.Hotel_Reservation{" + super.toString() +
                "hasKitchenette=" + hasKitchenette +
                ", kitchenetteFee=" + kitchenetteFee +
                ", flatFee=" + flatFee +
                ", id=" + id +
                '}';
    }
}