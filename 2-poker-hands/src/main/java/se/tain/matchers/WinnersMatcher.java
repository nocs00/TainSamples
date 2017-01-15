package se.tain.matchers;

import se.tain.Player;
import se.tain.matchers.model.Combination;
import se.tain.matchers.model.CombinationValue;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WinnersMatcher {
    public static List<Player> getWinners(List<Combination> combinations, List<String> cardsOnTable) {
        if (combinations == null || combinations.isEmpty()) return Collections.EMPTY_LIST;

        int max = combinations.stream()
                .sorted()
                .max(Combination::compareTo)
                .get()
                .getPower();

        List<Combination> winCombinations = combinations.stream()
                .filter(c -> c.getPower() == max).collect(Collectors.toList());

        //kicker
        if (winCombinations.size() > 1) {
            winCombinations = kickCombinations(winCombinations, cardsOnTable);
        }

        return winCombinations.stream().map(c -> c.getPlayer()).collect(Collectors.toList());
    }

    private static List<Combination> kickCombinations(
            List<Combination> combinations,
            List<String> cards)
    {
        if (combinations.get(0).getName().equals(CombinationValue.SET.name())) { //only SET implemented

            int bestCost = -1;
            for (Combination combination : combinations) {
                int cardCost = Util.getFaceCost(combination.getBestKicker());
                bestCost = Integer.max(bestCost, cardCost);
            }
            int max = bestCost;
            return combinations.stream()
                    .filter(c -> Util.getFaceCost(c.getBestKicker()) == max)
                    .collect(Collectors.toList());
        }
        return combinations;
    }
}
