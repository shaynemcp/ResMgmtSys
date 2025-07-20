package com.umgc.swen646;

import com.umgc.swen646.excpetions.IllegalOperationException;
import com.umgc.swen646.excpetions.SameAccountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    private Manager manager;
    private Account sampleAccount;
    private Reservation sampleReservation;

    @BeforeEach
    void setUp() {
        manager = new Manager();
        sampleAccount = new Account("ac-123456789", "123 Main St", "555-1234", "test@test.com", new ArrayList<>());
        sampleReservation = new Cabin_Reservation(sampleAccount.getAccountNumber(), "res-987654321", "456 Cabin Rd",
                "123 Main St", new Date(System.currentTimeMillis() + 86400000), 5, 2, 1, 1, 800,
                ReservationStatus.PENDING, "Cabin", false, false);
    }

    @Nested
    @DisplayName("1. Account Management Tests")
    class AccountManagement {
        @Test
        @DisplayName("testAddAccount_Success: Add a valid new account")
        void testAddAccount_Success() {
            manager.addAccount(sampleAccount);
            assertEquals(1, manager.getAccounts().size());
            assertEquals(sampleAccount, manager.getAccount("ac-123456789"));
        }

        @Test
        @DisplayName("testAddAccount_Failure_DuplicateAccountNumber: Throws exception for duplicate account")
        void testAddAccount_Failure_DuplicateAccountNumber() {
            manager.addAccount(sampleAccount);
            assertThrows(SameAccountException.class, () -> manager.addAccount(sampleAccount));
        }

        @Test
        @DisplayName("testUpdateAccount_Success_AllFields: Update all fields of an account")
        void testUpdateAccount_Success_AllFields() {
            manager.addAccount(sampleAccount);
            UpdateAccountBuilder builder = new UpdateAccountBuilder(sampleAccount.getAccountNumber())
                    .withNewEmail("new.email@test.com")
                    .withNewMailingAddress("456 New Address")
                    .withNewPhoneNumber("555-5678");
            manager.updateAccount(builder);

            Account updatedAccount = manager.getAccount(sampleAccount.getAccountNumber());
            assertEquals("new.email@test.com", updatedAccount.getEmail());
            assertEquals("456 New Address", updatedAccount.getMailingAddress());
            assertEquals("555-5678", updatedAccount.getPhoneNumber());
        }

        @Test
        @DisplayName("testGetAccount_Success: Retrieve an existing account")
        void testGetAccount_Success() {
            manager.addAccount(sampleAccount);
            Account foundAccount = manager.getAccount("ac-123456789");
            assertNotNull(foundAccount);
            assertEquals("ac-123456789", foundAccount.getAccountNumber());
        }
    }

    @Nested
    @DisplayName("2. Reservation Management & Business Rules")
    class ReservationManagement {

        @BeforeEach
        void setup() {
            manager.addAccount(sampleAccount);
        }

        @Test
        @DisplayName("testCreateReservation_Success_AllTypes: Create Hotel, Cabin, and House reservations")
        void testCreateReservation_Success_AllTypes() {
            // Test creating a Cabin
            boolean cabinResult = manager.createReservation(sampleAccount.getAccountNumber(), "res-cabin-1", "Cabin Address",
                    "Mailing Address", new Date(), 3, 2, 1, 1, 950,
                    ReservationStatus.PENDING, "cabin", true, true, null, null);
            assertTrue(cabinResult);

            // Test creating a Hotel
            boolean hotelResult = manager.createReservation(sampleAccount.getAccountNumber(), "res-hotel-1", "Hotel Address",
                    "Mailing Address", new Date(), 2, 1, 1, 1, 500,
                    ReservationStatus.PENDING, "hotel", null, null, true, null);
            assertTrue(hotelResult);

            // Test creating a House
            boolean houseResult = manager.createReservation(sampleAccount.getAccountNumber(), "res-house-1", "House Address",
                    "Mailing Address", new Date(), 7, 4, 3, 2, 2000,
                    ReservationStatus.PENDING, "house", null, null, null, 2);
            assertTrue(houseResult);

            assertEquals(3, manager.getAccount(sampleAccount.getAccountNumber()).getReservations().size());
        }

        @Test
        @DisplayName("testChangeReservation_Failure_AlreadyCancelled: Throws exception when changing a cancelled reservation")
        void testChangeReservation_Failure_AlreadyCancelled() {
            sampleReservation.setStatus(ReservationStatus.CANCELLED);
            manager.addReservation(sampleAccount.getAccountNumber(), sampleReservation);

            assertThrows(IllegalOperationException.class, () -> manager.changeReservation(sampleAccount.getAccountNumber(), sampleReservation));
        }

        @Test
        @DisplayName("testChangeReservation_Failure_PastDate: Throws exception when changing a reservation with a past date")
        void testChangeReservation_Failure_PastDate() {
            sampleReservation.setReservationStart(new Date(System.currentTimeMillis() - 86400000)); // Yesterday
            manager.addReservation(sampleAccount.getAccountNumber(), sampleReservation);
            assertThrows(IllegalOperationException.class, () -> manager.changeReservation(sampleAccount.getAccountNumber(), sampleReservation));
        }

        @Test
        @DisplayName("testCancelReservation_Success_FutureDate: Cancel a reservation with a future date")
        void testCancelReservation_Success_FutureDate() {
            manager.addReservation(sampleAccount.getAccountNumber(), sampleReservation);
            boolean result = manager.cancelReservation(sampleAccount.getAccountNumber(), sampleReservation.getReservationNumber());
            assertTrue(result);
            assertEquals(ReservationStatus.CANCELLED, manager.getReservation(sampleAccount.getAccountNumber(), sampleReservation.getReservationNumber()).getStatus());
        }
    }

    @Nested
    @DisplayName("4. Data Persistence (File I/O) Tests")
    class DataPersistence {

        @TempDir
        Path tempDir;

        @BeforeEach
        void setup() {
            manager.DATA_DIRECTORY = tempDir.toString();
        }

        @Test
        @DisplayName("testLoadData_Success_EmptyDirectory: Load from an empty directory")
        void testLoadData_Success_EmptyDirectory() {
            manager.loadData();
            assertTrue(manager.getAccounts().isEmpty());
        }

        @Test
        @DisplayName("testGenerateXmlFiles_Success: Generate XML files for accounts and reservations")
        void testGenerateXmlFiles_Success() throws IOException {
            manager.addAccount(sampleAccount);
            manager.addReservation(sampleAccount.getAccountNumber(), sampleReservation);
            manager.generateXmlFiles();

            File accountDir = new File(tempDir.toString(), sampleAccount.getAccountNumber());
            assertTrue(accountDir.exists());
            assertTrue(accountDir.isDirectory());

            File accountFile = new File(accountDir, "acc-" + sampleAccount.getAccountNumber() + ".xml");
            assertTrue(accountFile.exists());

            File reservationFile = new File(accountDir, "res-" + sampleReservation.getReservationNumber() + ".xml");
            assertTrue(reservationFile.exists());
        }

        @Test
        @DisplayName("testDataPersistence_AfterUpdate: Load updated data correctly")
        void testDataPersistence_AfterUpdate() throws IOException {
            // 1. Add account and save
            manager.addAccount(sampleAccount);
            manager.generateXmlFiles();

            // 2. Create a new manager instance and load the data
            Manager newManager = new Manager();
            newManager.DATA_DIRECTORY = tempDir.toString();
            newManager.loadData();
            assertEquals(1, newManager.getAccounts().size());
            assertEquals("test@test.com", newManager.getAccount(sampleAccount.getAccountNumber()).getEmail());

            // 3. Update the account and save again
            UpdateAccountBuilder builder = new UpdateAccountBuilder(sampleAccount.getAccountNumber()).withNewEmail("updated.email@test.com");
            manager.updateAccount(builder);
            manager.generateXmlFiles();

            // 4. Create another manager instance and load the updated data
            Manager finalManager = new Manager();
            finalManager.DATA_DIRECTORY = tempDir.toString();
            finalManager.loadData();

            assertEquals(1, finalManager.getAccounts().size());
            assertEquals("updated.email@test.com", finalManager.getAccount(sampleAccount.getAccountNumber()).getEmail());
        }
    }
}