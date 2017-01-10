package se.tain;

public class Player {
    private String playerName;
    private Hand holeCards;
    private Hand winningHand;

    public Player(String playerName, String initialHandString) {
        this.playerName = playerName;
        this.holeCards = Hand.valueOf(initialHandString);
    }

    @Override
    public String toString() {
        return playerName;
    }
}
