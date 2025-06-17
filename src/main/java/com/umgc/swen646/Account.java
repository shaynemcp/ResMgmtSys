package com.umgc.swen646;

import java.util.*;

/**
 * Accounts store reservations for clients, loaded by a com.umgc.swen646.Manager upon program start, identified by a UI of "ac-" & 9 characters which cannot be changed
 * They hold information in attributes: [mailing address, list of reservations, email address and phone #]
 * Accounts are updated by a com.umgc.swen646.Manager, and once added cannot be deleted
 */
public class Account extends Manager {



    public Account(String accountNumber, String mailingAddress, String phoneNumber, String email, List<Reservation> reservations) {
        this.accountNumber = accountNumber;
        this.mailingAddress = mailingAddress;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.reservations = reservations;
    }




    /**
     * com.umgc.swen646.Account# for an account, initialized @ value =-99 until reservation is updated
     */
    private String accountNumber;

    /**
     *  Nullable, modifiable mailing address associated with an account
     */
    public String mailingAddress;

    /**
     * Non-null, modifiable phone number associated with the account
     */
    private String phoneNumber;

    /**
     * Non-null, modifiable email address associated with the account
     */
    private String email;

    /**
     * Stores a list of reservations and/or draft reservations associated with the account
     */
    private List<Reservation> reservations;

    /**
     * Loads XML/JSON from local file & converts to com.umgc.swen646.Account Object
     */
//    public Document loadFromFile(String filePath) throws IOException, SAXException, ParserConfigurationException {
//        // TODO implement here
//        // Create a DocumentBuilder
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//
//        // Parse the XML file into a Document object
//        File xmlFile = new File(filePath);
//        Document document = builder.parse(xmlFile);
//
//        // Normalize the document to ensure consistent structure
//        document.getDocumentElement().normalize();
//
//        try {
//            Document document1 = XMLReader.readXMLFromFile("sample.xml");
//            // Process the Document object as needed
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return document;
//    }

    /**
     * Updates the account to data passed through params to local text file in XML/JSON
     */
    public void saveToFile() {
        // TODO implement here
    }

    /**
     * GETTERS & SETTERS
    */
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    /**
     * Formats and return's com.umgc.swen646.Account data as output for the user to read
     */
    @Override
    public String toString() {
        return "com.umgc.swen646.Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", mailingAddress='" + mailingAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", reservations=" + reservations +
                '}';
    }
}