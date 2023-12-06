package day06;

import java.util.ArrayList;
import java.util.List;

public record SheetOfPaper(List<RaceRecord> raceRecords) {
    public static SheetOfPaper fromTimeAndDistanceLists(List<Integer> timeAllowedList, List<Integer> bestDistanceList) {
        List<RaceRecord> raceRecords = new ArrayList<>();
        for (int i = 0; i < timeAllowedList.size(); i++) {
            raceRecords.add(new RaceRecord(timeAllowedList.get(i), bestDistanceList.get(i)));
        }
        return new SheetOfPaper(raceRecords);
    }

    public long timesOfWaysToBeatTheRecord() {
        return this.raceRecords.stream()
                .map(RaceRecord::countWaysToBeatTheRecord)
                .reduce(1, (a, b) -> a*b);
    }
}
