package day05;

import java.util.Optional;

public record MapConversionRule(long destinationRangeStart, long sourceRangeStart, int rangeLength) {
    public static MapConversionRule fromRawContent(String mapLine) {
        String[] numbersArr = mapLine.split(" ");
        return new MapConversionRule(Long.parseLong(numbersArr[0]), Long.parseLong(numbersArr[1]), Integer.parseInt(numbersArr[2]));
    }

    public Optional<Long> getDestinationNumber(long sourceNumber) {
        if (sourceNumber < this.sourceRangeStart) {
            return Optional.empty();
        }

        if (sourceNumber > this.sourceRangeStart + this.rangeLength) {
            return Optional.empty();
        }

        return Optional.of(this.destinationRangeStart + (sourceNumber - this.sourceRangeStart));
    }
}
