package com.umgc.swen646;

import org.glassfish.jaxb.core.v2.TODO;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.text.ParseException;
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
        System.out.println("Starting data load from: " + DATA_DIRECTORY);
        File baseDir = new File(DATA_DIRECTORY);

        if (!baseDir.exists() || !baseDir.isDirectory()) {
            System.out.println("Data directory does not exist or is not a directory: " + DATA_DIRECTORY);
            return;
        }

        this.accounts.clear();
        this.reservations.clear();
        System.out.println("Cleared existing accounts and reservations in memory.");

        File[] accountDirs = baseDir.listFiles(File::isDirectory);

        if (accountDirs == null || accountDirs.length == 0) {
            System.out.println("No account directories found in " + DATA_DIRECTORY + ". No data to load.");
            return;
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            System.out.println("XML DocumentBuilder initialized successfully.");
        } catch (ParserConfigurationException e) {
            System.err.println("Error configuring XML parser: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        for (File accountDir : accountDirs) {
            String accountNumber = accountDir.getName();
            File accountFile = new File(accountDir, "acc-" + accountNumber + ".xml");

            if (!accountFile.exists()) {
                System.out.println("Account XML file 'acc-" + accountNumber + ".xml' not found in " + accountDir.getName() + ". Skipping this account.");
                continue;
            }

            try {
                // 1. Parse Account XML File
                System.out.println("Attempting to parse account file: " + accountFile.getAbsolutePath());
                Document accountDoc = dBuilder.parse(accountFile);
                accountDoc.getDocumentElement().normalize();

                String mailingAddress = getTagValue(accountDoc, "mailingAddress");
                String phoneNumber = getTagValue(accountDoc, "phoneNumber");
                String email = getTagValue(accountDoc, "email");

                // Create a new Account object
                Account account = new Account(accountNumber, mailingAddress, phoneNumber, email, null);
                System.out.println("Successfully loaded basic Account details: " + account.getAccountNumber());

                // Temporary list to hold reservations for this specific account
                List<Reservation> tempReservationsForAccount = new ArrayList<>();

                // 2. Discover and Parse individual Reservation XML files within this account's directory
                File[] reservationFiles = accountDir.listFiles((dir, name) -> name.startsWith("res-") && name.endsWith(".xml"));

                if (reservationFiles != null) {
                    System.out.println("  Found " + reservationFiles.length + " reservation XML files in " + accountDir.getName() + " directory.");
                    for (File resFile : reservationFiles) {
                        try {
                            Reservation reservation = parseReservationXml(resFile, dBuilder);
                            if (reservation != null) {
                                tempReservationsForAccount.add(reservation); // Add to temp list for this account
                                this.reservations.add(reservation); // Also add to manager's flat list if needed
                                System.out.println("  Parsed Reservation: '" + reservation.getReservationNumber() + "' (Type: " + reservation.getClass().getSimpleName() + ")");
                            }
                        } catch (IOException | SAXException innerE) {
                            System.err.println("Error parsing reservation file '" + resFile.getName() + "': " + innerE.getMessage());
                            innerE.printStackTrace();
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    System.out.println("  No reservation XML files found for account " + accountNumber + ".");
                }

                // Set the collected reservations to the Account object
                account.setReservations(tempReservationsForAccount);
                System.out.println("  Attached " + tempReservationsForAccount.size() + " reservations to Account: '" + account.getAccountNumber() + "'");


                this.accounts.add(account);
                System.out.println("Account '" + account.getAccountNumber() + "' loaded successfully with " + account.getReservations().size() + " reservations.");

            } catch (IOException | SAXException e) {
                System.err.println("Error parsing account file '" + accountFile.getName() + "': " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Data loading complete. Total accounts loaded: " + this.accounts.size());
        displayLoadedData();
    }

    /**
     * Prints out all accounts currently loaded in memory and their associated reservations.
     * This provides a summary of the data successfully loaded by `loadData()`.
     */
    public void displayLoadedData() {
        System.out.println("\n--- Displaying Currently Loaded Data ---");
        if (this.accounts.isEmpty()) {
            System.out.println("No accounts currently loaded in memory.");
            return;
        }

        for (Account account : this.accounts) {
            System.out.println(account); // Uses Account's toString() for primary details
            if (!account.getReservations().isEmpty()) {
                System.out.println("  Reservations for " + account.getAccountNumber() + ":");
                for (Reservation res : account.getReservations()) {
                    // Prints key details of each reservation for quick overview, including its concrete type
                    System.out.println("    - Num: " + res.getReservationNumber() +
                            ", Type: " + res.getClass().getSimpleName() +
                            ", Status: " + res.getStatus() +
                            ", Check-in: " + Reservation.DATE_FORMATTER.format(res.getReservationStart()) +
                            ", Beds: " + res.getBeds() +
                            ", Price/Night: " + String.format("%.2f", res.pricePerNight()));
                    // You can add more specific details here based on reservation type if desired
                }
            } else {
                System.out.println("  No reservations found for this account.");
            }
            System.out.println(); // Blank line for readability between accounts
        }
        System.out.println("----------------------------------------\n");
    }

    /**
     * Helper method to extract the text content of the first occurrence of a specified tag from an XML Document.
     * @param doc The XML Document from which to extract the tag value.
     * @param tagName The name of the XML tag whose text content is to be extracted.
     * @return The text content of the tag, or an empty string if the tag is not found or is empty.
     */
    private String getTagValue(Document doc, String tagName) {
        NodeList nl = doc.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Node node = nl.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                return node.getTextContent();
            }
        }
        return ""; // Return empty string if tag not found or has no content
    }

    /**
     * Helper method to parse a single Reservation XML file into a Reservation object.
     * This method assumes a specific XML structure for reservation files as generated by `Reservation.toString()`.
     * @param resFile The File object representing the reservation XML file.
     * @param dBuilder The DocumentBuilder instance to use for parsing.
     * @return A populated Reservation object if parsing is successful, otherwise null.
     * @throws IOException If an I/O error occurs during file reading.
     * @throws SAXException If any parse error or warning occurs.
     */
    private Reservation parseReservationXml(File resFile, DocumentBuilder dBuilder) throws IOException, SAXException, ParseException {
        System.out.println("  Parsing reservation file: " + resFile.getName());
        Document resDoc = dBuilder.parse(resFile);
        resDoc.getDocumentElement().normalize();

        String rootElementName = resDoc.getDocumentElement().getTagName();
        System.out.println("  Reservation XML Root Element: " + rootElementName);

        String accountNumber = getTagValue(resDoc, "accountNumber");
        String reservationNumber = getTagValue(resDoc, "reservationNumber");
        String physicalAddress = getTagValue(resDoc, "physicalAddress");
        String mailingAddress = getTagValue(resDoc, "mailingAddress");
        Date reservationStart = Reservation.DATE_FORMATTER.parse(getTagValue(resDoc, "reservationStart"));
        int nights = Integer.parseInt(getTagValue(resDoc, "nights"));
        int beds = Integer.parseInt(getTagValue(resDoc, "beds"));
        int bedrooms = Integer.parseInt(getTagValue(resDoc, "bedrooms"));
        int bathrooms = Integer.parseInt(getTagValue(resDoc, "bathrooms"));
        double squareFootage = Double.parseDouble(getTagValue(resDoc, "squareFootage"));
        ReservationStatus status = ReservationStatus.valueOf(getTagValue(resDoc, "status").toUpperCase());
        String reservationType = getTagValue(resDoc, "reservationType");


        switch (rootElementName) {
            case "cabinReservation":
                boolean hasKitchen = Boolean.parseBoolean(getTagValue(resDoc, "hasKitchen"));
                boolean hasLoft = Boolean.parseBoolean(getTagValue(resDoc, "hasLoft"));
                return new Cabin_Reservation(0, accountNumber, reservationNumber, physicalAddress, mailingAddress,
                        reservationStart, nights, beds, bedrooms, bathrooms, squareFootage,
                        status, reservationType, hasKitchen, hasLoft);
            case "hotelReservation":
                boolean hasKitchenette = Boolean.parseBoolean(getTagValue(resDoc, "hasKitchenette"));
                return new Hotel_Reservation(accountNumber, reservationNumber, physicalAddress, mailingAddress,
                        reservationStart, nights, beds, bedrooms, bathrooms, squareFootage,
                        status, reservationType, hasKitchenette);
            case "houseReservation":
                int floors = Integer.parseInt(getTagValue(resDoc, "floors"));
                return new House_Reservation(0, accountNumber, reservationNumber, physicalAddress, mailingAddress,
                        reservationStart, nights, beds, bedrooms, bathrooms, squareFootage,
                        status, reservationType, floors);
            default:
                System.out.println(reservationType + " in file: " + resFile.getName());
                return null;
        }
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
    public boolean updateAccount(UpdateAccountBuilder builder) {
        if (builder.getAccountNumber() == null || builder.getAccountNumber().trim().isEmpty()) {
            throw new NullPointerException("Error: Account number cannot be null or empty for update operation.");
        }

        Account existingAccount = getAccount(builder.getAccountNumber());

        if (existingAccount != null) {
            // Update mailing address if provided by the builder
            if (builder.getNewMailingAddress() != null) {
                existingAccount.setMailingAddress(builder.getNewMailingAddress());
            }
            // Update phone number if provided by the builder
            if (builder.getNewPhoneNumber() != null) {
                existingAccount.setPhoneNumber(builder.getNewPhoneNumber());
            }
            // Update email if provided by the builder
            // Note: I'm assuming 'newEmail' was intended for Account update,
            // as 'newPhysicalAddress' is a Reservation field and not directly part of Account.
            if (builder.getNewEmail() != null) {
                existingAccount.setEmail(builder.getNewEmail());
            }

            System.out.println("Account " + builder.getNewPhoneNumber() + " updated successfully in memory.");

            // Persist changes to disk by regenerating XML files
            // This will regenerate all account and reservation files to reflect the change.
            // For a large system, consider a more granular save mechanism if performance is critical.
            generateXmlFiles();

            return true;
        } else {
            System.out.println("Account " + builder.getAccountNumber() + " not found. Update failed.");
            return false;
        }
    }

    /**
     * Adds a reservation object to the list of reservation objects
     */
    public void addReservation(String accountNumber, Reservation reservation) {
        Account account = getAccount(accountNumber);
        if (account != null) {
            List<Reservation> currentReservations = new ArrayList<>(account.getReservations()); // Get a mutable copy
            currentReservations.add(reservation);
            account.setReservations(currentReservations); // Set the updated list back

            this.reservations.add(reservation); // Add to Manager's flat list
            System.out.println("Reservation " + reservation.getReservationNumber() + " added to account " + accountNumber + " in memory.");
        } else {
            System.out.println("Account " + accountNumber + " not found. Cannot add reservation.");
        }
    }
    /** Method to grab a Reservation by accountNumber & reservationNumber*/
    public Reservation getReservation(String accountNumber, String reservationNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new NullPointerException("Account number cannot be null or empty for getReservation operation.");
        }
        if (reservationNumber == null || reservationNumber.trim().isEmpty()) {
            throw new NullPointerException("Reservation number cannot be null or empty for getReservation operation.");
        }

        Account account = getAccount(accountNumber);
        if (account == null) {
            System.out.println("Account " + accountNumber + " not found. Cannot retrieve reservation " + reservationNumber + ".");
            return null; // Account not found
        }

        for (Reservation res : account.getReservations()) {
            if (res.getReservationNumber().equals(reservationNumber)) {
                System.out.println("Reservation " + reservationNumber + " found for account " + accountNumber + ".");
                return res; // Reservation found
            }
        }

        System.out.println("Reservation " + reservationNumber + " not found for account " + accountNumber + ".");
        return null; // Reservation not found within the account
    }

    /**Creates a new Reservation for a given Account Number & adds to the list of reservation objects */
    public boolean createReservation(String accountNumber, String reservationNumber, String physicalAddress,
                                     String mailingAddress, Date reservationStart, int nights, int beds,
                                     int bedrooms, int bathrooms, double squareFootage, ReservationStatus status,
                                     String reservationType, Boolean hasKitchen, Boolean hasLoft,
                                     Boolean hasKitchenette, Integer floors) {

        // Validate mandatory parameters
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new NullPointerException("Account number cannot be null or empty.");
        }
        if (reservationNumber == null || reservationNumber.trim().isEmpty()) {
            throw new NullPointerException("Reservation number cannot be null or empty.");
        }
        if (physicalAddress == null || physicalAddress.trim().isEmpty()) {
            throw new NullPointerException("Physical address cannot be null or empty.");
        }
        if (mailingAddress == null || mailingAddress.trim().isEmpty()) {
            throw new NullPointerException("Mailing address cannot be null or empty.");
        }
        if (reservationStart == null) {
            throw new NullPointerException("Reservation start date cannot be null.");
        }
        if (reservationType == null || reservationType.trim().isEmpty()) {
            throw new NullPointerException("Reservation type cannot be null or empty.");
        }
        if (status == null) {
            throw new NullPointerException("Reservation status cannot be null.");
        }


        // Ensure the account exists
        Account targetAccount = getAccount(accountNumber);
        if (targetAccount == null) {
            System.out.println("Account " + accountNumber + " not found. Cannot create reservation " + reservationNumber + ".");
            return false;
        }

        // Check for duplicate reservation number within this account (or globally if reservation numbers are unique across accounts)
        // For simplicity, checking within the target account. If reservation numbers are globally unique,
        // you'd need a broader check.
        for (Reservation res : targetAccount.getReservations()) {
            if (res.getReservationNumber().equals(reservationNumber)) {
                System.out.println("Reservation with number " + reservationNumber + " already exists for account " + accountNumber + ".");
                return false;
            }
        }


        Reservation newReservation;
        // Determine the type of reservation and instantiate the correct subclass
        switch (reservationType.toLowerCase()) {
            case "cabin":
                // Default optional boolean parameters to false if null is passed
                boolean finalHasKitchen = (hasKitchen != null) ? hasKitchen : false;
                boolean finalHasLoft = (hasLoft != null) ? hasLoft : false;
                newReservation = new Cabin_Reservation(
                        0, // id - assuming 0 for new creation as used in other constructors
                        accountNumber,
                        reservationNumber,
                        physicalAddress,
                        mailingAddress,
                        reservationStart,
                        nights,
                        beds,
                        bedrooms,
                        bathrooms,
                        squareFootage,
                        status,
                        reservationType,
                        finalHasKitchen,
                        finalHasLoft
                );
                break;
            case "hotel":
                // Default optional boolean parameter to false if null is passed
                boolean finalHasKitchenette = (hasKitchenette != null) ? hasKitchenette : false;
                newReservation = new Hotel_Reservation(
                        accountNumber,
                        reservationNumber,
                        physicalAddress,
                        mailingAddress,
                        reservationStart,
                        nights,
                        beds,
                        bedrooms,
                        bathrooms,
                        squareFootage,
                        status,
                        reservationType,
                        finalHasKitchenette
                );
                break;
            case "house":
                // Default optional integer parameter to 0 if null is passed
                int finalFloors = (floors != null) ? floors : 0;
                newReservation = new House_Reservation(
                        0, // id - assuming 0 for new creation
                        accountNumber,
                        reservationNumber,
                        physicalAddress,
                        mailingAddress,
                        reservationStart,
                        nights,
                        beds,
                        bedrooms,
                        bathrooms,
                        squareFootage,
                        status,
                        reservationType,
                        finalFloors
                );
                break;
            default:
                System.err.println("Unknown reservation type: " + reservationType + ". Reservation not created.");
                throw new IllegalArgumentException("Unknown reservation type: " + reservationType);
        }

        // Add the newly created reservation to the account's list
        this.addReservation(accountNumber, newReservation);
        System.out.println("Successfully created and added " + reservationType + " reservation " + reservationNumber + " to account " + accountNumber + ".");

        // Persist the changes to disk
        generateXmlFiles();
        return true;
    }


    /**
     * Finds the Reservation with the associated accountNumber & updates the value for reservationStatus to completed status
     */
    public boolean completeReservation(String accountNumber, String reservationNumber) {
        Account account = getAccount(accountNumber);
        if (account == null) {
            System.out.println("Account " + accountNumber + " not found. Cannot complete reservation " + reservationNumber + ".");
            return false;
        }

        for (Reservation res : account.getReservations()) {
            if (res.getReservationNumber().equals(reservationNumber)) {
                res.setStatus(ReservationStatus.COMPLETED);
                System.out.println("Reservation " + reservationNumber + " for account " + accountNumber + " marked as COMPLETED.");
                generateXmlFiles(); // Persist the change
                return true;
            }
        }
        System.out.println("Reservation " + reservationNumber + " not found for account " + accountNumber + ". Cannot complete.");
        return false;
    }

    /**
     * Finds the Reservation with the associated accountNumber & updates the value for reservationStatus to cancelled status
     */
    public boolean cancelReservation(String accountNumber, String reservationNumber) {
        Account account = getAccount(accountNumber);
        if (account == null) {
            System.out.println("Account " + accountNumber + " not found. Cannot cancel reservation " + reservationNumber + ".");
            return false;
        }

        for (Reservation res : account.getReservations()) {
            if (res.getReservationNumber().equals(reservationNumber)) {
                res.setStatus(ReservationStatus.CANCELLED);
                System.out.println("Reservation " + reservationNumber + " for account " + accountNumber + " marked as CANCELLED.");
                generateXmlFiles(); // Persist the change
                return true;
            }
        }
        System.out.println("Reservation " + reservationNumber + " not found for account " + accountNumber + ". Cannot cancel.");
        return false;
    }

    /** Allows a Reservation's properties to be modified, is */
    public boolean changeReservation(String accountNumber, Reservation reservation) {
        //TODO: Modify this method to accept arguments for changing specific Reservation attributes by type
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new NullPointerException("Account number cannot be null or empty for changeReservation operation.");
        }
        if (reservation == null) {
            throw new NullPointerException("New reservation object cannot be null for changeReservation operation.");
        }
        if (reservation.getReservationNumber() == null || reservation.getReservationNumber().trim().isEmpty()) {
            throw new NullPointerException("Reservation number in the new reservation object cannot be null or empty.");
        }

        Account account = getAccount(accountNumber);
        if (account == null) {
            System.out.println("Account " + accountNumber + " not found. Cannot change reservation " + reservation.getReservationNumber() + ".");
            return false;
        }

        Reservation existingReservation = null;
        List<Reservation> accountReservations = account.getReservations();
        int indexOfExistingReservation = -1;

        for (int i = 0; i < accountReservations.size(); i++) {
            if (accountReservations.get(i).getReservationNumber().equals(reservation.getReservationNumber())) {
                existingReservation = accountReservations.get(i);
                indexOfExistingReservation = i;
                break;
            }
        }

        if (existingReservation == null) {
            System.out.println("Reservation " + reservation.getReservationNumber() + " not found for account " + accountNumber + ". Cannot change.");
            return false;
        }

        //Check conditions for changes (Cancelled, Completed, Past Date) ---
        String reasonForFailure = null;
        if (existingReservation.getStatus() == ReservationStatus.CANCELLED) {
            reasonForFailure = "Reservation is Cancelled.";
        } else if (existingReservation.getStatus() == ReservationStatus.COMPLETED) {
            reasonForFailure = "Reservation is Completed.";
        } else if (existingReservation.getReservationStart().before(new Date())) { // Check if start date is in the past
            reasonForFailure = "Reservation date is in the past.";
        }

        if (reasonForFailure != null) {
            String errorMessage = String.format("Operation: Change Reservation, Account ID: %s, Reservation Number: %s, Reason: %s",
                    accountNumber, reservation.getReservationNumber(), reasonForFailure);
            throw new IllegalOperationException(errorMessage);
        }

        // --- Update Common Reservation Properties ---
        // These updates only proceed if the checks above pass
        existingReservation.setPhysicalAddress(reservation.getPhysicalAddress());
        existingReservation.setMailingAddress(reservation.getMailingAddress());
        existingReservation.setReservationStart(reservation.getReservationStart());
        existingReservation.setNights(reservation.getNights());
        existingReservation.setBeds(reservation.getBeds());
        existingReservation.setBedrooms(reservation.getBedrooms());
        existingReservation.setBathrooms(reservation.getBathrooms());
        existingReservation.setSquareFootage(reservation.getSquareFootage());
        existingReservation.setStatus(reservation.getStatus());
        existingReservation.setReservationType(reservation.getReservationType());

        // --- Update Subclass-Specific Properties ---
        if (existingReservation instanceof Cabin_Reservation && reservation instanceof Cabin_Reservation) {
            Cabin_Reservation existingCabin = (Cabin_Reservation) existingReservation;
            Cabin_Reservation newCabinDetails = (Cabin_Reservation) reservation;
            existingCabin.setHasKitchen(newCabinDetails.isHasKitchen());
            existingCabin.setHasLoft(newCabinDetails.isHasLoft());
            System.out.println("  Updated Cabin_Reservation specific details.");
        } else if (existingReservation instanceof Hotel_Reservation && reservation instanceof Hotel_Reservation) {
            Hotel_Reservation existingHotel = (Hotel_Reservation) existingReservation;
            Hotel_Reservation newHotelDetails = (Hotel_Reservation) reservation;
            existingHotel.isHasKitchenette();
            System.out.println("  Updated Hotel_Reservation specific details.");
        } else if (existingReservation instanceof House_Reservation && reservation instanceof House_Reservation) {
            House_Reservation existingHouse = (House_Reservation) existingReservation;
            House_Reservation newHouseDetails = (House_Reservation) reservation;
            existingHouse.setFloors(newHouseDetails.getFloors());
            System.out.println("  Updated House_Reservation specific details.");
        } else {
            System.out.println("  Warning: Type mismatch or unhandled reservation type for update. Existing: " + existingReservation.getClass().getSimpleName() + ", New: " + reservation.getClass().getSimpleName());
        }

        accountReservations.set(indexOfExistingReservation, existingReservation);
        account.setReservations(accountReservations);

        System.out.println("Reservation " + reservation.getReservationNumber() + " for account " + accountNumber + " changed successfully.");

        generateXmlFiles();
        return true;
    }


}