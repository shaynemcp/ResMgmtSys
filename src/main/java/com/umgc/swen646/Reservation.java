package com.umgc.swen646;

import javax.xml.stream.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents an abstract Reservation for a specific trip.
 * This class provides the structure and methods required to manage a reservation.
 */
public abstract class Reservation {
    // Date formatter for consistent XML output and parsing
    protected static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * If a reservation has a square footage of larger than 900sqft. it will incur a $15 fee
     */
     private final double sqFtFee;

     /**
     * The base price of any Reservation
      */
     private final double basePrice;

    /**
     * Default constructor, typically used for creating an instance of a subclass.
     */
    public Reservation(String accountNumber, String reservationNumber, String physicalAddress, String mailingAddress,
                       Date reservationStart, int nights, int beds, int bedrooms, int bathrooms, double squareFootage,
                       ReservationStatus status, String reservationType) {
        this.accountNumber = accountNumber;
        this.reservationNumber = reservationNumber;
        this.physicalAddress = physicalAddress;
        this.mailingAddress = mailingAddress;
        this.reservationStart = reservationStart;
        this.nights = nights;
        this.beds = beds;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.squareFootage = squareFootage;
        this.status = status;
        this.reservationType = reservationType;
        basePrice = 120;
        sqFtFee = 15;
    }

    /**
     * Account number associated with the user who made the reservation.
     */
    private String accountNumber;

    /**
     * A unique identifier string specific to this reservation (may differ from `id`).
     */
    private String reservationNumber;

    /**
     * Physical address of the reserved property.
     * If not provided, it may default to the mailing address.
     */
    private String physicalAddress;

    /**
     * Mailing address used for billing or communication purposes.
     */
    private String mailingAddress;

    /**
     * The start date of the reservation.
     */
    private Date reservationStart;

    /**
     * Number of nights the reservation spans.
     */
    private int nights;

    /**
     * Number of beds included in the reserved property.
     */
    private int beds;

    /**
     * Number of bedrooms in the property.
     */
    private int bedrooms;

    /**
     * Number of bathrooms in the property.
     */
    private int bathrooms;

    /**
     * Total square footage of the reserved property.
     */
    private double squareFootage;

    /**
     * Status of the reservation (e.g., Confirmed, Canceled, Pending).
     */
    private ReservationStatus status;


    /**Differentiates between Hotel, House or Cabin Reservation types*/
    private String reservationType;

    /**
     * Calculates and returns the price per night for the reservation.
     *
     * @return Price divided by number of nights (if nights > 0)
     */
    public double pricePerNight() {
        if(getSquareFootage() > 900)
           return basePrice + sqFtFee;
        else
            return basePrice;
        // TODO: Implement exception handling for invalid 'nights' input
    }

    /**
     * Calculates the total price of the reservation, possibly including additional fees.
     *
     * @return Total cost based on duration, square footage, and base price
     */
    public double totalPrice() {
        return pricePerNight() * nights;
    }


    /**
     * Saves the current reservation instance to a local file in XML or JSON format.
     * Useful for persistent storage of reservation details.
     * */
    public void saveToFile() {
        // TODO: Implement file saving logic
    }

    /**GETTERS*/
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public Date getReservationStart() {
        return reservationStart;
    }

    public int getNights() {
        return nights;
    }

    public int getBeds() {
        return beds;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public String getReservationType() {
        return reservationType;
    }

    public double getSqFtFee() {
        return sqFtFee;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public double getSquareFootage() {
        return squareFootage;
    }

    /**SETTERS*/
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public void setReservationStart(Date reservationStart) {
        this.reservationStart = reservationStart;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public void setSquareFootage(int squareFootage) {
        this.squareFootage = squareFootage;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }



    public void setReservationType(String reservationType) {
        this.reservationType = reservationType;
    }

    /**
     * Returns Reservation details to be used in Read/Write XML methods & saved to permanent Data file
     */
    @Override
    public String toString() {
        // Output common fields as XML fragment
        return "  <sqFtFee>" + sqFtFee + "</sqFtFee>\n" +
                "  <basePrice>" + basePrice + "</basePrice>\n" +
                "  <accountNumber>" + accountNumber + "</accountNumber>\n" +
                "  <reservationNumber>" + reservationNumber + "</reservationNumber>\n" +
                "  <physicalAddress>" + physicalAddress + "</physicalAddress>\n" +
                "  <mailingAddress>" + mailingAddress + "</mailingAddress>\n" +
                "  <reservationStart>" + DATE_FORMATTER.format(reservationStart) + "</reservationStart>\n" +
                "  <nights>" + nights + "</nights>\n" +
                "  <beds>" + beds + "</beds>\n" +
                "  <bedrooms>" + bedrooms + "</bedrooms>\n" +
                "  <bathrooms>" + bathrooms + "</bathrooms>\n" +
                "  <squareFootage>" + squareFootage + "</squareFootage>\n" +
                "  <status>" + status.name() + "</status>\n" +
                "  <reservationType>" + reservationType + "</reservationType>\n";
    }
}


