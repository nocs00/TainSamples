package se.tain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Deck {
    private static final int DECK_SIZE = 52;
    private Queue<String> cards;

    public Deck() {
        cards = new ArrayDeque<>(DECK_SIZE);
        List<String> faces = Arrays.asList("2,3,4,5,6,7,8,9,T,J,Q,K,A".split(","));
        List<String> suits = Arrays.asList("H,S,C,D".split(","));
        List<String> shuffledCards = new ArrayList<>();
        faces.forEach(f -> suits.forEach(s -> shuffledCards.add(f+s)));
        Collections.shuffle(shuffledCards);
        shuffledCards.forEach(card -> cards.add(card));
    }

    public boolean hasMore() {
        return !cards.isEmpty();
    }

    public String nextCard() {
        return cards.poll();
    }

    public static Queue<String> newDeck() {
        Deck deck = new Deck();
        return deck.cards;
    }
}
