package com.umgc.swen646;

public class SameAccountException extends RuntimeException{

    public String accounNumber;

    public SameAccountException(String accountNumber){
        super("This account number matches an account that already exists: " + accountNumber);
        this.accounNumber = accountNumber;
    }

    public String toString(){
        return this.getClass().getSimpleName() + ": This reservation number matches a hotel reservation that already exists: " + accounNumber;
    }
}
