package com.demo.microservicesproject.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountType {

    Savings("S"),
    Checking("C");

    private final String abbreviation;

    AccountType(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @JsonValue
    public String getAbbreviation() {
        return abbreviation;
    }

    @JsonCreator
    public static AccountType fromAbbreviation(String abbreviation) {
        for (AccountType type : values()) {
            if (type.abbreviation.equals(abbreviation)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid abbreviation: " + abbreviation);
    }

}
