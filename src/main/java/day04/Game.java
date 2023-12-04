package day04;

import java.util.List;

public record Game(List<ScratchCard> scratchCards) {
    public long sumPilePoints() {
        return this.scratchCards.stream()
                .map(ScratchCard::computePoints)
                .reduce(0L, Long::sum);
    }
}
