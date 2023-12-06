package day06;

public record RaceRecord(int timeAllowed, int bestDistance) {
    public int countWaysToBeatTheRecord() {
        int count = 0;
        for (int i = 0; i < timeAllowed; i++) {
            int distance = i * (timeAllowed - i);
            if (distance > bestDistance)
                count++;
        }
        return count;
    }
}
