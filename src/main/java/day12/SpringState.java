package day12;

import java.util.Arrays;

public enum SpringState {
    OPERATIONAL("."),
    DAMAGED("#"),
    UNKNOWN("?");

    public final String symbol;

    SpringState(String symbol) {
        this.symbol = symbol;
    }

    public static SpringState from(char c) {
        return Arrays.stream(SpringState.values()).filter(springState -> springState.symbol.equals(String.valueOf(c))).findFirst().orElse(null);
    }
}
