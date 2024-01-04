package day14;

import utils.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ParabolicReflectorDishApp {

    private static final boolean TEST = false;
    private static final String PUZZLE_INPUT_FILE_NAME = TEST ? "puzzle_input_test.txt" : "puzzle_input.txt";

    public static void main(String[] args) throws FileNotFoundException {
        ParabolicReflectorDishApp app = new ParabolicReflectorDishApp();

        // First part
        app.computeScore();
    }

    private final String filePath;
    private Platform platorm;

    public ParabolicReflectorDishApp() throws FileNotFoundException {
        String BASE_PATH = "src/main/resources/" + this.getClass().getPackageName() + "/";
        this.filePath = BASE_PATH + PUZZLE_INPUT_FILE_NAME;
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        Grid<String> rocks = new Grid<>(new HashMap<>());
        int lineNumber = 0;
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);

            List<Cell<String>> cells = new ArrayList<>();
            for (int col = 0; col < content.length(); col++) {
                cells.add(new Cell<>(String.valueOf(content.charAt(col)), new Position(col, lineNumber)));
            }
            rocks.addLine(lineNumber, cells);
            lineNumber++;
        }
        this.platorm = new Platform(rocks);
        this.platorm.displayInConsole();
    }

    private void computeScore() {
        Instant start = Instant.now();

        long score = this.platorm.totalLoadOnSupportBeamToward(Move.UP);

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
