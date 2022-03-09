package com.clarusone.poker.model;

import java.util.HashMap;
import java.util.Map;

public enum Rank {
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("T", 10),
    JACK("J", 11),
    QUEEN("Q", 12),
    KING("K", 13),
    ACE("A", 14);

    private static final Map<String, Rank> BY_REAL_NUMBER = new HashMap<>();
    private static final Map<Integer, Rank> BY_WEIGHT = new HashMap<>();

    static {
        for (Rank r : values()) {
            BY_REAL_NUMBER.put(r.number, r);
            BY_WEIGHT.put(r.weight, r);
        }
    }

    private final String number;
    private final int weight;

    Rank(String number, int weight) {
        this.number = number;
        this.weight = weight;
    }

    public static Rank valueOfNumber(String number) {
        return BY_REAL_NUMBER.get(number);
    }

    public static Rank valueOfWeight(int weight) {
        return BY_WEIGHT.get(weight);
    }

    public String getNumber() {
        return number;
    }

    public int getWeight() {
        return weight;
    }
}