package se.tain.matchers.model;

public enum CombinationValue {
    HIGH_CARD(0),
    ONE_PAIR(13),
    TWO_PAIR(26),
    SET(39),
    STRAIGHT(52),
    FLUSH(65),
    FULL_HOUSE(78),
    QUADS(91),
    STRAIGHT_FLUSH(104),
    ROYAL_FLUSH(117);

    private int value;

    CombinationValue(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }
}
