package day02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Game(int id, List<CubesSubset> cubesSubsets) {
    public static Game from(String content) {
        String[] arr = content.split(": ");
        int id = Integer.parseInt(arr[0].split(" ")[1]);
        List<CubesSubset> cubesSubsets = new ArrayList<>();
        Arrays.stream(arr[1].split("; ")).forEach(subSet -> cubesSubsets.add(CubesSubset.from(subSet)));
        return new Game(id, cubesSubsets);
    }

    public boolean isPossible(int nbRedCubesInBag, int nbGreenCubesInBag, int nbBlueCubesInBag) {
        return this.cubesSubsets.stream().allMatch(cubesSubset -> cubesSubset.isPossible(nbRedCubesInBag, nbGreenCubesInBag, nbBlueCubesInBag));
    }

    public long power() {
        int minRed = this.fewestNbOfCubesOfType(CubeType.RED);
        int minGreen = this.fewestNbOfCubesOfType(CubeType.GREEN);
        int minBlue = this.fewestNbOfCubesOfType(CubeType.BLUE);
        return minRed * minGreen * minBlue;
    }

    private int fewestNbOfCubesOfType(CubeType cubeType) {
        return this.cubesSubsets.stream().filter(cubesSubset -> cubesSubset.countCubesByType().get(cubeType) != null)
                .map(cubesSubset -> cubesSubset.countCubesByType().get(cubeType))
                .max(Integer::compare).orElse(0);
    }
}
