package se.tain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand {
    private List<Card> cards;

    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    public static Hand valueOf( String initialHandString ) {
        int cardsCnt = initialHandString.length() / 2;
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < cardsCnt; i++) {
            cards.add(Card.valueOf(initialHandString.substring(i * 2, i * 2 + 2)));
        }
        return new Hand( cards );
    }
}
