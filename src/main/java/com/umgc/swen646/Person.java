package com.umgc.swen646;

import java.util.List;

public class Person {
    String name;

    Person(String name) {
        // validate parameter
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Please enter some value, name cannot be null or empty");

        this.name = name;  // assign parameter to attribute
    }

    // creates a clone of com.umgc.swen646.Person objcet, to be manipulated by user
    public Person clone() {
        return new Person(this.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return  "<person>" + name + "</person>" ;
    }
}
