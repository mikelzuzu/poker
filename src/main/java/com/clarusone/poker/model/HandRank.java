package com.clarusone.poker.model;

public enum HandRank {
    HIGH_CARD(0),
    PAIR(1),
    TWO_PAIR(2),
    THREE_OF_A_KIND(3),
    STRAIGHT(4),
    FLUSH(5),
    FULL_HOUSE(6),
    FOUR_OF_A_KIND(7),
    STRAIGHT_FLUSH(8),
    ROYAL_FLUSH(9);

    private final int value;

    HandRank(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
