package se.tain;

import java.util.Collections;
import java.util.List;

public class Hand {
    private List<Card> cards;

    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    public static Hand valueOf( String initialHandString ) {
        return new Hand( Collections.emptyList() );
    }
}
