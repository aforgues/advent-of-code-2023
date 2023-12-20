package day12;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public record SpringConditionRow(String rawStates, List<SpringState> states, List<Integer> brokenContiguousGroupSizes) {
    private static final boolean DEBUG = false;
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
        // compute all possible variants while checking contiguous groups and size
        List<String> variants = generateAllVariants(this.rawStates, this.brokenContiguousGroupSizes);

        return variants.size();
    }

    public long countUnfoldedPossibleArrangements() {
        // unfold rawStates
        String unfoldRawStates = (this.rawStates + "?").repeat(5);
        unfoldRawStates = unfoldRawStates.substring(0, unfoldRawStates.length() - 1);

        // unfold brokenContiguousGroupSizes
        List<Integer> unfoldBrokenContiguousGroupSizes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            unfoldBrokenContiguousGroupSizes.addAll(this.brokenContiguousGroupSizes);
        }

        long count = generateAllVariants(unfoldRawStates, unfoldBrokenContiguousGroupSizes).size();

        System.out.println(this.rawStates + " - " + this.brokenContiguousGroupSizes + " : " + count + " arrangement(s)");
        return count;
    }

    private static boolean isValidVariant(String variant, List<Integer> brokenContiguousGroupSizes) {
        List<Integer> groupSizes = new ArrayList<>();
        StringBuilder group = new StringBuilder();
        String lastCheckedState = "";
        for (char c : variant.toCharArray()) {
            String s = String.valueOf(c);

            lastCheckedState = s;

            if (s.equals(SpringState.UNKNOWN.symbol)) {
                break;
            }

            if (s.equals(SpringState.DAMAGED.symbol)) {
                group.append(s);
            }
            else if (s.equals(SpringState.OPERATIONAL.symbol) && group.length() > 0){
                groupSizes.add(group.length());
                //System.out.println("Group size : " + group.length());
                group = new StringBuilder();
            }
        }
        if (group.length() > 0 && ! lastCheckedState.equals(SpringState.UNKNOWN.symbol)) {
            groupSizes.add(group.length());
            //System.out.println("Group size : " + group.length());
            group = new StringBuilder();
        }

        if (variant.contains(SpringState.UNKNOWN.symbol)) {
            if (groupSizes.size() <= brokenContiguousGroupSizes.size()) {
                for (int i = 0; i < groupSizes.size(); i++) {
                    if (!groupSizes.get(i).equals(brokenContiguousGroupSizes.get(i))) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return groupSizes.equals(brokenContiguousGroupSizes);
    }

    private static List<String> generateAllVariants(String rawStates, List<Integer> brokenContiguousGroupSizes) {
        if (DEBUG)
            System.out.println("Raw states : " + rawStates);
        List<String> variants = new ArrayList<>();
        ArrayDeque<String> queue = new ArrayDeque<>();
        queue.add(rawStates);

        while (queue.size() > 0) {
            String s = queue.poll();
            if (DEBUG)
                System.out.println("check " + s);
            String damagedVariant = s.replaceFirst("\\?", SpringState.DAMAGED.symbol);
            String operationalVariant = s.replaceFirst("\\?", SpringState.OPERATIONAL.symbol);

            if (isValidVariant(damagedVariant, brokenContiguousGroupSizes)) {
                if (damagedVariant.contains(SpringState.UNKNOWN.symbol)) {
                    queue.add(damagedVariant);
                } else {
                    variants.add(damagedVariant);
                    if (DEBUG)
                        System.out.println("Variant : " + damagedVariant);
                }
            }
            else {
                if (DEBUG)
                    System.out.println("Not valid variant : " + damagedVariant);
            }

            if (isValidVariant(operationalVariant, brokenContiguousGroupSizes)) {
                if (operationalVariant.contains(SpringState.UNKNOWN.symbol)) {
                    queue.add(operationalVariant);
                } else {
                    variants.add(operationalVariant);
                    if (DEBUG)
                        System.out.println("Variant : " + operationalVariant);
                }
            }
            else {
                if (DEBUG)
                    System.out.println("Not valid variant : " + operationalVariant);
            }
        }

        //System.out.println("All variants : " + variants);
        return variants;
    }
}
