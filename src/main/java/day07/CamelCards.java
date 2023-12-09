package day07;

import java.util.TreeMap;

public record CamelCards(TreeMap<Hand, Integer> bidsByHand) {
    public long totalWinnings() {
        return this.bidsByHand.entrySet().stream()
                .map(entry -> this.rank(entry.getKey()) * entry.getValue())
                .reduce(0, Integer::sum);
    }

    private int rank(Hand hand) {
        int rank = (this.bidsByHand.keySet().stream().toList().indexOf(hand) +  1);
        System.out.println("Hand " + hand.cardTypeList() + " => type = " + hand.getType() + " => rank = " + rank);
        return rank;
    }
}
