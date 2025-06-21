package com.umgc.swen646;

public class UpdateAccountBuilder {
    private String accountNumber;
    private String newMailingAddress;
    private String newPhoneNumber;
    private String newEmail; // Assuming email is the desired optional field for Account update

    /**
     * Constructor for the builder. Account number is mandatory.
     * @param accountNumber The account number of the account to be updated.
     */
    public UpdateAccountBuilder(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Sets the new mailing address for the account.
     * @param newMailingAddress The new mailing address. Pass null if not changing.
     * @return The builder instance for chaining.
     */
    public UpdateAccountBuilder withNewMailingAddress(String newMailingAddress) {
        this.newMailingAddress = newMailingAddress;
        return this;
    }

    /**
     * Sets the new phone number for the account.
     * @param newPhoneNumber The new phone number. Pass null if not changing.
     * @return The builder instance for chaining.
     */
    public UpdateAccountBuilder withNewPhoneNumber(String newPhoneNumber) {
        this.newPhoneNumber = newPhoneNumber;
        return this;
    }

    /**
     * Sets the new email address for the account.
     * @param newEmail The new email address. Pass null if not changing.
     * @return The builder instance for chaining.
     */
    public UpdateAccountBuilder withNewEmail(String newEmail) {
        this.newEmail = newEmail;
        return this;
    }

    /**
     * Builds the UpdateAccountBuilder object itself. This object is then
     * passed to the Manager's updateAccount method to perform the update.
     * @return The configured UpdateAccountBuilder instance.
     */
    public UpdateAccountBuilder build() {
        return this;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getNewMailingAddress() {
        return newMailingAddress;
    }

    public void setNewMailingAddress(String newMailingAddress) {
        this.newMailingAddress = newMailingAddress;
    }

    public String getNewPhoneNumber() {
        return newPhoneNumber;
    }

    public void setNewPhoneNumber(String newPhoneNumber) {
        this.newPhoneNumber = newPhoneNumber;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }
}