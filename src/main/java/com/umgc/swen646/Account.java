package com.umgc.swen646;

import java.io.File;
import java.util.*;

/**
 * Accounts store reservations for clients, loaded by a Manager upon program start, identified by a UI of "ac-" & 9 characters which cannot be changed
 * They hold information in attributes: [mailing address, list of reservations, email address and phone #]
 * Accounts are updated by a Manager, and once added cannot be deleted
 */
public class Account{

    public Account(String accountNumber, String mailingAddress, String phoneNumber, String email, List<Reservation> reservations) {
        this.accountNumber = accountNumber;
        this.mailingAddress = mailingAddress;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.reservations = reservations;
    }

    /**
     * Account# for an account, initialized @ value =-99 until reservation is updated
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
     * Loads XML/JSON from local file & converts to Account Object
     */
//    public Document loadFromFile(String filePath) throws IOException, SAXException, ParserConfigurationException {

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
     * Formats and return's Account data as output for the user to read
     */
    @Override
    public String toString() {
        return "<accountNumber>" + accountNumber + "</accountNumber>" +
                "<mailingAddress>" + mailingAddress + "</mailingAddress>" +
                "<phoneNumber>" + phoneNumber + "</phoneNumber>" +
                "<email>" + email + "</email>" +
                "<reservations>" + reservations + "</reservations>";
    }
}