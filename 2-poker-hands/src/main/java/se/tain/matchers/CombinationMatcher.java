package se.tain.matchers;

import se.tain.Player;
import se.tain.matchers.model.Combination;
import se.tain.matchers.model.Hand;
import se.tain.matchers.model.CombinationValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static se.tain.matchers.Util.*;
import static se.tain.matchers.Util.FACES;
import static se.tain.matchers.Util.SUITES;

public class CombinationMatcher {

    public static Combination getCombination(Player player, List<String> cardsOnTable) {
        List<String> allCards = Stream
                .concat(cardsOnTable.stream(), player.getHoleCards().rawCards().stream())
                .collect(Collectors.toList());

        int[] cards = getCardsCost(allCards);

        Combination combination = getCombination(cards);
        player.setWinningHand(calcWinningHand(player, combination, cardsOnTable));
        combination.setPlayer(player);
        return combination;
    }

    /** encoding explanation
     * int hand_code = (suite * 13) + card
     *
     * card         [2,3,4,5,6,7,8,9,T,J,Q,K,A] = [0..12]
     * suite        [H,S,C,D]                   = [0..3]
     * hand_code                                = [0..51]
     *
     * example:
     * 2H = 0 + (0*13) = 0
     * JC = 9 + (2 * 13) = 35
     */
    private static Combination getCombination(int[] allCards) {

        Hand currentHand = sortHand(allCards);

        int[] card = currentHand.getCard();
        int[] suite = currentHand.getSuite();
        int[] suiteCount = currentHand.getSuiteCount();

        int result;
        Combination combination = null;

        result = isRoyalFlush(card, suite, suiteCount);
        if (result != -1) {
            return new Combination(117, "RoyalFlush");
        }
        result = isStraightFlush(card, suite, suiteCount);
        if (result != -1) {
            return new Combination(104 + result, "StraightFlush");
        }
        result = isQuads(card);
        if (result != -1) {
            return new Combination(91 + result, "Quads");
        }
        result = isFullHouse(card);
        if (result != -1) {
            return new Combination(78 + result, "FullHouse");
        }
        result = isFlush(card, suite, suiteCount);
        if (result != -1) {
            return new Combination(65 + result, "Flush");
        }
        result = isStraight(card);
        if (result != -1) {
            return new Combination(52 + result, "Straight");
        }
        combination = isSet(card, suite);
        if (combination != null) {
            return combination;
        }
//        result = isSet(card);
//        if (result != -1) {
//            return new Combination(39 + result, "Set");
//        }
        result = isTwoPair(card);
        if (result != -1) {
            return new Combination(26 + result, "TwoPair");
        }
        result = isOnePair(card);
        if (result != -1) {
            return new Combination(13 + result, "OnePair");
        }
        result = isHighCard(card);
        if (result != -1) {
            return new Combination(result, "HighCard");
        }
        return new Combination(-1, "NoCombination");
    }

    /** all below methods
     * @return  -1 .. 12 , where -1 : combination not found; 0..12 value of higher card in combination
     */
    protected static int isHighCard(int[] card) { //no other combinations matched, higher face in hand
        return notEmpty(card) ? card[0] : -1;
    }

    protected static int isOnePair(int[] card) { //2 cards of matching rank
        if (!assertSize(card, 2)) return -1;

        int prevCard = card[0];
        for (int i = 1; i < card.length; i++) {
            if (card[i] == prevCard) return prevCard;
            prevCard = card[i];
        }

        return -1;
    }

    protected static int isTwoPair(int[] card) { //2 cards of matching rank and another 2 card matching rank
        if (!assertSize(card, 4)) return -1;

        int firstPair = -1;
        int secondPair = -1;

        int prevCard = card[0];
        for (int i = 1; i < card.length; i++) {
            if (card[i] == prevCard) {
                if (firstPair == -1) {
                    firstPair = prevCard;
                    if (card.length > i+1) {
                        prevCard = card[++i];
                    }
                    else return -1;
                } else {
                    secondPair = prevCard;
                    return firstPair;
                }
            }

            prevCard = card[i];
        }

        return -1;
    }

    protected static Combination isSet(int[] card, int[] suite) { //3 cards same rank
        if (!assertSize(card, 3)) return null;

        List<String> combinationCards = new ArrayList<>();

        int sequence = 1;
        int prevCard = card[0];
        combinationCards.add(FACES.get(card[0]) + SUITES.get(suite[0]));
        for (int i = 1; i < card.length; i++) {
            if (prevCard == card[i]) {
                combinationCards.add(FACES.get(card[i]) + SUITES.get(suite[i]));
                if (++sequence == 3) {
                    return new Combination(prevCard + CombinationValue.SET.getValue(), CombinationValue.SET.name(), combinationCards);
                }
            } else {
                sequence = 1;
                combinationCards = new ArrayList<>(Arrays.asList(FACES.get(card[i]) + SUITES.get(suite[i])));
            }
            prevCard = card[i];
        }

        return null;
    }

//    protected static int isSet(int[] card) { //3 cards same rank
//        if (!assertSize(card, 3)) return -1;
//
//        int sequence = 1;
//        int prevCard = card[0];
//        for (int i = 1; i < card.length; i++) {
//            if (prevCard == card[i]) {
//                if (++sequence == 3) {
//                    return prevCard;
//                }
//            } else {
//                prevCard = card[i];
//                sequence = 1;
//            }
//        }
//
//        return -1;
//    }

    protected static int isStraight(int[] card) { //5 cards in sequence
        if (!assertSize(card, 5)) return -1;

        int straightPower = -1;
        int sequence = 1;
        int prevCard = card[0];
        for (int i = 1; i < card.length; i++) {

            if (prevCard == card[i]) continue;
            if (prevCard - 1 > card[i]) {
                sequence = 1;
                prevCard = card[i];
                continue;
            }

            if (prevCard - 1 == card[i]) {
                if (straightPower == -1) straightPower = prevCard;

                if (++sequence == 5) {
                    return straightPower;
                }
                prevCard = card[i];
            }
        }

        return -1;
    }

    protected static int isFlush(int[] card, int[] suite, int[] suiteCount) { //5 cards same suite
        if (!assertSize(card, 5)) return -1;

        int winSuitesCount = 0;
        for (int i = 0; i < suiteCount.length; i++) {
            if (suiteCount[i] >= 5) {
                winSuitesCount++;
            }
        }

        if (winSuitesCount == 0) return -1;

        int[] winSuites = new int[winSuitesCount];
        int winSuiteIndex = 0;
        for (int i = 0; i < suiteCount.length; i++) {
            if (suiteCount[i] >= 5) {
                winSuites[winSuiteIndex++] = i;
            }
        }

        int flushHighCard = -1;
        for (int s = 0; s < winSuites.length; s++) {
            int winSuite = winSuites[s];
            for (int i = 0; i < card.length; i++) {
                if (suite[i] == winSuite) {
                    if (flushHighCard < card[i]) flushHighCard = card[i];
                    break;//we need only first occurrence
                }
            }
        }

        return flushHighCard;
    }

    protected static int isFullHouse(int[] card) { //set and pair
        if (!assertSize(card, 5)) return -1;

        boolean isAfterPair = false;
        boolean pairFound = false;
        boolean setFound = false;
        int pairPower = -1;
        int setPower = -1;

        int sequence = 1;
        int prevCard = card[0];
        for (int i = 1; i < card.length; i++) {
            if (prevCard == card[i]) {
                sequence++;
                if (sequence == 2 && !isAfterPair) {
                    pairFound = true;
                    isAfterPair = true;
                    pairPower = prevCard;
                } else if (sequence == 3) {
                    if (isAfterPair) {
                        pairFound = false;
                        isAfterPair = false;
                        pairPower = -1;
                    }
                    setFound = true;
                    setPower = prevCard;
                    sequence = 0;
                }

                if (setFound && pairFound) {
                    return setPower;
                }
            } else {
                sequence = 1;
            }
            prevCard = card[i];
        }

        return -1;
    }

    protected static int isQuads(int[] card) { //4 cards same rank
        if (!assertSize(card, 4)) return -1;

        int sequence = 1;
        int prevCard = card[0];
        for (int i = 1; i < card.length; i++) {
            if (prevCard == card[i]) {
                if (++sequence == 4) {
                    return prevCard;
                }
            } else {
                prevCard = card[i];
                sequence = 1;
            }
        }

        return -1;
    }

    protected static int isStraightFlush(int[] card, int[] suite, int[] suiteCount) { //straight + flush
        if (!assertSize(card, 5)) return -1;

        int winSuitesCount = 0;
        for (int i = 0; i < suiteCount.length; i++) {
            if (suiteCount[i] >= 5) {
                winSuitesCount++;
            }
        }

        if (winSuitesCount == 0) return -1;

        int[] winSuites = new int[winSuitesCount];
        int winSuiteIndex = 0;
        for (int i = 0; i < suiteCount.length; i++) {
            if (suiteCount[i] >= 5) {
               winSuites[winSuiteIndex++] = i;
            }
        }
        //flushes found, looking for straight

        int straightHighCard = -1;

        for (int s = 0; s < winSuites.length; s++) {
            int winSuite = winSuites[s];
            int straightPower = -1;
            int sequence = 1;
            int prevCard = -1;
            for (int i = 1; i < card.length; i++) {

                if (suite[i-1] == winSuite) {
                    prevCard = card[i-1];
                    if (straightPower == -1) straightPower = prevCard;
                }
                if (prevCard - 1 > card[i]) {
                    sequence = 1;
                    prevCard = card[i];
                    continue;
                }
                if (suite[i] != winSuite) continue;
                if (prevCard == card[i]) continue;


                if (prevCard - 1 == card[i]) {
                    if (++sequence == 5 && straightHighCard < straightPower) {
                        straightHighCard = straightPower;
                    }
                    prevCard = card[i];
                }
            }
        }


        return straightHighCard;
    }

    protected static int isRoyalFlush(int[] card, int[] suite, int[] suiteCount) { //10 .. A straight flush
        int straightFlush = isStraightFlush(card, suite, suiteCount);
        if (straightFlush == 12) {
            return 12;
        }

        return -1;
    }

}
