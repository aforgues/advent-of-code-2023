package day05;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.*;

public class SeedFertilizerApp {

    private static final boolean TEST = false;
    private static final String PUZZLE_INPUT_FILE_NAME = TEST ? "puzzle_input_test.txt" : "puzzle_input.txt";
    private static final String SEEDS = "seeds: ";
    private static final String SEED_TO_SOIL_MAP = "seed-to-soil map:";
    private static final String SOIL_TO_FERTILIZER_MAP = "soil-to-fertilizer map:";
    private static final String FERTILIZER_TO_WATER_MAP = "fertilizer-to-water map:";
    private static final String WATER_TO_LIGHT_MAP = "water-to-light map:";
    private static final String LIGHT_TO_TEMPERATURE_MAP = "light-to-temperature map:";
    private static final String TEMPERATURE_TO_HUMIDITY_MAP = "temperature-to-humidity map:";
    private static final String HUMIDITY_TO_LOCATION_MAP = "humidity-to-location map:";

    public static void main(String[] args) throws FileNotFoundException {
        SeedFertilizerApp app = new SeedFertilizerApp();

        // First part
        app.computeScore();

        // Second part
        app.computeScorePart2();
    }

    private final String filePath;

    private Almanac almanac;
    public SeedFertilizerApp() throws FileNotFoundException {
        String BASE_PATH = "src/main/resources/" + this.getClass().getPackageName() + "/";
        this.filePath = BASE_PATH + PUZZLE_INPUT_FILE_NAME;
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        List<Long> toBePlantedSeeds = new ArrayList<>();
        List<MapConversionRule> seedToSoilMap = new ArrayList<>();
        List<MapConversionRule> soilToFertilizerMap = new ArrayList<>();
        List<MapConversionRule> fertilizerToWaterMap = new ArrayList<>();
        List<MapConversionRule> waterToLightMap = new ArrayList<>();
        List<MapConversionRule> lightToTemperatureMap = new ArrayList<>();
        List<MapConversionRule> temperatureToHumidityMap = new ArrayList<>();
        List<MapConversionRule> humidityToLocationMap = new ArrayList<>();

        while (scanner.hasNext()) {
            String content = scanner.next();
            //System.out.println(content);

            if (content.startsWith(SEEDS)) {
                System.out.println(content);
                toBePlantedSeeds = Arrays.stream(content.substring(SEEDS.length()).split(" ")).map(Long::parseLong).toList();
                // Skip next blank line
                scanner.next();
                continue;
            }

            if (content.startsWith(SEED_TO_SOIL_MAP)) {
                computeMappings(SEED_TO_SOIL_MAP, seedToSoilMap, scanner);
                continue;
            }

            if (content.startsWith(SOIL_TO_FERTILIZER_MAP)) {
                computeMappings(SOIL_TO_FERTILIZER_MAP, soilToFertilizerMap, scanner);
                continue;
            }

            if (content.startsWith(FERTILIZER_TO_WATER_MAP)) {
                computeMappings(FERTILIZER_TO_WATER_MAP, fertilizerToWaterMap, scanner);
                continue;
            }

            if (content.startsWith(WATER_TO_LIGHT_MAP)) {
                computeMappings(WATER_TO_LIGHT_MAP, waterToLightMap, scanner);
                continue;
            }

            if (content.startsWith(LIGHT_TO_TEMPERATURE_MAP)) {
                computeMappings(LIGHT_TO_TEMPERATURE_MAP, lightToTemperatureMap, scanner);
                continue;
            }

            if (content.startsWith(TEMPERATURE_TO_HUMIDITY_MAP)) {
                computeMappings(TEMPERATURE_TO_HUMIDITY_MAP, temperatureToHumidityMap, scanner);
                continue;
            }

            if (content.startsWith(HUMIDITY_TO_LOCATION_MAP)) {
                computeMappings(HUMIDITY_TO_LOCATION_MAP, humidityToLocationMap, scanner);
            }
        }

        this.almanac = new Almanac(toBePlantedSeeds, seedToSoilMap, soilToFertilizerMap, fertilizerToWaterMap, waterToLightMap, lightToTemperatureMap, temperatureToHumidityMap, humidityToLocationMap);
        System.out.println(this.almanac);
    }

    private static void computeMappings(String mapLabel, List<MapConversionRule> rules, Scanner scanner) {
        String mapLine = scanner.next();
        while(mapLine.length() > 0) {
            MapConversionRule rule = MapConversionRule.fromRawContent(mapLine);
            //System.out.println("rule : " + rule);
            rules.add(rule);
            if (scanner.hasNext()) {
                mapLine = scanner.next();
            }
            else {
                break;
            }
        }
        //System.out.println(mapLabel + " " + map);
    }

    private void computeScore() {
        Instant start = Instant.now();

        long score = this.almanac.computeLowestLocationNumberRequired(false);

        Instant end = Instant.now();

        System.out.println("Score Part 1 : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }

    private void computeScorePart2() {
        Instant start = Instant.now();

        long score = this.almanac.computeLowestLocationNumberRequired(true);

        Instant end = Instant.now();

        System.out.println("Score Part 2 : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
