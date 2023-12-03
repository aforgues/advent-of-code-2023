package day02;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CubeConundrumApp {

    private static final boolean TEST = false;
    private static final String PUZZLE_INPUT_FILE_NAME = TEST ? "puzzle_input_test.txt" : "puzzle_input.txt";

    public static void main(String[] args) throws FileNotFoundException {
        CubeConundrumApp app = new CubeConundrumApp();

        // First part
        app.computeScore();

        // Second part
        app.computeScorePart2();
    }

    private final String filePath;
    private final List<Game> games;

    public CubeConundrumApp() throws FileNotFoundException {
        String BASE_PATH = "src/main/resources/" + this.getClass().getPackageName() + "/";
        this.filePath = BASE_PATH + PUZZLE_INPUT_FILE_NAME;
        this.games = new ArrayList<>();
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            this.games.add(Game.from(content));
        }
        System.out.println(this.games);
    }

    private void computeScore() {
        Instant start = Instant.now();

        BagGame bagGame = new BagGame(12, 13, 14, this.games);
        long score = bagGame.sumPossibleGameIds();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }

    private void computeScorePart2() {
        Instant start = Instant.now();

        long score = this.games.stream()
                .map(game -> game.power())
                .reduce(0L, Long::sum);

        Instant end = Instant.now();

        System.out.println("Score Part 2 : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
