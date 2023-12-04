package day04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public record ScratchCard(int id, List<Integer> winningNumbers, List<Integer> cardNumbers) {
    public static ScratchCard from(String content) {
        String[] arr = content.split(": ");
        int id = Integer.parseInt(arr[0].split("rd")[1].replaceAll(" ", ""));

        String[] arrNumbers = arr[1].split(" \\| ");
        List<Integer> winningNumbers = new ArrayList<>(Arrays.stream(arrNumbers[0].trim().replaceAll("  ", " ").split(" ")).map(Integer::parseInt).toList());
        List<Integer> cardNumbers = new ArrayList<>(Arrays.stream(arrNumbers[1].trim().replaceAll("  ", " ").split(" ")).map(Integer::parseInt).toList());

        return new ScratchCard(id, winningNumbers, cardNumbers);
    }

    public long computePoints() {
        AtomicInteger matchCount = new AtomicInteger();
        this.cardNumbers.forEach(cardNumber -> {
            if (this.winningNumbers.stream().anyMatch(winningNumber -> winningNumber.equals(cardNumber))) {
                matchCount.getAndIncrement();
            }
        });
        return matchCount.get() > 0 ? Double.valueOf(Math.pow(2, matchCount.get() - 1)).longValue() : 0L;
    }
}
