package day05;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record Almanac(List<Long> tobePlantedSeeds,
                      Map<Long, Long> seedToSoilMap,
                      Map<Long, Long> soilToFertilizerMap,
                      Map<Long, Long> fertilizerToWaterMap,
                      Map<Long, Long> waterToLightMap,
                      Map<Long, Long> lightToTemperatureMap,
                      Map<Long, Long> temperatureToHumidityMap,
                      Map<Long, Long> humidityToLocationMap) {
    public List<Long> computeLocationNumbersRequired() {
        List<Long> locations = new ArrayList<>();
        this.tobePlantedSeeds.forEach(seed -> {
            long soil = Optional.ofNullable(seedToSoilMap.get(seed)).orElse(seed);
            //System.out.println("Seed " + seed + " -> soil : " + soil);

            long fertilizer = Optional.ofNullable(soilToFertilizerMap.get(soil)).orElse(soil);
            //System.out.println("Soil " + soil + " -> fertilizer : " + fertilizer);

            long water = Optional.ofNullable(fertilizerToWaterMap.get(fertilizer)).orElse(fertilizer);
            //System.out.println("Fertilizer " + fertilizer + " -> water : " + water);

            long light = Optional.ofNullable(waterToLightMap.get(water)).orElse(water);
            //System.out.println("Water " + water + " -> light : " + light);

            long temperature = Optional.ofNullable(lightToTemperatureMap.get(light)).orElse(light);
            //System.out.println("Light " + light + " -> temperature : " + temperature);

            long humidity = Optional.ofNullable(temperatureToHumidityMap.get(temperature)).orElse(temperature);
            //System.out.println("Temperature " + temperature + " -> humidity : " + humidity);

            long location = Optional.ofNullable(humidityToLocationMap.get(humidity)).orElse(humidity);
            System.out.println("Seed " + seed + " -> soil : " + soil + " -> Fertilizer " + fertilizer + " -> water : " + water + " -> Light " + light + " -> temperature : " + temperature + " -> humidity " + humidity + " -> location : " + location);
            locations.add(location);
        });
        return locations;
    }
}
