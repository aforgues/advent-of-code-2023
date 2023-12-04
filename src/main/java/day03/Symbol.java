package day03;

import utils.Position;

public record Symbol(Position position, char value) {
    public static boolean isSymbol(char c) {
        if (c == '.')
            return false;

        try {
            Integer.parseInt(String.valueOf(c));
            return false;
        }
        catch (NumberFormatException e) {
            return true;
        }
    }
}
