package se.tain.matchers.model;

import lombok.Data;
import se.tain.Player;
import se.tain.matchers.Util;

import java.util.Collections;
import java.util.List;

@Data
public class Combination implements Comparable<Combination> {
    private int power;
    private String name;
    private Player player;

    private List<String> combinationCards;

    public Combination(int power, String name) {
        this(power, name, Collections.EMPTY_LIST);
    }

    public Combination(int power, String name, List<String> combinationCards) {
        Combination.this.power = power;
        Combination.this.name = name;
        Combination.this.combinationCards = combinationCards;
    }

    @Override
    public int compareTo(Combination other) {
        Integer thisValue = Integer.valueOf(power);
        Integer otherValue = Integer.valueOf(other.power);
        return thisValue.compareTo(otherValue);
    }

    public String getBestKicker() {
        String kicker = null;

        if (name.equals(CombinationValue.SET.name())) { //only set implemented
            List<String> handCards = getPlayer().getWinningHand().rawCards();
            int maxCost = -1;
            for (String handCard : handCards) {
                if (!getCombinationCards().contains(handCard)) {
                    int cardCost = Util.getFaceCost(handCard);
                    maxCost = Integer.max(maxCost, cardCost);
                    if (maxCost == cardCost) kicker = handCard;
                }
            }
        }
        return kicker;
    }
}
