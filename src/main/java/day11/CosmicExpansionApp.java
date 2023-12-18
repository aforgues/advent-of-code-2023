package day11;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.Scanner;

public class CosmicExpansionApp {

    private static final boolean TEST = false;
    private static final String PUZZLE_INPUT_FILE_NAME = TEST ? "puzzle_input_test.txt" : "puzzle_input.txt";

    public static void main(String[] args) throws FileNotFoundException {
        CosmicExpansionApp app = new CosmicExpansionApp();

        // First part
        app.computeScore();

        // Second part
        app.computeScoreV2();
    }

    private final String filePath;
    private GiantImage giantImage;

    public CosmicExpansionApp() {
        String BASE_PATH = "src/main/resources/" + this.getClass().getPackageName() + "/";
        this.filePath = BASE_PATH + PUZZLE_INPUT_FILE_NAME;
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        this.giantImage = new GiantImage();
        int lineNumber = 0;
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            giantImage.addLine(content, lineNumber++);
        }
        System.out.println(this.giantImage);
        this.giantImage.displayInConsole();
    }

    private void computeScore() throws FileNotFoundException {
        this.parseFile();
        Instant start = Instant.now();

        long score = this.giantImage.sumShortestPathBetweenGalaxies();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }

    private void computeScoreV2() throws FileNotFoundException {
        this.parseFile();
        Instant start = Instant.now();

        long score = this.giantImage.sumShortestPathBetweenMuchOlderGalaxies();

        Instant end = Instant.now();

        System.out.println("Score Part 2 : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
