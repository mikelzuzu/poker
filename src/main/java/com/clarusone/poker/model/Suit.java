package com.clarusone.poker.model;

public enum Suit {
    S("SPADES"),
    H("HEARTS"),
    D("DIAMONDS"),
    C("CLUBS");

    private String value;

    Suit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
