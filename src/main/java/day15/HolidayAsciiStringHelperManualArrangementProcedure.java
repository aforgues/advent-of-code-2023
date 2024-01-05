package day15;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HolidayAsciiStringHelperManualArrangementProcedure {
    private final InitializationSequence initializationSequence;

    public HolidayAsciiStringHelperManualArrangementProcedure(final InitializationSequence initializationSequence) {
        this.initializationSequence = initializationSequence;
    }

    public long totalFocusingPower() {
        // Compute all lenses along boxes
        Map<Integer, Map<String, Long>> lensesByLabelByBox = new HashMap<>();
        for (Step step : this.initializationSequence.steps()) {
            System.out.println("Analysing step " + step);
            System.out.println("  label       = " + step.label());
            System.out.println("  operation   = " + step.operation());
            System.out.println("  focalLength = " + step.focalLength());
            System.out.println("  hash of label = " + step.labelHash());

            switch (step.operation()) {
                case EQUALS_SIGN -> {
                    Map<String, Long> lensesByLabel = lensesByLabelByBox.computeIfAbsent(step.labelHash(), k -> new LinkedHashMap<>());
                    lensesByLabel.put(step.label(), step.focalLength());
                }
                case DASH -> {
                    Map<String, Long> lensesByLabel = lensesByLabelByBox.get(step.labelHash());
                    if (lensesByLabel != null) {
                        lensesByLabel.remove(step.label());
                    }
                }
            }
            System.out.println(lensesByLabelByBox);

        }

        // Compute focusing power for each lens
        int totalFocusingPower = 0;
        for (Map.Entry<Integer, Map<String, Long>> entryBox : lensesByLabelByBox.entrySet()) {
            int slotNb = 1;
            for (Map.Entry<String, Long> entryLens : entryBox.getValue().entrySet()) {
                System.out.println(entryLens.getKey() + ": " + (entryBox.getKey() + 1) + " * " + slotNb + " * " + entryLens.getValue());
                totalFocusingPower += (entryBox.getKey() + 1) * slotNb * entryLens.getValue();
                slotNb++;
            }
        }

        return totalFocusingPower;
    }
}
