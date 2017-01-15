package se.tain;

import se.tain.matchers.CombinationMatcher;
import se.tain.matchers.WinnersMatcher;
import se.tain.matchers.model.Combination;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class PokerTable {
    public static void main( String[] args ) {
        PokerTable testTable = new PokerTable();

        Deck deck = Deck.newDeck();

        String playerName1 = "pedro";
        String playerName2 = "john";
        String playerCards1 = deck.nextCard() + deck.nextCard();
        String playerCards2 = deck.nextCard() + deck.nextCard();

        List<Player> players = asList(new Player(playerName1, playerCards1), new Player(playerName2, playerCards2));

        System.out.print("Starting with: ");
        System.out.println(players.stream()
                .map(p -> p.getPlayerName() + "(" + p.getHoleCards() + ")" )
                .collect(Collectors.joining(";\t")));

        testTable.deal(
                deck.stream(),
                players
        ).forEach( System.out::println );
    }

    /**
     * Write a function that returns a stream of winning players for each dealerCardsStream card that appears on table
     *
     * @param dealerCardsStream - cards that appear on table
     * @param players           - initial players on table
     * @return stream of winning players for each new valid card on a table. Invalid cards are just skipped
     */
    public Stream<List<Player>> deal( Stream<String> dealerCardsStream, List<Player> players ) {
        return dealerCardsStream.map( card -> getWinners( card, players ) ).limit(5);
    }

    private static int turnNumber = 1;
    private static List<String> cardsOnTable = new ArrayList<>();
    private static List<Player> getWinners( String inputCard, List<Player> players ) {
        cardsOnTable.add(inputCard);
        System.out.printf("Turn %d\t", turnNumber++);
        System.out.printf( "Card dealt: %s\tTable: %s\t", inputCard,  cardsOnTable.stream().collect(Collectors.joining(",")));
        System.out.print("Winners: ");

        List<Combination> combinations = players.stream()
                .map(player -> CombinationMatcher.getCombination(player, cardsOnTable))
                .sorted().collect(Collectors.toList());

        return WinnersMatcher.getWinners(combinations, cardsOnTable);
    }
}
