package day14;

import utils.Cell;
import utils.Grid;
import utils.Move;
import utils.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Platform(Grid<String> rocks) {

    private static final String ROUNDING_ROCK = "O";
    private static final String CUBE_SHAPED_ROCK = "#";

    public void displayInConsole() {
        this.rocks.displayInConsole();
    }

    public long totalLoadOnSupportBeamToward(Move move) {
        // First tilt the lever on given move (UP)
        Grid<String> updatedRocks = tilt(move);

        // Compute the load of updated rocks
        return computeLoad(updatedRocks);
    }

    private Grid<String> tilt(Move move) {

        // Extract the cube-shaped (#) rocks which won't move
        List<Cell<String>> cubeShapedRocks = this.rocks.cellsByline().values().stream().flatMap(Collection::stream).filter(cell -> cell.data().equals(CUBE_SHAPED_ROCK)).toList();
        System.out.println(cubeShapedRocks);

        // Extract the rounded rocks (O) which won't move
        List<Cell<String>> roundedRocks = this.rocks.cellsByline().values().stream().flatMap(Collection::stream).filter(cell -> cell.data().equals(ROUNDING_ROCK)).toList();
        System.out.println(roundedRocks);

        // Move the rounded rocks until the border, a cube-shaped or another rounded rock
        for (Cell<String> roundedRock : roundedRocks) {
            while (true) {
                // check next position
                Position nextPosition = roundedRock.position().moveTo(move);

                // Stop moving while hitting the border of the grid
                if (!this.rocks.isInside(nextPosition)) {
                    break;
                }

                // Stop moving while hitting a cube-shaped or rounded rock
                if (cubeShapedRocks.contains(new Cell<>(CUBE_SHAPED_ROCK, nextPosition))
                        || roundedRocks.contains(new Cell<>(ROUNDING_ROCK, nextPosition))) {
                    break;
                }

                // Move :)
                roundedRock.moveTo(move);
                //System.out.println("Moved " + roundedRock);
            }
        }

        // Clone the grid
        List<Cell<String>> allRocks = new ArrayList<>();
        allRocks.addAll(cubeShapedRocks);
        allRocks.addAll(roundedRocks);

        Map<Integer, List<Cell<String>>> cellsByLine = allRocks.stream().collect(Collectors.groupingBy(cell -> cell.position().y()));
        Grid<String> updatedRocks = new Grid<>(cellsByLine);
        System.out.println("updatedRocks moving " + move + " : ");
        updatedRocks.displayInConsole();

        return updatedRocks;
    }

    private static long computeLoad(Grid<String> updatedRocks) {
        return updatedRocks.cellsByline().entrySet().stream()
                .map(entry -> (updatedRocks.getLinesNumber() - entry.getKey()) * countRoundedRocksInCells(entry.getValue()))
                .reduce(0L, Long::sum);
    }

    private static long countRoundedRocksInCells(List<Cell<String>> cells) {
        return cells.stream().filter(cell -> cell.data().equals(ROUNDING_ROCK)).count();
    }
}
