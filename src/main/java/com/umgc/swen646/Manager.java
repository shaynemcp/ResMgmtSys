package com.umgc.swen646;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Manager class which acts as CRUD DB manager for all reservation Accounts
 */
public class Manager {
    /**
     * links the filepath to the XML/JSON files where data will be stored/updated
     */
    public String DATA_DIRECTORY = "/Users/shaynemcpherson/Documents/UMGC/Summer25/ResMgmtDB";

    /**
     * Stores a list of Accounts associated with the Manger
     */
    private List<Account> accounts;
    private List<Reservation> reservations;

    Manager() {
        this.reservations = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }

        /**
         * Generates XML files for all accounts and their associated reservations
         */
        public void generateXmlFiles() {
            System.out.println("Generating XML files in: " + DATA_DIRECTORY);
            File baseDir = new File(DATA_DIRECTORY);
            if (!baseDir.exists()) {
                baseDir.mkdirs(); // Create the base directory if it doesn't exist
            }

            if (this.accounts == null || this.accounts.isEmpty()) {
                System.out.println("No accounts to process. Please add accounts first.");
                return;
            }

            for (Account account : this.accounts) {
                String accountNumber = account.getAccountNumber();
                File accountDir = new File(baseDir, accountNumber);

                if (!accountDir.exists()) {
                    accountDir.mkdirs(); // Create the account-specific directory
                }

                // 1. Generate Account XML File
                File accountFile = new File(accountDir, "acc-" + accountNumber + ".xml");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountFile))) {
                    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                    writer.write("<account>\n");
                    writer.write("  <accountNumber>" + accountNumber + "</accountNumber>\n");
                    writer.write("  <mailingAddress>" + account.getMailingAddress() + "</mailingAddress>\n");
                    writer.write("  <phoneNumber>" + account.getPhoneNumber() + "</phoneNumber>\n");
                    writer.write("  <email>" + account.getEmail() + "</email>\n");

                    writer.write("  <reservations>\n");
                    if (account.getReservations() != null && !account.getReservations().isEmpty()) {
                        for (Reservation res : account.getReservations()) {
                            writer.write("    <reservationNumber>" + res.getReservationNumber() + "</reservationNumber>\n");
                        }
                    } else {
                        writer.write("    <message>No reservations associated with this account.</message>\n");
                    }
                    writer.write("  </reservations>\n");
                    writer.write("</account>");
                    System.out.println("Generated account file: " + accountFile.getAbsolutePath());
                } catch (IOException e) {
                    System.err.println("Error writing account file for " + accountNumber + ": " + e.getMessage());
                    e.printStackTrace();
                }

                // 2. Generate Reservation XML Files for each reservation
                if (account.getReservations() != null) {
                    for (Reservation res : account.getReservations()) {
                        File reservationFile = new File(accountDir, "res-" + res.getReservationNumber() + ".xml");
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reservationFile))) {
                            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                            // The toString() method already provides XML-like content,
                            // which should serve as the root element for the reservation.
                            writer.write(res.toString());
                            System.out.println("Generated reservation file: " + reservationFile.getAbsolutePath());
                        } catch (IOException e) {
                            System.err.println("Error writing reservation file for " + res.getReservationNumber() + ": " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
            System.out.println("XML file generation complete.");
        }

    /**
     * method which loads all accounts' data once the Manager class is instantiated, which occurs upon program start
     */
    public void loadData() {
        // TODO implement here
    }

    /**
     * Prints out accounts associated to a specific Person's account#
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
            throw new NullPointerException("Error: Account number cannot be null or empty.");
        }

        // Iterate through the list to find the account
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account; // Account found
            }
        }
        return null; // Account not found
    }

    /**
     * add an Account object to the list of accounts
     */
    public void addAccount(Account account) {
        if (account == null) {
            throw new NullPointerException("Error: Cannot add a null account.");
        }
        for (Account existingAccount : accounts) {
            if (existingAccount.getAccountNumber().equals(account.getAccountNumber())) {
                throw new SameAccountException(existingAccount.getAccountNumber());
            }
        }
        // If no duplicate is found, add the account to the list
        this.accounts.add(account);
        System.out.println("Account " + account.getAccountNumber() + " added successfully.");


    }

    /**
     * finds the Account in list of accounts and updates attribute values
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
     * Finds the Reservation with the associated accountNumber & updates the value for reservationStatus to completed status
     */
    public void completeReservation(String accountNumber, String reservationNumber, ReservationStatus reservationStatus) {
        // TODO implement here
    }

    /**
     * Finds the Reservation with the associated accountNumber & updates the value for reservationStatus to cancelled status
     */
    public void cancelReservation(String accountNumber, String reservationNumber) {
        // TODO implement here
    }

    /**
     * Finds the Reservation with the associated accountNumber & updates the passed params for the Reservation object
     */
    public void changeReservation(String accountNumber, Reservation reservation) {
        // TODO implement here
    }



}