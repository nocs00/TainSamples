package se.tain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Card {

    private String face;
    private String suit;

    public static Card valueOf(String rawCard) {
        return new Card(rawCard.substring(0,1), rawCard.substring(1));

    }

}
