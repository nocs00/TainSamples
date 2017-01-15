package se.tain.matchers.model;

import lombok.Data;

@Data
public class Hand {
    private int[] card;
    private int[] suite;
    private int[] suiteCount;

    public Hand(int length) {
        card = new int[length];
        suite = new int[length];
        suiteCount = new int[4];
    }

    public Hand(int[] card, int[] suite, int[] suiteCount) {
        this.card = card;
        this.suite = suite;
        this.suiteCount = suiteCount;
    }
}
