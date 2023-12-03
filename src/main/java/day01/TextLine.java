package day01;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public record TextLine(String line) {
    public long computeCalibrationValue(boolean includeLettersSpelledDigits) {
        char[] items = this.line.toCharArray();
        AtomicReference<Integer> firstDigit = new AtomicReference<>(0);
        for (int i = 0; i < items.length; i++) {
            char c = items[i];
            if (Character.isDigit(c)) {
                firstDigit.set(Character.getNumericValue(c));
                break;
            }
        }
        AtomicInteger indexOfFirstDigit = new AtomicInteger(this.line.indexOf("" + firstDigit));

        AtomicReference<Integer> lastDigit = new AtomicReference<>(0);
        for (int i = items.length - 1; i >= 0; i--) {
            char c = items[i];
            if (Character.isDigit(c)) {
                lastDigit.set(Character.getNumericValue(c));
                break;
            }
        }
        AtomicInteger indexOfLastDigit = new AtomicInteger(this.line.lastIndexOf("" + lastDigit));

        // check if a LettersSpelled digit exists on the line
        if (includeLettersSpelledDigits) {
            Arrays.stream(LettersSpelledDigit.values()).forEach(lettersSpelledDigit -> {
                int index = this.line.indexOf(lettersSpelledDigit.name().toLowerCase());
                if (index >= 0) {
                    if (indexOfFirstDigit.get() == -1 || index < indexOfFirstDigit.get()) {
                        firstDigit.set(lettersSpelledDigit.digitValue);
                        indexOfFirstDigit.set(index);
                    }
                }
                int lastIndex = this.line.lastIndexOf(lettersSpelledDigit.name().toLowerCase());
                if (lastIndex >= 0) {
                    if (indexOfLastDigit.get() == -1 || lastIndex > indexOfLastDigit.get()) {
                        lastDigit.set(lettersSpelledDigit.digitValue);
                        indexOfLastDigit.set(lastIndex);
                    }
                }
            });
        }

        long calibrationValue = firstDigit.get().intValue()*10 + lastDigit.get().intValue();
        System.out.println(calibrationValue);

        return calibrationValue;
    }
}
