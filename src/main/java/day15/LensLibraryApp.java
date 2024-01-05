package day15;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.Scanner;

public class LensLibraryApp {

    private static final boolean TEST = false;
    private static final String PUZZLE_INPUT_FILE_NAME = TEST ? "puzzle_input_test.txt" : "puzzle_input.txt";

    public static void main(String[] args) throws FileNotFoundException {
        LensLibraryApp app = new LensLibraryApp();

        // First part
        app.computeScore();

        // Second part
        app.computeScoreV2();
    }

    private final String filePath;
    private InitializationSequence initializationSequence;

    public LensLibraryApp() throws FileNotFoundException {
        String BASE_PATH = "src/main/resources/" + this.getClass().getPackageName() + "/";
        this.filePath = BASE_PATH + PUZZLE_INPUT_FILE_NAME;
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            this.initializationSequence = InitializationSequence.fromRawContent(content);
        }

        System.out.println(this.initializationSequence);
    }

    private void computeScore() {
        Instant start = Instant.now();

        long score = this.initializationSequence.sumHashOfEachStep();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }

    private void computeScoreV2() {
        Instant start = Instant.now();

        long score = new HolidayAsciiStringHelperManualArrangementProcedure(this.initializationSequence).totalFocusingPower();

        Instant end = Instant.now();

        System.out.println("Score Part 2 : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
