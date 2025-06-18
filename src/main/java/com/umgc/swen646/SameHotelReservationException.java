package com.umgc.swen646;


public class SameHotelReservationException extends RuntimeException{

    public String hotelRes;

    public SameHotelReservationException(String hotelRes){
        super("This reservation number matches a hotel reservation that already exists: " + hotelRes);
        this.hotelRes = hotelRes;
    }

    public String toString(){
        return this.getClass().getSimpleName() + ": This reservation number matches a hotel reservation that already exists: " + hotelRes;
    }
}
