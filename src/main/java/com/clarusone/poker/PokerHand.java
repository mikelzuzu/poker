package com.clarusone.poker;

import com.clarusone.poker.model.Card;
import com.clarusone.poker.model.HandInfo;
import com.clarusone.poker.model.HandRank;
import com.clarusone.poker.utils.HandUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PokerHand implements Comparable<PokerHand> {

    private final List<Card> cards;
    private final HandInfo handInfo;

    public PokerHand(String fiveCards) {
        cards = Arrays.stream(fiveCards.split("\\s+"))
                .map(card -> {
                    if (card.length() == 2) {
                        char rank = card.charAt(0);
                        char suit = card.charAt(1);
                        return new Card(rank, suit);
                    } else {
                        throw new IllegalArgumentException();
                    }

                })
                .collect(Collectors.toList());

        handInfo = HandUtils.buildHandInfo(cards);

        //It would be interesting to check add card verification for such as duplicate cards.
    }

    @Override
    public int compareTo(PokerHand opponentHand) {
        //for now I am returning TIE but we might need to throw and exception as two players should not have same cards!
        if (HandUtils.duplicateCards(this.cards, opponentHand.cards)) {
            return HandResult.TIE.comparatorValue;
        }

        int handValue = Integer.compare(handInfo.getHandRank().getValue(),
                opponentHand.handInfo.getHandRank().getValue());
        if (handValue != 0) {
            return handValue;
        } else {
            //they have the same hand value so need to check if it's a tie or not
            return eventTie(opponentHand);
        }

        //return the result here. for example tie
        //return HandResult.TIE.comparatorValue;
    }

    private int eventTie(PokerHand opponentHand) {
        switch (handInfo.getHandRank()) {
            case ROYAL_FLUSH:
            case STRAIGHT_FLUSH:
            case STRAIGHT:
                //Find the highest number for winner
                return handInfo.getCards().get(0).compareTo(opponentHand.handInfo.getCards().get(0));
            case FOUR_OF_A_KIND:
            case THREE_OF_A_KIND:
            case TWO_PAIR:
            case FULL_HOUSE:
            case PAIR:
                //Check who highest duplicate or higher kicker
                boolean occurrence = true;
                if (handInfo.getHandRank().equals(HandRank.TWO_PAIR)) {
                    occurrence = false;
                }
                List<Card> playerDuplicates = HandUtils.getDuplicates(handInfo.getCardsByRank(), occurrence);
                List<Card> opponentDuplicates = HandUtils.getDuplicates(opponentHand.handInfo.getCardsByRank(), occurrence);
                if (HandUtils.compareHands(playerDuplicates, opponentDuplicates) != 0) {
                    return HandUtils.compareHands(playerDuplicates, opponentDuplicates);
                }
                //if they have same duplicate, find out higher kicker
                List<Card> playerKicker = HandUtils.getKickers(handInfo.getCardsByRank());
                List<Card> opponentKicker = HandUtils.getKickers(opponentHand.handInfo.getCardsByRank());
                return HandUtils.compareHands(playerKicker, opponentKicker);
            case HIGH_CARD:
            case FLUSH:
                //Find highest cars
                return HandUtils.compareHands(handInfo.getCards(), opponentHand.handInfo.getCards());
        }
        return HandResult.TIE.comparatorValue;
    }
}
