package day07;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.Scanner;
import java.util.TreeMap;

public class CamelCardsApp {

    private static final boolean TEST = false;
    private static final String PUZZLE_INPUT_FILE_NAME = TEST ? "puzzle_input_test.txt" : "puzzle_input.txt";

    public static void main(String[] args) throws FileNotFoundException {
        CamelCardsApp app = new CamelCardsApp();

        // First part
        app.computeScore();

        // Second part
        app.computeScorePart2();
    }

    private final String filePath;
    private CamelCards camelCards;

    public CamelCardsApp() {
        String BASE_PATH = "src/main/resources/" + this.getClass().getPackageName() + "/";
        this.filePath = BASE_PATH + PUZZLE_INPUT_FILE_NAME;
    }

    private void parseFile(boolean useNewJokerRule) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        TreeMap<Hand, Integer> bidsByHand = new TreeMap<>();
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);

            String[] arr = content.split(" ");
            bidsByHand.put(Hand.fromRawContent(arr[0], useNewJokerRule), Integer.parseInt(arr[1]));
        }

        this.camelCards = new CamelCards(bidsByHand);
        System.out.println(this.camelCards);
    }

    private void computeScore() throws FileNotFoundException {
        Instant start = Instant.now();

        this.parseFile(false);
        long score = this.camelCards.totalWinnings();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }

    private void computeScorePart2() throws FileNotFoundException {
        Instant start = Instant.now();

        this.parseFile(true);
        long score = this.camelCards.totalWinnings();

        Instant end = Instant.now();

        System.out.println("Score Part 2 : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
