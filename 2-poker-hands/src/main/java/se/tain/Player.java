package se.tain;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;

@Setter
@Getter
public class Player {
    private String playerName;
    private Hand holeCards;
    private Hand winningHand;

    public Player(String playerName, String initialHandString) {
        this.playerName = playerName;
        this.holeCards = Hand.valueOf(initialHandString);
        this.winningHand = new Hand(Collections.EMPTY_LIST);
    }

    @Override
    public String toString() {
        return playerName + "(" + winningHand.getName() + ")";
    }
}
