package day15;

import java.util.Arrays;

public enum Operation {
    DASH("-"),
    EQUALS_SIGN("=");

    public final String character;
    Operation(String character) {
        this.character = character;
    }

    public static Operation from(String c) {
        return Arrays.stream(Operation.values()).filter(operation -> operation.character.equals(c)).findFirst().orElse(null);
    }
}
