package com.clarusone.poker.model;

import java.util.List;
import java.util.Map;

public class HandInfo {
    private final HandRank handRank;
    private final Map<Suit, List<Card>> cardsBySuit;
    private final Map<Rank, List<Card>> cardsByRank;
    private final boolean isStraight;
    private List<Card> cards;

    public HandInfo(List<Card> cards, HandRank handRank, Map<Suit, List<Card>> cardsBySuit, Map<Rank, List<Card>> cardsByRank, boolean isStraight) {
        this.cards = cards;
        this.handRank = handRank;
        this.cardsBySuit = cardsBySuit;
        this.cardsByRank = cardsByRank;
        this.isStraight = isStraight;
    }

    //for now I don't need it but in case is there
    public List<Card> getCards() {
        return cards;
    }

    public HandRank getHandRank() {
        return handRank;
    }

    public Map<Suit, List<Card>> getCardsBySuit() {
        return cardsBySuit;
    }

    public Map<Rank, List<Card>> getCardsByRank() {
        return cardsByRank;
    }

    public boolean isStraight() {
        return isStraight;
    }
}
