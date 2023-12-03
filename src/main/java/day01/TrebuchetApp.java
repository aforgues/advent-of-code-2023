package day01;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TrebuchetApp {

    private static final boolean TEST = false;
    private static final String PUZZLE_INPUT_FILE_NAME = TEST ? "puzzle_input_test_v2.txt" : "puzzle_input.txt";

    public static void main(String[] args) throws FileNotFoundException {
        TrebuchetApp app = new TrebuchetApp();

        // First part
        //app.computeScore();

        // Second part
        app.computeScorePart2();
    }

    private final String filePath;

    private CalibrationDocument calibrationDocument;
    public TrebuchetApp() throws FileNotFoundException {
        String BASE_PATH = "src/main/resources/" + this.getClass().getPackageName() + "/";
        this.filePath = BASE_PATH + PUZZLE_INPUT_FILE_NAME;
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        List<TextLine> textLines = new ArrayList<>();
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            textLines.add(new TextLine(content));
        }

        this.calibrationDocument = new CalibrationDocument(textLines);
        System.out.println(this.calibrationDocument);
    }

    private void computeScore() {
        Instant start = Instant.now();

        long score = this.calibrationDocument.sumCablibrationValues(false);

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }

    private void computeScorePart2() {
        Instant start = Instant.now();

        long score = this.calibrationDocument.sumCablibrationValues(true);

        Instant end = Instant.now();

        System.out.println("Score Part 2 : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
