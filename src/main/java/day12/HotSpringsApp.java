package day12;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HotSpringsApp {

    private static final boolean TEST = false;
    private static final String PUZZLE_INPUT_FILE_NAME = TEST ? "puzzle_input_test.txt" : "puzzle_input.txt";

    public static void main(String[] args) throws FileNotFoundException {
        HotSpringsApp app = new HotSpringsApp();

        // First part
        app.computeScore();

        // Second part
        app.computeScoreV2();
    }

    private final String filePath;
    private SpringConditionRecords springConditionRecords;

    public HotSpringsApp() throws FileNotFoundException {
        String BASE_PATH = "src/main/resources/" + this.getClass().getPackageName() + "/";
        this.filePath = BASE_PATH + PUZZLE_INPUT_FILE_NAME;
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        List<SpringConditionRow> springConditionRowList = new ArrayList<>();
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            springConditionRowList.add(SpringConditionRow.fromRawContent(content));
        }
        this.springConditionRecords = new SpringConditionRecords(springConditionRowList);
        System.out.println(this.springConditionRecords);
    }

    private void computeScore() {
        Instant start = Instant.now();

        long score = this.springConditionRecords.sumPossibleArrangementCounts();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }

    private void computeScoreV2() {
        Instant start = Instant.now();

        long score = this.springConditionRecords.sumUnfoldedPossibleArrangementCounts();

        Instant end = Instant.now();

        System.out.println("Score Part 2 : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
