package day03;

import utils.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GearRatiosApp {

    private static final boolean TEST = false;
    private static final String PUZZLE_INPUT_FILE_NAME = TEST ? "puzzle_input_test.txt" : "puzzle_input.txt";

    public static void main(String[] args) throws FileNotFoundException {
        GearRatiosApp app = new GearRatiosApp();

        // First part
        app.computeScore();
    }

    private final String filePath;
    private EngineSchematic engineSchematic;

    public GearRatiosApp() throws FileNotFoundException {
        String BASE_PATH = "src/main/resources/" + this.getClass().getPackageName() + "/";
        this.filePath = BASE_PATH + PUZZLE_INPUT_FILE_NAME;
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        List<Symbol> symbols = new ArrayList<>();
        List<PartNumber> partNumbers = new ArrayList<>();
        int lineSize = 0;

        int lineNumber = 0;
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);

            lineSize = content.length();
            for (int column = 0; column < lineSize; column++) {
                char c = content.charAt(column);

                if (c == '.') {
                    continue;
                }

                if (Symbol.isSymbol(c)) {
                    symbols.add(new Symbol(new Position(column, lineNumber), c));
                }
                else {
                    StringBuilder pn = new StringBuilder(Character.toString(c));
                    int next = column + 1;
                    while (next < lineSize) {
                        char nextChar = content.charAt(next);
                        if (Symbol.isSymbol(nextChar) || nextChar == '.') {
                            break;
                        }
                        pn.append(nextChar);
                        next++;
                    }
                    partNumbers.add(new PartNumber(new Position(column, lineNumber), Integer.parseInt(pn.toString())));
                    column = (next - 1);
                }
            }


            lineNumber++;
        }

        this.engineSchematic = new EngineSchematic(symbols, partNumbers, lineSize, lineNumber);
        System.out.println(this.engineSchematic);
        this.engineSchematic.displaySchema();
    }

    private void computeScore() {
        Instant start = Instant.now();

        long score = this.engineSchematic.sumPartNumbers();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
