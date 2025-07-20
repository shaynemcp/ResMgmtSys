package com.umgc.swen646;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateAccountBuilderTest {

    private UpdateAccountBuilder builder;
    private final String accountNumber = "acc-test-123";

    @BeforeEach
    void setUp() {
        // Initialize a new builder before each test
        builder = new UpdateAccountBuilder(accountNumber);
    }

    @Test
    @DisplayName("Test constructor initializes account number correctly")
    void testConstructor() {
        assertNotNull(builder);
        assertEquals(accountNumber, builder.getAccountNumber(), "Constructor should set the account number.");
    }

    @Test
    @DisplayName("Test withNewMailingAddress() sets the address")
    void withNewMailingAddress() {
        String newAddress = "100 Builder Lane";
        builder.withNewMailingAddress(newAddress);
        assertEquals(newAddress, builder.getNewMailingAddress(), "Mailing address should be updated.");
    }

    @Test
    @DisplayName("Test withNewPhoneNumber() sets the phone number")
    void withNewPhoneNumber() {
        String newPhone = "555-888-9999";
        builder.withNewPhoneNumber(newPhone);
        assertEquals(newPhone, builder.getNewPhoneNumber(), "Phone number should be updated.");
    }

    @Test
    @DisplayName("Test withNewEmail() sets the email")
    void withNewEmail() {
        String newEmail = "builder@test.com";
        builder.withNewEmail(newEmail);
        assertEquals(newEmail, builder.getNewEmail(), "Email should be updated.");
    }

    @Test
    @DisplayName("Test method chaining for setting all properties")
    void testMethodChaining_AllProperties() {
        String newAddress = "200 Fluent Ave";
        String newPhone = "555-222-3333";
        String newEmail = "fluent@test.com";

        // Chain all 'with' methods together
        builder.withNewMailingAddress(newAddress)
                .withNewPhoneNumber(newPhone)
                .withNewEmail(newEmail);

        assertAll("Verify all properties are set via chaining",
                () -> assertEquals(newAddress, builder.getNewMailingAddress()),
                () -> assertEquals(newPhone, builder.getNewPhoneNumber()),
                () -> assertEquals(newEmail, builder.getNewEmail()),
                () -> assertEquals(accountNumber, builder.getAccountNumber(), "Account number should remain unchanged.")
        );
    }

    @Test
    @DisplayName("Test partial build with only one property set")
    void testPartialBuild() {
        String newEmail = "partial@test.com";
        builder.withNewEmail(newEmail);

        assertAll("Verify only specified properties are set",
                () -> assertEquals(newEmail, builder.getNewEmail()),
                () -> assertNull(builder.getNewMailingAddress(), "Unset mailing address should be null."),
                () -> assertNull(builder.getNewPhoneNumber(), "Unset phone number should be null.")
        );
    }

    @Test
    @DisplayName("Test build() method returns the same builder instance")
    void testBuild() {
        UpdateAccountBuilder builtInstance = builder.build();

        // The build() method should return the instance itself for the Manager to use
        assertSame(builder, builtInstance, "build() should return the same instance.");
    }
}
