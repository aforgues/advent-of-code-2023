package day12;

import java.util.*;

public record SpringConditionRow(String rawStates, List<SpringState> states, List<Integer> brokenContiguousGroupSizes) {
    public static SpringConditionRow fromRawContent(String content) {
        String[] array = content.split(" ");

        List<SpringState> states = new ArrayList<>();
        for (char c : array[0].toCharArray()) {
            states.add(SpringState.from(c));
        }

        List<Integer> brokenContiguousGroupSizes = new ArrayList<>();
        for (String s : array[1].split(",")) {
            brokenContiguousGroupSizes.add(Integer.parseInt(s));
        }
        return new SpringConditionRow(array[0], states, brokenContiguousGroupSizes);
    }

    public long countPossibleArrangements() {
        // compute all possible variants
        List<String> variants = generateAllVariants();

        // check contiguous groups and size
        return countValidVariants(variants);
    }

    private long countValidVariants(List<String> variants) {
        long count = 0;

        for (String variant : variants) {
            if (isValidVariant(variant)) {
                count++;
            }
        }

        return count;
    }

    private boolean isValidVariant(String variant) {
        List<Integer> groupSizes = new ArrayList<>();
        StringBuilder group = new StringBuilder();
        for (char c : variant.toCharArray()) {
            String s = String.valueOf(c);

            if (s.equals(SpringState.DAMAGED.symbol)) {
                group.append(s);
            }
            else if (group.length() > 0){
                groupSizes.add(group.length());
                //System.out.println("Group size : " + group.length());
                group = new StringBuilder();
            }
        }
        if (group.length() > 0) {
            groupSizes.add(group.length());
            //System.out.println("Group size : " + group.length());
            group = new StringBuilder();
        }
        return groupSizes.equals(this.brokenContiguousGroupSizes);
    }

    private List<String> generateAllVariants() {
        System.out.println("Raw states : " + this.rawStates);
        List<String> variants = new ArrayList<>();
        ArrayDeque<String> queue = new ArrayDeque<>();
        queue.add(this.rawStates);

        while (queue.size() > 0) {
            String s = queue.poll();
            String damagedVariant = s.replaceFirst("\\?", SpringState.DAMAGED.symbol);
            String operationalVariant = s.replaceFirst("\\?", SpringState.OPERATIONAL.symbol);

            if (damagedVariant.contains(SpringState.UNKNOWN.symbol)) {
                queue.add(damagedVariant);
            }
            else {
                variants.add(damagedVariant);
                //System.out.println("Variant : " + damagedVariant);
            }
            if (operationalVariant.contains(SpringState.UNKNOWN.symbol)) {
                queue.add(operationalVariant);
            }
            else {
                variants.add(operationalVariant);
                //System.out.println("Variant : " + operationalVariant);
            }
        }

        System.out.println("All variants : " + variants);
        return variants;
    }
}
