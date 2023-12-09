package day08;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.*;

public class HauntedWastelandApp {

    private static final boolean TEST = false;
    private static final String PUZZLE_INPUT_FILE_NAME = TEST ? "puzzle_input_test.txt" : "puzzle_input.txt";

    public static void main(String[] args) throws FileNotFoundException {
        HauntedWastelandApp app = new HauntedWastelandApp();

        // First part
        //app.computeScore();

        // Second part
        app.computeScorePart2();
    }

    private final String filePath;
    private DesertMap desertMap;

    public HauntedWastelandApp() throws FileNotFoundException {
        String BASE_PATH = "src/main/resources/" + this.getClass().getPackageName() + "/";
        this.filePath = BASE_PATH + PUZZLE_INPUT_FILE_NAME;
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        NavigationInstructions navigationInstructions = null;
        List<NodesLink> nodesLinkList = new ArrayList<>();

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);

            if (navigationInstructions == null) {
                navigationInstructions = NavigationInstructions.fromRawContent(content);
                scanner.next();
                continue;
            }
            nodesLinkList.add(NodesLink.fromRawContent(content));
        }

        this.desertMap = new DesertMap(navigationInstructions, Network.fromNodesLinkList(nodesLinkList));
        System.out.println(this.desertMap);
    }

    private void computeScore() {
        Instant start = Instant.now();

        long score = this.desertMap.stepsToFinishNode();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }

    private void computeScorePart2() {
        Instant start = Instant.now();

        long score = this.desertMap.ghostStepsToFinishNode();

        Instant end = Instant.now();

        System.out.println("Score Part 2 : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
