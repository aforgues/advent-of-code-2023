package day09;

import java.util.List;

public record SensorReport(List<SingleValueHistory> singleValueHistoryList) {
    public long sumExtrapolatedNextValues() {
        return this.singleValueHistoryList.stream()
                .map(SingleValueHistory::extrapolateNextValue)
                .reduce(0, Integer::sum);
    }
}
