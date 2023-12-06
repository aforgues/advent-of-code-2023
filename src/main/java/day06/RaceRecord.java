package day06;

public record RaceRecord(long timeAllowed, long bestDistance) {
    public long countWaysToBeatTheRecord() {
        long count = 0;
        for (long i = 0; i < timeAllowed; i++) {
            long distance = i * (timeAllowed - i);
            if (distance > bestDistance)
                count++;
        }
        return count;
    }
}
