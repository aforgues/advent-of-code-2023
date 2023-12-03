package day02;

import java.util.List;

public record BagGame(int nbRedCubesInBag, int nbGreenCubesInBag, int nbBlueCubesInBag, List<Game> games) {
    public long sumPossibleGameIds() {
        return games.stream().filter(game -> game.isPossible(nbRedCubesInBag, nbGreenCubesInBag, nbBlueCubesInBag))
                .map(game -> game.id())
                .reduce(0, Integer::sum);
    }
}
