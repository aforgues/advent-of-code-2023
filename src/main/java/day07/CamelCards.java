package day07;

import java.util.TreeMap;

public record CamelCards(TreeMap<Hand, Integer> bidsByHand) {
    public long totalWinnings() {
        return this.bidsByHand.entrySet().stream()
                .map(entry -> this.rank(entry.getKey()) * entry.getValue())
                .reduce(0, Integer::sum);
    }

    private int rank(Hand hand) {
        return (this.bidsByHand.keySet().stream().toList().indexOf(hand) +  1);
    }
}
