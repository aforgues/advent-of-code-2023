package day09;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MirageMaintenanceApp {

    private static final boolean TEST = false;
    private static final String PUZZLE_INPUT_FILE_NAME = TEST ? "puzzle_input_test.txt" : "puzzle_input.txt";

    public static void main(String[] args) throws FileNotFoundException {
        MirageMaintenanceApp app = new MirageMaintenanceApp();

        // First part
        app.computeScore();

        // Second part
        app.computeScorePart2();
    }

    private final String filePath;
    private SensorReport sensorReport;

    public MirageMaintenanceApp() throws FileNotFoundException {
        String BASE_PATH = "src/main/resources/" + this.getClass().getPackageName() + "/";
        this.filePath = BASE_PATH + PUZZLE_INPUT_FILE_NAME;
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        List<SingleValueHistory> singleValueHistoryList = new ArrayList<>();
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);

            singleValueHistoryList.add(new SingleValueHistory(Arrays.stream(content.split(" ")).map(Integer::parseInt).toList()));
        }

        this.sensorReport = new SensorReport(singleValueHistoryList);
        System.out.println(this.sensorReport);
    }

    private void computeScore() {
        Instant start = Instant.now();

        long score = this.sensorReport.sumExtrapolatedNextValues();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }

    private void computeScorePart2() {
        Instant start = Instant.now();

        long score = this.sensorReport.sumExtrapolatedPreviousValues();

        Instant end = Instant.now();

        System.out.println("Score Part 2 : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
