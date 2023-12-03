package day01;

import java.util.List;

public record CalibrationDocument(List<TextLine> textLines) {
    public long sumCablibrationValues(boolean includeLettersSpelledDigits) {
        return this.textLines.stream().map(textLine -> textLine.computeCalibrationValue(includeLettersSpelledDigits)).reduce(0L, Long::sum);
    }
}
