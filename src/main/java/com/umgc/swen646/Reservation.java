package com.umgc.swen646;

import javax.xml.stream.*;
import java.io.*;
import java.util.*;

/**
 * Represents an abstract Reservation for a specific trip.
 * This class provides the structure and methods required to manage a reservation.
 */
public abstract class Reservation {
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


//     Read all reservations from XML file
//    public List<Reservation> getAllReservations() {
//        List<Reservation> reservations = new ArrayList<>();
//
//        try {
//            XMLInputFactory factory = XMLInputFactory.newInstance();
//            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream("sample.xml"));
//
//            Reservation currentReservation = null;
//            String currentElement = "";
//
//
//            while (reader.hasNext()) {
//                int event = reader.next();
//
//                switch (event) {
//                    case XMLStreamConstants.START_ELEMENT:
//                        currentElement = reader.getLocalName();
//                        if ("reservation".equals(currentElement)) {
//                            reservationType = reader.getAttributeValue(null, "type");
//                            // Create specific reservation type based on the type attribute
//                            currentReservation = createReservationByType(reservationType);
//                        }
//                        break;
//
//                    case XMLStreamConstants.CHARACTERS:
//                        if (currentReservation != null && !reader.isWhiteSpace()) {
//                            String text = reader.getText().trim();
//                            if (!text.isEmpty()) {
//                                setReservationType(currentReservation, currentElement, text);
//                            }
//                        }
//                        break;
//
//                    case XMLStreamConstants.END_ELEMENT:
//                        if ("reservation".equals(reader.getLocalName()) && currentReservation != null) {
//                            reservations.add(currentReservation);
//                            currentReservation = null;
//                        }
//                        break;
//                }
//            }
//            reader.close();
//
//        } catch (Exception e) {
//            System.err.println("Error reading XML file: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        return reservations;
//    }

    /**
     * Loads reservation details from a file located at the specified file path.
     * Typically used to retrieve a previously saved reservation.
     */

    // Load reservation by reservation number
//    public Reservation loadReservationByNumber(String reservationNumber) {
//        List<Reservation> reservations = getAllReservations();
//        return reservations.stream()
//                .filter(r -> r.getReservationNumber().equals(reservationNumber))
//                .findFirst()
//                .orElse(null);
//    }
    // Save a new reservation (simplified - adds to existing structure)
//    public boolean saveNewReservation(Reservation newReservation) throws XMLStreamException {
//        List<Reservation> reservations = getAllReservations();
//        reservations.add(newReservation);
//        return saveAllReservations(reservations);
//    }
    // Helper method to save all reservations back to XML
    private boolean saveAllReservations(List<Reservation> reservations) throws XMLStreamException {
        try {
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream("sample.xml"));

            writer.writeStartDocument();
            writer.writeStartElement("account");
            // Write all reservations
            for (Reservation reservation : reservations) {
                writeReservation(writer, reservation);
            }

            writer.writeEndElement(); // reservations
            writer.writeEndElement(); // account
            writer.writeEndDocument();
            writer.close();

            return true;

        } catch (Exception e) {
            System.err.println("Error saving reservations: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    // Helper method to write a single reservation
    private void writeReservation(XMLStreamWriter writer, Reservation reservation) throws XMLStreamException {
        writer.writeStartElement("reservation");
        writer.writeAttribute("type", reservation.getReservationType());

        writeElement(writer, "accountNumber", reservation.getAccountNumber());
        writeElement(writer, "reservationNumber", reservation.getReservationNumber());
        writeElement(writer, "physicalAddress", reservation.getPhysicalAddress());
        writeElement(writer, "mailingAddress", reservation.getMailingAddress());
        writeElement(writer, "reservationStart", String.valueOf(reservation.getReservationStart()));
        writeElement(writer, "nights", String.valueOf(reservation.getNights()));
        writeElement(writer, "beds", String.valueOf(reservation.getBeds()));
        writeElement(writer, "bedrooms", String.valueOf(reservation.getBedrooms()));
        writeElement(writer, "bathrooms", String.valueOf(reservation.getBathrooms()));
        writeElement(writer, "squareFootage", String.valueOf(reservation.getSquareFootage()));
        writeElement(writer, "status", reservation.getStatus().toString());

        writer.writeEndElement(); // reservation
    }
    // Helper method to write XML elements
    private void writeElement(XMLStreamWriter writer, String elementName, String value) throws XMLStreamException {
        writer.writeStartElement(elementName);
        writer.writeCharacters(value != null ? value : "");
        writer.writeEndElement();
    }
    // Return reservations as plain text
//    public String getReservationsAsText() {
//        List<Reservation> reservations = getAllReservations();
//        StringBuilder sb = new StringBuilder();
//
//        for (int i = 0; i < reservations.size(); i++) {
//            sb.append("=== RESERVATION ").append(i + 1).append(" ===\n");
//            sb.append(reservations.get(i).toString());
//            sb.append("\n");
//        }
//
//        return sb.toString();
//    }
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
        return "<sqftfee>" + sqFtFee + "</sqftfee>" +
                "<baseprice>" + basePrice + "</baseprice>" +
                "<accountNumber>" + accountNumber + "</accountNumber>" +
                "<reservationNumber>" + reservationNumber + "</reservationNumber>" +
                "<physicalAddress>" + physicalAddress + "</physicalAddress>" +
                "<mailingAddress>" + mailingAddress + "</mailingAddress>" +
                "<reservationStart>" + reservationStart + "</reservationStart>" +
                "<nights>" + nights + "</nights>" +
                "<beds>" + beds + "</beds>" +
                "<bedrooms>" + bedrooms + "</bedrooms>" +
                "<bathrooms>" + bathrooms + "</bathrooms>" +
                "<squareFootage>" + squareFootage + "</squareFootage>" +
                "<status>" + status + "</status>" +
                "<reservationType>" + reservationType + "</reservationType>";
    }
}


