package day04;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record Game(List<ScratchCard> scratchCards) {
    public long sumPilePoints() {
        return this.scratchCards.stream()
                .map(ScratchCard::computePoints)
                .reduce(0L, Long::sum);
    }

    public long countTotalScratchCards() {
        Map<Integer, Integer> countCopyByScratchCardId = new HashMap<>();

        this.scratchCards.forEach(scratchCard -> {
            int matchCount = scratchCard.matchCount();
            int copies = Optional.ofNullable(countCopyByScratchCardId.get(scratchCard.id())).orElse(0);
            if (matchCount > 0) {
                for (int i = scratchCard.id() + 1; i <= scratchCard.id() + matchCount; i++) {
                    if (i > this.scratchCards.size()) {
                        break;
                    }
                    Integer count = Optional.ofNullable(countCopyByScratchCardId.get(i)).orElse(0);
                    countCopyByScratchCardId.put(i, count + 1 + copies);
                }
            }
        });
        return this.scratchCards.size() + countCopyByScratchCardId.values().stream().reduce(0, Integer::sum);
    }
}
