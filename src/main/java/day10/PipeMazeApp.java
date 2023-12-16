package day10;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.Scanner;

public class PipeMazeApp {

    private static final boolean TEST = true;
    private static final String PUZZLE_INPUT_FILE_NAME = TEST ? "puzzle_input_test.txt" : "puzzle_input.txt";

    public static void main(String[] args) throws FileNotFoundException {
        PipeMazeApp app = new PipeMazeApp();

        // First part
        app.computeScore();

        // Second part
        app.computeScorePart2();
    }

    private final String filePath;
    private TilesGrid tilesGrid;

    public PipeMazeApp() throws FileNotFoundException {
        String BASE_PATH = "src/main/resources/" + this.getClass().getPackageName() + "/";
        this.filePath = BASE_PATH + PUZZLE_INPUT_FILE_NAME;
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        this.tilesGrid = new TilesGrid();
        int lineNumber = 0;
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);

            this.tilesGrid.addLineFromRawContent(content, lineNumber++);
        }

        System.out.println(this.tilesGrid);
        this.tilesGrid.displayInConsole();
    }

    private void computeScore() {
        Instant start = Instant.now();

        long score = this.tilesGrid.stepsAlongTheLoopToTheFarthestPoint();

        Instant end = Instant.now();

        this.tilesGrid.displayInConsole();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }

    private void computeScorePart2() {
        Instant start = Instant.now();

        long score = this.tilesGrid.countEnclosedTilesByTheLoop();

        Instant end = Instant.now();

        System.out.println("Score Part 2 : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
