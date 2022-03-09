package com.clarusone.poker.model;

public class Card implements Comparable<Card> {
    private Rank rank;
    private Suit suit;

    public Card(char rank, char suit) {
        this.rank = Rank.valueOfNumber(String.valueOf(rank));
        if (this.rank == null) {
            throw new IllegalArgumentException("Invalid rank.");
        }

        this.suit = Suit.valueOf(String.valueOf(suit));
        if (this.suit == null) {
            throw new IllegalArgumentException("Invalid suit.");
        }
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (rank != card.rank) return false;
        return suit == card.suit;
    }

    @Override
    public int hashCode() {
        int result = rank != null ? rank.hashCode() : 0;
        result = 31 * result + (suit != null ? suit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Card{" +
                "rank=" + rank +
                ", suit=" + suit +
                '}';
    }

    @Override
    public int compareTo(Card o) {
        //This is used to sort from highest to lowest, it is custom
        if (rank.getWeight() > o.rank.getWeight()) {
            return -1;
        } else if (rank.getWeight() < o.rank.getWeight()) {
            return 1;
        }
        return 0;
    }
}
