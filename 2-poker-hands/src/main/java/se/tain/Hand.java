package se.tain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Hand {
    private List<Card> cards;
    private String name;

    public Hand(List<Card> cards) {
        this(cards, "");
    }

    public Hand(List<Card> cards, String name) {
        this.cards = cards;
        this.name = name;
    }

    public static Hand valueOf( String initialHandString ) {
        int cardsCnt = initialHandString.length() / 2;
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < cardsCnt; i++) {
            cards.add(Card.valueOf(initialHandString.substring(i * 2, i * 2 + 2)));
        }
        return new Hand( cards );
    }

    public List<String> rawCards() {
        return cards.stream().map(c -> c.toString()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return rawCards().stream().collect(Collectors.joining(","));
    }
}
