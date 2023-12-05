package day05;

import java.util.HashMap;
import java.util.Map;

public record MapConversionRule(long destinationRangeStart, long sourceRangeStart, int rangeLength) {
    public static MapConversionRule fromRawContent(String mapLine) {
        String[] numbersArr = mapLine.split(" ");
        return new MapConversionRule(Long.parseLong(numbersArr[0]), Long.parseLong(numbersArr[1]), Integer.parseInt(numbersArr[2]));
    }

    public Map<Long, Long> generateMapping() {
        Map<Long, Long> mapping = new HashMap<>();

        for (int i = 0; i < rangeLength; i++) {
            mapping.put(this.sourceRangeStart + i, this.destinationRangeStart + i);
        }

        return mapping;
    }
}
