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



    // TODO:  Implement Read/Write XML method
    public void XMLHotelReader(){
        // TODO: add switch case to determine extra attributes including Kitchenette, Floors, etc.
    }


    public Hotel_Reservation(String accountNumber, String reservationNumber, String physicalAddress, String mailingAddress,
                             Date reservationStart, int nights, int beds, int bedrooms, int bathrooms, double squareFootage,
                             ReservationStatus status, String reservationType, boolean hasKitchenette) {
        super(accountNumber, reservationNumber, physicalAddress, mailingAddress, reservationStart, nights, beds, bedrooms,
                bathrooms, squareFootage, status, reservationType);
        this.hasKitchenette = hasKitchenette;
    }

    /**
     * Constructor for the reservation type Hotel com.umgc.swen646.Reservation
     */


    public Hotel_Reservation clone() throws CloneNotSupportedException {
        Hotel_Reservation cloneRes = new Hotel_Reservation(this.getAccountNumber(),this.getReservationNumber(),
                this.getPhysicalAddress(), this.getMailingAddress(), this.getReservationStart(), this.getNights(), this.getBeds(),
        this.getBedrooms(), this.getBathrooms(), this.getSquareFootage(), this.getStatus(), this.getReservationType(),
        this.isHasKitchenette());
        return cloneRes;    }
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
     *  * Overrides Reservation's toString & adds attributes included in a Hotel Reservation
     * */
    @Override
    public String toString() {
        return "<hotel>" + super.toString() +
                "<hasKitchenette>" + hasKitchenette + "</hasKitchenette>" +
                "<kitchenetteFee>" + kitchenetteFee + "</kitchenetteFee>" +
                "<flatFee>" + flatFee + "</flatFee>" +
                "</hotel>";
    }


}