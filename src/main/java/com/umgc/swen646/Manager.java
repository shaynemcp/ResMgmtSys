package com.umgc.swen646;

import java.util.*;

/**
 * com.umgc.swen646.Manager class which acts as CRUD DB manager for all reservation Accounts
 */
public class Manager {
    /**
     * links the filepath to the XML/JSON files where data will be stored/updated
     */
    public String DATA_DIRECTORY;

    /**
     * Stores a list of Accounts associated with the Manger
     */
    private List<Account> accounts;

    Manager() {
        this.accounts = new ArrayList<>();
    }



    /**
     * method which loads all accounts' data once the com.umgc.swen646.Manager class is instantiated, which occurs upon program start
     */
    public void loadData() {
        // TODO implement here
    }

    /**
     * Prints out accounts associated to a specific com.umgc.swen646.Person's account#
     */
    public List<Account> getAccounts() {
        // Return a new ArrayList to prevent external modifications to the internal list
        return new ArrayList<>(this.accounts);
    }

    /**
     * passes accountNumber in list of accounts & returns matching Acccount object
     */
    public Account getAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            System.err.println("Error: com.umgc.swen646.Account number cannot be null or empty.");
            return null;
        }

        // Iterate through the list to find the account
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account; // com.umgc.swen646.Account found
            }
        }
        return null; // com.umgc.swen646.Account not found
    }

    /**
     * add an com.umgc.swen646.Account object to the list of accounts
     */
    public void addAccount(Account account) {
        if (account == null) {
            System.err.println("Error: Cannot add a null account.");
        }
        for (Account existingAccount : accounts) {
            if (existingAccount.getAccountNumber().equals(account.getAccountNumber())) {
                System.out.println("com.umgc.swen646.Account with number " + account.getAccountNumber() + " already exists. Not adding duplicate.");
                return; // Exit method if account already exists
            }
        }
        // If no duplicate is found, add the account to the list
        this.accounts.add(account);
        System.out.println("com.umgc.swen646.Account " + account.getAccountNumber() + " added successfully.");


    }

    /**
     * finds the com.umgc.swen646.Account in list of accounts and updates attribute values
     */
    public void updateAccount(ReservationStatus reservationStatus) {
        // TODO implement here
    }

    /**
     * Adds a reservation object to the list of reservation objects
     */
    public void addReservation(String accountNumber, Reservation reservation) {
        // TODO implement here
    }

    /**
     * Finds the com.umgc.swen646.Reservation with the associated accountNumber & updates the value for reservationStatus to completed status
     */
    public void completeReservation(String accountNumber, String reservationNumber, ReservationStatus reservationStatus) {
        // TODO implement here
    }

    /**
     * Finds the com.umgc.swen646.Reservation with the associated accountNumber & updates the value for reservationStatus to cancelled status
     */
    public void cancelReservation(String accountNumber, String reservationNumber) {
        // TODO implement here
    }

    /**
     * Finds the com.umgc.swen646.Reservation with the associated accountNumber & updates the passed params for the com.umgc.swen646.Reservation object
     */
    public void changeReservation(String accountNumber, Reservation reservation) {
        // TODO implement here
    }



}