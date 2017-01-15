package se.tain.matchers;

import org.junit.Test;
import se.tain.Player;
import se.tain.matchers.model.Combination;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static se.tain.matchers.model.CombinationValue.SET;

public class WinnersMatcherTest {

    @Test
    public void testKicker() throws Exception {
        Player player = null, player2 = null;
        Combination combination = null, combination2 = null;
        List<String> table = Arrays.asList("9S,9H,JH,TH,QS,TS".split(","));

        //set
        player = new Player("test", "9CAD");
        combination = CombinationMatcher.getCombination(player, table);

        //set
        player2 = new Player("test2", "9D2D");
        combination2 = CombinationMatcher.getCombination(player2, table);

        List<Player> winners =
                WinnersMatcher.getWinners(
                        Arrays.asList(combination, combination2),
                        table);

        assertThat(winners.size(), equalTo(1));
        String name = winners.get(0).getWinningHand().getName();
        assertEquals(name, SET.name());
    }

    @Test
    public void testTie() throws Exception {
        Player player = null, player2 = null;
        Combination combination = null, combination2 = null;
        List<String> table = Arrays.asList("9S,3H,JD,TD,TS,2H,9C".split(","));

        //set
        player = new Player("test", "9DQC");
        combination = CombinationMatcher.getCombination(player, table);

        //set
        player2 = new Player("test2", "9CQS");
        combination2 = CombinationMatcher.getCombination(player2, table);

        List<Player> winners =
                WinnersMatcher.getWinners(
                        Arrays.asList(combination, combination2),
                        table);

        assertThat(winners.size(), equalTo(2));
        String name = winners.get(0).getWinningHand().getName();
        String name2 = winners.get(1).getWinningHand().getName();
        assertEquals(name, SET.name());
        assertEquals(name2, SET.name());
    }

}