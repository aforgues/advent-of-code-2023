package day04;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ScratchcardsApp {

    private static final boolean TEST = false;
    private static final String PUZZLE_INPUT_FILE_NAME = TEST ? "puzzle_input_test.txt" : "puzzle_input.txt";

    public static void main(String[] args) throws FileNotFoundException {
        ScratchcardsApp app = new ScratchcardsApp();

        // First part
        app.computeScore();

        // Second part
        app.computeScorePart2();
    }

    private final String filePath;
    private final List<ScratchCard> scratchCards;

    public ScratchcardsApp() throws FileNotFoundException {
        String BASE_PATH = "src/main/resources/" + this.getClass().getPackageName() + "/";
        this.filePath = BASE_PATH + PUZZLE_INPUT_FILE_NAME;
        this.scratchCards = new ArrayList<>();
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);

            this.scratchCards.add(ScratchCard.from(content));
        }
        System.out.println(this.scratchCards);
    }

    private void computeScore() {
        Instant start = Instant.now();

        long score = new Game(this.scratchCards).sumPilePoints();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }

    private void computeScorePart2() {
        Instant start = Instant.now();

        long score = new Game(this.scratchCards).countTotalScratchCards();

        Instant end = Instant.now();

        System.out.println("Score Part 2: " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
