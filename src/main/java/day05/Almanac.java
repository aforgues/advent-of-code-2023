package day05;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Almanac(List<Long> tobePlantedSeeds,
                      List<MapConversionRule> seedToSoilMap,
                      List<MapConversionRule> soilToFertilizerMap,
                      List<MapConversionRule> fertilizerToWaterMap,
                      List<MapConversionRule> waterToLightMap,
                      List<MapConversionRule> lightToTemperatureMap,
                      List<MapConversionRule> temperatureToHumidityMap,
                      List<MapConversionRule> humidityToLocationMap) {
    public List<Long> computeLocationNumbersRequired() {
        List<Long> locations = new ArrayList<>();
        this.tobePlantedSeeds.forEach(seed -> {
            long soil = getDestinationNumberForRules(seedToSoilMap, seed);
            //System.out.println("Seed " + seed + " -> soil : " + soil);

            long fertilizer = getDestinationNumberForRules(soilToFertilizerMap, soil);
            //System.out.println("Soil " + soil + " -> fertilizer : " + fertilizer);

            long water = getDestinationNumberForRules(fertilizerToWaterMap, fertilizer);
            //System.out.println("Fertilizer " + fertilizer + " -> water : " + water);

            long light = getDestinationNumberForRules(waterToLightMap, water);
            //System.out.println("Water " + water + " -> light : " + light);

            long temperature = getDestinationNumberForRules(lightToTemperatureMap, light);
            //System.out.println("Light " + light + " -> temperature : " + temperature);

            long humidity = getDestinationNumberForRules(temperatureToHumidityMap, temperature);
            //System.out.println("Temperature " + temperature + " -> humidity : " + humidity);

            long location = getDestinationNumberForRules(humidityToLocationMap, humidity);
            System.out.println("Seed " + seed + " -> soil : " + soil + " -> Fertilizer " + fertilizer + " -> water : " + water + " -> Light " + light + " -> temperature : " + temperature + " -> humidity " + humidity + " -> location : " + location);
            locations.add(location);
        });
        return locations;
    }

    private static long getDestinationNumberForRules(List<MapConversionRule> rules, Long source) {
        Optional<Long> destination = Optional.empty();
        for (MapConversionRule rule : rules) {
            destination = rule.getDestinationNumber(source);
            if (destination.isPresent())
                break;
        }
        return destination.orElse(source);
    }
}
