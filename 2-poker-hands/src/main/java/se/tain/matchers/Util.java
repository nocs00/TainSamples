package se.tain.matchers;

import se.tain.Card;
import se.tain.Player;
import se.tain.matchers.model.Combination;
import se.tain.matchers.model.Hand;
import se.tain.matchers.model.CombinationValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

    public static final List<String> FACES = Arrays.asList("2,3,4,5,6,7,8,9,T,J,Q,K,A".split(","));
    public static final List<String> SUITES = Arrays.asList("H,S,C,D".split(","));

    public static String getCard(int face, int suite) {
        return FACES.get(face) + SUITES.get(suite);
    }

    public static int getFaceCost(String card) {
        return FACES.indexOf(card.substring(0, 1));
    }

    public static int getSuiteCost(String card) {
        return SUITES.indexOf(card.substring(1));
    }

    public static int[] getCardsCost(List<String> cards) {
        int[] cardsCost = new int[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            cardsCost[i] = getCardCost(cards.get(i));
        }
        return cardsCost;
    }

    public static int getCardCost(String card) {
        int face = getFaceCost(card);
        int suite = getSuiteCost(card);
        return face + (suite * 13);
    }

    public static boolean isEmpty(int[] array) {
        return !notEmpty(array);
    }

    public static boolean notEmpty(int[] array) {
        return array != null && array.length > 0;
    }

    public static boolean assertSize(int[] array, int size) {
        return notEmpty(array) && array.length >= size;
    }

    public static Hand sortHand(int[] hand) {
        int[] card = new int[hand.length];
        int[] suite = new int[hand.length];
        int[] suiteCount = new int[4];

        //desc sort
        for (int i = 0; i < hand.length; i++) {
            for (int j = 0; j < hand.length - 1; j++) {
                int t;
                if (hand[j] % 13 < hand[j + 1] % 13) {
                    t = hand[j + 1];
                    hand[j + 1] = hand[j];
                    hand[j] = t;
                }
                if ((hand[j] % 13 == hand[j + 1] % 13) &&
                        (hand[j] < hand[j + 1])) {
                    t = hand[j + 1];
                    hand[j + 1] = hand[j];
                    hand[j] = t;
                }
            }
        }
        //assignments
        for (int i = 0; i < hand.length; i++) {
            card[i] = hand[i] % 13; // 0 .. 12
            suite[i] = hand[i] / 13; //0 .. 3
            suiteCount[suite[i]]++;
        }

        return new Hand(card, suite, suiteCount);
    }

    public static se.tain.Hand calcWinningHand(Player player, Combination combination,
                                                List<String> table) {
        se.tain.Hand winningHand = new se.tain.Hand(Collections.EMPTY_LIST, combination.getName());
        if (combination.getName().endsWith(CombinationValue.SET.name())) { //only SET implemented

            List<String> handCards = player.getHoleCards().rawCards();
            List<String> combinationCards = combination.getCombinationCards();

            List<String> winningCards = new ArrayList<>(handCards);

            for (String cc : combinationCards) {
                if (!handCards.contains(cc)) winningCards.add(cc);
            }

            int position = 0;
            while (winningCards.size() < 5) {
                Hand hand = sortHand(getCardsCost(table));
                int face = hand.getCard()[position];
                int suite = hand.getSuite()[position];
                winningCards.add(getCard(face,position++));
            }

            winningHand = new se.tain.Hand(
                    winningCards.stream().map(raw -> Card.valueOf(raw)).collect(Collectors.toList()),
                    combination.getName());
        }

        return winningHand;
    }
}
