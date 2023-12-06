package day06;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WaitForItApp {

    private static final boolean TEST = false;
    private static final String PUZZLE_INPUT_FILE_NAME = TEST ? "puzzle_input_test.txt" : "puzzle_input.txt";

    public static void main(String[] args) throws FileNotFoundException {
        WaitForItApp app = new WaitForItApp();

        // First part
        app.computeScore();
    }

    private final String filePath;
    private SheetOfPaper sheetOfPaper;

    public WaitForItApp() throws FileNotFoundException {
        String BASE_PATH = "src/main/resources/" + this.getClass().getPackageName() + "/";
        this.filePath = BASE_PATH + PUZZLE_INPUT_FILE_NAME;
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        List<Integer> timeAllowedList = new ArrayList<>();
        List<Integer> bestDistanceList = new ArrayList<>();
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            if (content.startsWith("Time")) {
                Scanner scanTime = new Scanner(content.split(":")[1]);
                scanTime.useDelimiter(" ");
                while (scanTime.hasNext()) {
                    if (scanTime.hasNextInt()) {
                        timeAllowedList.add(scanTime.nextInt());
                    }
                    else {
                        scanTime.next();
                    }
                }
            }
            else {
                Scanner scanTime = new Scanner(content.split(":")[1]);
                scanTime.useDelimiter(" ");
                while (scanTime.hasNext()) {
                    if (scanTime.hasNextInt()) {
                        bestDistanceList.add(scanTime.nextInt());
                    }
                    else {
                        scanTime.next();
                    }
                }
            }
        }

        this.sheetOfPaper = SheetOfPaper.fromTimeAndDistanceLists(timeAllowedList, bestDistanceList);
        System.out.println(this.sheetOfPaper);
    }

    private void computeScore() {
        Instant start = Instant.now();

        long score = this.sheetOfPaper.timesOfWaysToBeatTheRecord();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
