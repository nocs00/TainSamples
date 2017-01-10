package se.tain;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class PokerTable {
    public static void main( String[] args ) {
        Scanner consoleInput = new Scanner( System.in );
        Stream<String> consoleInputDealerStream = Stream.generate( consoleInput::nextLine );

        PokerTable testTable = new PokerTable();

        testTable.deal(
                consoleInputDealerStream,
                asList( new Player( "pedro", "2CAH" ), new Player( "john", "2D2S" ) )
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
        return dealerCardsStream.map( card -> getWinners( card, players ) );
    }

    private static List<Player> getWinners( String inputCard, List<Player> players ) {
        System.out.printf( "Card dealt: %s%n", inputCard );
        return players;
    }
}