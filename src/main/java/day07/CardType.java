package day07;

public enum CardType {
    _2, _3, _4, _5, _6, _7, _8, _9, T, J, Q, K, A;

    public static CardType from(char c) {
        if (Character.isDigit(c)) {
            int num = Character.getNumericValue(c);
            return CardType.valueOf("_" + num);
        }
        else {
            return CardType.valueOf(String.valueOf(c));
        }
    }
}
