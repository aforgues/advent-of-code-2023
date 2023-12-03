package day02;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record CubesSubset(Map<CubeType, Integer> countCubesByType) {
    public static CubesSubset from(String subSet) {
        Map<CubeType, Integer> countCubesByType = new HashMap<>();
        String[] countAndTypeArr = subSet.split(", ");
        Arrays.stream(countAndTypeArr).forEach(countAndType -> {
            String[] arr = countAndType.split(" ");
            countCubesByType.put(CubeType.valueOf(arr[1].toUpperCase()),Integer.parseInt(arr[0]));
        });
        return new CubesSubset(countCubesByType);
    }

    public boolean isPossible(int nbRedCubesInBag, int nbGreenCubesInBag, int nbBlueCubesInBag) {
        int nbRed = Optional.ofNullable(this.countCubesByType.get(CubeType.RED)).orElse(0);
        int nbGreen = Optional.ofNullable(this.countCubesByType.get(CubeType.GREEN)).orElse(0);
        int nbBlue = Optional.ofNullable(this.countCubesByType.get(CubeType.BLUE)).orElse(0);
        return nbRed <= nbRedCubesInBag && nbGreen <= nbGreenCubesInBag && nbBlue <= nbBlueCubesInBag;
    }
}
