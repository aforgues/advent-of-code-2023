package day01;

public enum LettersSpelledDigit {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9);

    public final int digitValue;

    LettersSpelledDigit(int digitValue) {
        this.digitValue = digitValue;
    }
}
