package se.tain.matchers;

import org.junit.Test;
import se.tain.Player;
import se.tain.matchers.model.Combination;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static se.tain.matchers.model.CombinationValue.SET;

public class CombinationMatcherTest {


    
    @Test
    public void testGetCombination() throws Exception {
        Player player = null;
        List<String> cards = null;
        Combination combination = null;
        String name = null;

        //"2,3,4,5,6,7,8,9,T,J,Q,K,A"
        //"H,S,C,D"

        //royal flush
        player = new Player("test", "AHKH");
        cards = Arrays.asList("9S,9C,9D,9H,QH,JH,TH".split(","));
        combination = CombinationMatcher.getCombination(player, cards);
        name = combination.getName();
        assertEquals(name, "RoyalFlush");

        //straight flush
        player = new Player("test", "KH");
        cards = Arrays.asList("9S,9C,9D,9H,QH,JH,TH".split(","));
        combination = CombinationMatcher.getCombination(player, cards);
        name = combination.getName();
        assertEquals(name, "StraightFlush");

        //quads
        player = new Player("test", "AH");
        cards = Arrays.asList("9S,9C,9D,9H,QH,JH,TH".split(","));
        combination = CombinationMatcher.getCombination(player, cards);
        name = combination.getName();
        assertEquals(name, "Quads");

        //full house
        player = new Player("test", "9SQD");
        cards = Arrays.asList("9S,QH,JH,TH,QS,TS".split(","));
        combination = CombinationMatcher.getCombination(player, cards);
        name = combination.getName();
        assertEquals(name, "FullHouse");

        //flush
        player = new Player("test", "9DQD");
        cards = Arrays.asList("9S,QH,JD,TD,TS,2D".split(","));
        combination = CombinationMatcher.getCombination(player, cards);
        name = combination.getName();
        assertEquals(name, "Flush");

        //straight
        player = new Player("test", "9DQD");
        cards = Arrays.asList("9S,QH,JD,TD,TS,2H,8S".split(","));
        combination = CombinationMatcher.getCombination(player, cards);
        name = combination.getName();
        assertEquals(name, "Straight");

        //set
        player = new Player("test", "9DKC");
        cards = Arrays.asList("9S,3H,JD,TD,TS,2H,9C".split(","));
        combination = CombinationMatcher.getCombination(player, cards);
        name = combination.getName();
        assertEquals(name, SET.name());

        //two pair
        player = new Player("test", "9DKC");
        cards = Arrays.asList("9S,3H,JD,TD,TS,3S,5C".split(","));
        combination = CombinationMatcher.getCombination(player, cards);
        name = combination.getName();
        assertEquals(name, "TwoPair");

        //pair
        player = new Player("test", "9DKC");
        cards = Arrays.asList("7S,3H,JD,TD,TS,4S,5C".split(","));
        combination = CombinationMatcher.getCombination(player, cards);
        name = combination.getName();
        assertEquals(name, "OnePair");

        //high card
        player = new Player("test", "9DKC");
        cards = Arrays.asList("7S,3H,JD,TD,AS,4S,5C".split(","));
        combination = CombinationMatcher.getCombination(player, cards);
        name = combination.getName();
        assertEquals(name, "HighCard");
    }
    
    @Test
    public void testIsHighCard() throws Exception {
        //positive
        int[] card = new int[]{12,2,1,0,0};
        int result = CombinationMatcher.isHighCard(card);
        assertEquals(result, 12);

        int[] card2 = new int[]{0,0,0,0};
        int result2 = CombinationMatcher.isHighCard(card2);
        assertEquals(result2, 0);
    }

    @Test
    public void testIsOnePair() throws Exception {
        //positive
        int[] card = new int[]{12,2,1,0,0};
        int result = CombinationMatcher.isOnePair(card);
        assertEquals(result, 0);

        card = new int[]{12,12,1,0,0};
        result = CombinationMatcher.isOnePair(card);
        assertEquals(result, 12);

        //negative
        int[] card2 = new int[]{12,11,10,9,1,0};
        int result2 = CombinationMatcher.isOnePair(card2);
        assertEquals(result2, -1);
    }
    
    @Test
    public void testIsTwoPair() throws Exception {
        //positive
        int[] card = new int[]{12,12,10,10,0};
        int result = CombinationMatcher.isTwoPair(card);
        assertEquals(result, 12);

        card = new int[]{12,11,10,10,8,2,2,1,0};
        result = CombinationMatcher.isTwoPair(card);
        assertEquals(result, 10);

        //negative
        int[] card2 = new int[]{12,11,10,10,8,2,1,0};
        int result2 = CombinationMatcher.isTwoPair(card2);
        assertEquals(result2, -1);
    }
    
    @Test
    public void testIsSet() throws Exception {
        //positive
        int[] card = new int[]{12,12,9,9,9,7,7,0};
        int[] suite = new int[]{0,1,0,1,2,0,1,0};
        Combination combination = CombinationMatcher.isSet(card,suite);
        assertEquals(combination.getName(), SET.name());
        assertEquals(combination.getPower() - SET.getValue(), 9);

        card = new int[]{9,9,9,7,7,0};
        suite = new int[]{0,1,2,0,1,0};
        combination = CombinationMatcher.isSet(card,suite);
        assertEquals(combination.getName(), SET.name());
        assertEquals(combination.getPower() - SET.getValue(), 9);

        card = new int[]{12,12,9,9,7,7,0,0,0};
        suite = new int[]{0,1,0,1,0,1,0,1,2};
        combination = CombinationMatcher.isSet(card,suite);
        assertEquals(combination.getName(), SET.name());
        assertEquals(combination.getPower() - SET.getValue(), 0);

        //negative
        int[] card2 = new int[]{12,12,9,9,7,7,0};
        int[] suite2 = new int[]{0,1,0,1,0,1,0};
        Combination combination2 = CombinationMatcher.isSet(card2,suite2);
        assertNull(combination2);
    }
    
    @Test
    public void testIsStraight() throws Exception {
        //positive
        int[] card = new int[]{12,11,10,9,8,2,1,0};
        int result = CombinationMatcher.isStraight(card);
        assertEquals(result, 12);

        card = new int[]{12,10,9,8,8,7,6,4,3};
        result = CombinationMatcher.isStraight(card);
        assertEquals(result, 10);

        //negative
        int[] card2 = new int[]{12,10,9,7,6,4,3};
        int result2 = CombinationMatcher.isStraight(card2);
        assertEquals(result2, -1);
    }
    
    @Test
    public void testIsFlush() throws Exception {
        //positive
        int[] card = new int[]{12,11,10,9,8,2,1,0};
        int[] suite = new int[]{0,1,0,0,2,0,0,3};
        int[] suiteCount = new int[]{5,1,1,1};
        int result = CombinationMatcher.isFlush(card,suite,suiteCount);
        assertEquals(result, 12);

        card = new int[]{12,11,10,10,9,8,2,1,0,0};
        suite = new int[]{0,0,1,0,1,1,1,0,1,0};
        suiteCount = new int[]{5,5,0,0};
        result = CombinationMatcher.isFlush(card,suite,suiteCount);
        assertEquals(result, 12);

        //negative
        int[] card2 = new int[]{12,11,10,9,8,2,0};
        int[] suite2 = new int[]{0,1,0,0,2,0,3};
        int[] suiteCount2 = new int[]{4,1,1,1};
        int result2 = CombinationMatcher.isFlush(card2,suite2,suiteCount2);
        assertEquals(result2, -1);
    }
    
    @Test
    public void testIsFullHouse() throws Exception {
        //positive
        int[] card = new int[]{12,12,9,9,9,7,7,0};
        int result = CombinationMatcher.isFullHouse(card);
        assertEquals(result, 9);

        card = new int[]{12,9,9,9,7,7,0};
        result = CombinationMatcher.isFullHouse(card);
        assertEquals(result, 9);

        card = new int[]{12,9,9,9,7,0,0,0};
        result = CombinationMatcher.isFullHouse(card);
        assertEquals(result, 9);

        //negative
        int[] card2 = new int[]{12,12,9,9,7,7,0};
        int result2 = CombinationMatcher.isFullHouse(card2);
        assertEquals(result2, -1);
    }
    
    @Test
    public void testIsQuads() throws Exception {
        //positive
        int[] card = new int[]{12,12,9,9,9,9,7,7,0};
        int result = CombinationMatcher.isQuads(card);
        assertEquals(result, 9);

        //negative
        int[] card2 = new int[]{12,12,9,9,9,7,7,0};
        int result2 = CombinationMatcher.isQuads(card2);
        assertEquals(result2, -1);
    }
    
    @Test
    public void testIsStraightFlush() throws Exception {
        //positive
        int[] card = new int[]{12,11,10,9,8,2,1,0};
        int[] suite = new int[]{0,0,0,0,0,1,2,3};
        int[] suiteCount = new int[]{5,1,1,1};
        int result = CombinationMatcher.isStraightFlush(card,suite,suiteCount);
        assertEquals(result, 12);

        card = new int[]{12,11,10,9,8,7,7,6,2,1,0,0};
        suite = new int[]{0,2,1,1,1,3,1,1,3,0,2,1};
        suiteCount = new int[]{2,6,2,2};
        result = CombinationMatcher.isStraightFlush(card,suite,suiteCount);
        assertEquals(result, 10);

        //negative
        int[] card2 = new int[]{12,11,10,9,8,7,7,6,2,1,0,0};
        int[] suite2 = new int[]{0,2,1,1,1,3,1,3,1,0,2,1};
        int[] suiteCount2 = new int[]{2,6,2,2};
        int result2 = CombinationMatcher.isStraightFlush(card2,suite2,suiteCount2);
        assertEquals(result2, -1);
    }
    
    @Test
    public void testIsRoyalFlush() throws Exception {
        //positive
        int[] card = new int[]{12,11,10,9,8,7,2,1,0};
        int[] suite = new int[]{0,0,0,0,0,0,1,2,3};
        int[] suiteCount = new int[]{6,1,1,1};
        int result = CombinationMatcher.isRoyalFlush(card,suite,suiteCount);
        assertEquals(result, 12);

        card = new int[]{12,11,11,10,9,8,2,1,0};
        suite = new int[]{0,1,0,0,0,0,1,2,3};
        suiteCount = new int[]{5,2,1,1};
        result = CombinationMatcher.isRoyalFlush(card,suite,suiteCount);
        assertEquals(result, 12);

        //negative
        int[] card2 = new int[]{11,10,9,8,7,2,1,0};
        int[] suite2 = new int[]{0,0,0,0,0,1,2,3};
        int[] suiteCount2 = new int[]{5,1,1,1};
        int result2 = CombinationMatcher.isRoyalFlush(card2,suite2,suiteCount2);
        assertEquals(result2, -1);
    }

}