package day09;

import java.util.ArrayList;
import java.util.List;

public record SingleValueHistory(List<Integer> values) {

    public Integer extrapolateNextValue() {
        System.out.println("Extrapolating next value for : " + this.values);
        List<List<Integer>> sequences = new ArrayList<>();
        sequences.add(this.values);

        List<Integer> nextSeq = generateNextSequence(this.values);
        sequences.add(nextSeq);

        while (! nextSeq.stream().allMatch(seqValue -> seqValue == 0)) {
            nextSeq = generateNextSequence(nextSeq);
            sequences.add(nextSeq);
        }

        System.out.println(sequences);
        return sequences.reversed()
                    .stream()
                    .map(seq -> seq.get(seq.size() - 1))
                    .reduce(0, Integer::sum);

    }

    private List<Integer> generateNextSequence(List<Integer> seqValues) {
        Integer current = null;
        List<Integer> sequence = new ArrayList<>();
        for (Integer value : seqValues) {
            if (current != null) {
                sequence.add(value - current);
            }
            current = value;
        }
        return sequence;
    }
}
