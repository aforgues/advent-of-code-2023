package day13;

import utils.Cell;
import utils.Grid;
import utils.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class PointOfIncidenceApp {

    private static final boolean TEST = false;
    private static final String PUZZLE_INPUT_FILE_NAME = TEST ? "puzzle_input_test.txt" : "puzzle_input.txt";

    public static void main(String[] args) throws FileNotFoundException {
        PointOfIncidenceApp app = new PointOfIncidenceApp();

        // First part
        app.computeScore();
    }

    private final String filePath;
    private final MirrorPatterns mirrorPatterns;

    public PointOfIncidenceApp() throws FileNotFoundException {
        String BASE_PATH = "src/main/resources/" + this.getClass().getPackageName() + "/";
        this.filePath = BASE_PATH + PUZZLE_INPUT_FILE_NAME;
        this.mirrorPatterns = new MirrorPatterns(new ArrayList<>());
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        int lineNumber = 0;
        Grid<String> grid = new Grid<>(new HashMap<>());
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);

            if (content.length() == 0) {
                this.mirrorPatterns.addPattern(grid);
                grid = new Grid<>(new HashMap<>());
                lineNumber = 0;
                continue;
            }
            List<Cell<String>> cells = new ArrayList<>();
            for (int col = 0; col < content.length(); col++) {
                cells.add(new Cell<>(String.valueOf(content.charAt(col)), new Position(col, lineNumber)));
            }
            grid.addLine(lineNumber, cells);
            lineNumber++;
        }
        this.mirrorPatterns.addPattern(grid);

        //System.out.println(this.mirrorPatterns);
        //this.mirrorPatterns.displayInConsole();
    }

    private void computeScore() {
        Instant start = Instant.now();

        long score = this.mirrorPatterns.summarize();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
