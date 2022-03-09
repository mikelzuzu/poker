package com.clarusone.poker.utils;

import com.clarusone.poker.HandResult;
import com.clarusone.poker.model.Card;
import com.clarusone.poker.model.HandInfo;
import com.clarusone.poker.model.HandRank;
import com.clarusone.poker.model.Rank;
import com.clarusone.poker.model.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class HandUtils {

    public static HandInfo buildHandInfo(List<Card> cards) {
        //Capacity is based on that we are expecting to have 5 crads
        Map<Suit, List<Card>> cardsBySuit = new HashMap<>(4);
        Map<Rank, List<Card>> cardsByRank = new HashMap<>(4);
        //copying a list to another to sort it. In case we need initial order
        //Java 11 : List<Card> allCards = List.copyOf(cards);
        List<Card> allCards = cards.stream().collect(Collectors.toList());
        Collections.sort(allCards);
        boolean isStraigth = false;
        int index = 0;
        int consecutiveCards = 0;
        //Iterate over all cards to separate them by suits and by rank.
        // In the same loop find if it is straight
        for (Card card : allCards) {
            List<Card> bySuits = cardsBySuit.computeIfAbsent(card.getSuit(), x -> new ArrayList<>());
            List<Card> byRank = cardsByRank.computeIfAbsent(card.getRank(), x -> new ArrayList<>());
            bySuits.add(card);
            byRank.add(card);
            if (!isStraigth && index < allCards.size() - 1 && (card.getRank().getWeight() - allCards.get(index + 1).getRank().getWeight() == 1)) {
                consecutiveCards++;
                if (consecutiveCards == 4) {
                    //Double check that highest is lower or equal than ACE and lowest higher or equal than 2
                    if (allCards.get(0).getRank().getWeight() < Rank.ACE.getWeight()) {

                    }
                    isStraigth = true;
                }
            }
            index++;
        }

        HandRank rank = getRank(allCards, cardsBySuit, cardsByRank, isStraigth);
        return new HandInfo(allCards, rank, cardsBySuit, cardsByRank, isStraigth);
    }

    public static HandRank getRank(List<Card> allCards, Map<Suit, List<Card>> cardsBySuit, Map<Rank, List<Card>> cardsByRank, boolean isStraigth) {
        if (allCards.size() != 5) throw new IllegalArgumentException("You have to pass 5 cards");
        if (isRoyalFlush(allCards, cardsBySuit, isStraigth)) {
            return HandRank.ROYAL_FLUSH;
        } else if (isStraightFlush(cardsBySuit, isStraigth)) {
            return HandRank.STRAIGHT_FLUSH;
        } else if (isFourOfAKind(cardsByRank)) {
            return HandRank.FOUR_OF_A_KIND;
        } else if (isFullHouse(cardsByRank)) {
            return HandRank.FULL_HOUSE;
        } else if (isFlush(cardsBySuit)) {
            return HandRank.FLUSH;
        } else if (isStraigth) {
            return HandRank.STRAIGHT;
        } else if (isThreeOfKind(cardsByRank)) {
            return HandRank.THREE_OF_A_KIND;
        } else if (isTwoPair(cardsByRank)) {
            return HandRank.TWO_PAIR;
        } else if (isPair(cardsByRank)) {
            return HandRank.PAIR;
        } else {
            return HandRank.HIGH_CARD;
        }
    }

    private static boolean isRoyalFlush(List<Card> allCards, Map<Suit, List<Card>> cardsBySuit, boolean isStraigth) {
        return isStraightFlush(cardsBySuit, isStraigth) &&
                allCards.get(0).getRank().equals(Rank.ACE);
    }


    private static boolean isStraightFlush(Map<Suit, List<Card>> cardsBySuit, boolean isStraigth) {
        return isStraigth && !(cardsBySuit.size() > 1);
    }

    private static boolean isFlush(Map<Suit, List<Card>> cardsBySuit) {

        return cardsBySuit.size() == 1;
    }

    private static boolean isFourOfAKind(Map<Rank, List<Card>> cardsByRank) {

        if (cardsByRank.size() == 2) {
            for (List<Card> cards : cardsByRank.values()) {
                if (cards.size() == 4) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isFullHouse(Map<Rank, List<Card>> cardsByRank) {
        if (cardsByRank.size() == 2) {
            boolean hasPair = false;
            boolean hasThreeOfKind = false;
            for (List<Card> cards : cardsByRank.values()) {
                if (cards.size() == 2) {
                    hasPair = true;
                } else if (cards.size() == 3) {
                    hasThreeOfKind = true;
                }
            }
            return hasPair && hasThreeOfKind;
        }
        return false;
    }

    private static boolean isThreeOfKind(Map<Rank, List<Card>> cardsByRank) {
        if (cardsByRank.size() == 3) {
            for (List<Card> cards : cardsByRank.values()) {
                if (cards.size() == 3) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isTwoPair(Map<Rank, List<Card>> cardsByRank) {
        if (cardsByRank.size() == 3) {
            int pairs = 0;
            for (List<Card> cards : cardsByRank.values()) {
                if (cards.size() == 2) {
                    pairs++;
                }
            }
            return pairs == 2;
        }
        return false;
    }

    private static boolean isPair(Map<Rank, List<Card>> cardsByRank) {
        if (cardsByRank.size() == 4) {
            for (List<Card> cards : cardsByRank.values()) {
                if (cards.size() == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean duplicateCards(List<Card> card1, List<Card> card2) {
        return card1.containsAll(card2);
    }

    //Compare which hand has higher card

    /**
     * Compare which hand has higher card
     *
     * @param player hand for player
     * @param opponent
     * @return int for the one with higher card
     */
    public static int compareHands(List<Card> player, List<Card> opponent) {
        for (int i = 0; i < player.size(); ++i) {
            if (player.get(i).compareTo(opponent.get(i)) != 0) {
                //This is this way around because I have custom comparator to later sort from higher to lower
                return opponent.get(i).compareTo(player.get(i));
            }
        }
        return HandResult.TIE.comparatorValue;
    }

    /**
     * Get the list of the rank of the cards that are duplicated
     * for example: with we have "2S 2H 4H 5S 4C" we will get back a list of {4, 2}.
     * We won't get back just a number but it does not matter which suit is coming back next to the rank.
     * Previus example is if the sortByOccurrence is not enabled but if it is it would be first the one repeated more times:
     * "KS KH KD 2S 2C" we will get back {K 2} again with a suit that it is not important for us
     *
     * @param cardsByRank map of cards by rank
     * @param sortByOccurrence boolean to use for sorting
     * @return list of cards that are duplicated
     */
    public static List<Card> getDuplicates(Map<Rank, List<Card>> cardsByRank, boolean sortByOccurrence) {
        List<Card> cards;
        Map<Card, Integer> cardDuplication = new TreeMap<>();
        for (List<Card> cardList : cardsByRank.values()) {
            if (cardList.size() > 1) {
                cardDuplication.put(cardList.get(0), cardList.size());
            }
        }
        //Sort the map by the number of duplications or by the rank of the card
        //When we have more than one pair we want by rank of card to start compering from the highest one
        if (sortByOccurrence) {
            cards = cardDuplication.entrySet()
                    .stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .map(x -> x.getKey())
                    .collect(Collectors.toList());
        } else {
            cards = new LinkedList<>(cardDuplication.keySet());
            Collections.sort(cards);
        }

        return cards;
    }

    /**
     * Kickers are the cards that are not part of pairs or things like that
     * They are kind of left overs that you use in the event of tie
     *
     * @param cardsByRank map of cards by rank
     * @return list of cards that are not matching other cards ranks
     */
    public static List<Card> getKickers(Map<Rank, List<Card>> cardsByRank) {
        List<Card> kickers = new ArrayList<>();
        for (List<Card> cards : cardsByRank.values()) {
            if (cards.size() == 1) {
                kickers.add(cards.get(0));
            }
        }
        return kickers;
    }
}
