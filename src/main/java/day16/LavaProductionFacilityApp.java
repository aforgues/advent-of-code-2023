package day16;

import utils.Cell;
import utils.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LavaProductionFacilityApp {

    private static final boolean TEST = false;
    private static final String PUZZLE_INPUT_FILE_NAME = TEST ? "puzzle_input_test.txt" : "puzzle_input.txt";

    public static void main(String[] args) throws FileNotFoundException {
        LavaProductionFacilityApp app = new LavaProductionFacilityApp();

        // First part
        app.computeScore();
    }

    private final String filePath;
    private ContraptionLayout contraptionLayout;

    public LavaProductionFacilityApp() throws FileNotFoundException {
        String BASE_PATH = "src/main/resources/" + this.getClass().getPackageName() + "/";
        this.filePath = BASE_PATH + PUZZLE_INPUT_FILE_NAME;
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        List<Cell<String>> cells = new ArrayList<>();
        int lineNumber = 0;
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);

            for (int column = 0; column < content.length(); column++) {
                char c = content.charAt(column);
                if (c != '.') {
                    cells.add(new Cell<>(String.valueOf(c), new Position(column, lineNumber)));
                }
            }

            lineNumber++;
        }
        this.contraptionLayout = new ContraptionLayout(cells);

        System.out.println(this.contraptionLayout);
        this.contraptionLayout.displayAsGridInConsole();
    }

    private void computeScore() {
        Instant start = Instant.now();

        long score = new BeamJourney(this.contraptionLayout).countEnergizedTiles();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
