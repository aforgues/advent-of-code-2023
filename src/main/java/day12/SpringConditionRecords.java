package day12;

import java.util.List;

public record SpringConditionRecords(List<SpringConditionRow> springConditionRowList) {
    public long sumPossibleArrangementCounts() {
        return this.springConditionRowList.stream()
                .map(SpringConditionRow::countPossibleArrangements)
                .reduce(0L, Long::sum);
    }
}
