package com.umgc.swen646;

/**
 * Allows the com.umgc.swen646.Manager class to update the current status of a com.umgc.swen646.Reservation with
 * "pending", "confirmed", "cancelled", or "completed"
 */
public enum ReservationStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED
}