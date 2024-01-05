package day15;

import java.util.ArrayList;
import java.util.List;

public record InitializationSequence(List<Step> steps) {
    public static InitializationSequence fromRawContent(String content) {
        List<Step> steps = new ArrayList<>();

        for (String value : content.split(",")) {
            steps.add(new Step(value));
        }

        return new InitializationSequence(steps);
    }

    public long sumHashOfEachStep() {
        return this.steps.stream()
                .map(Step::hash)
                .reduce(0L, Long::sum);
    }
}
