package day14;

import utils.Cell;
import utils.Grid;
import utils.Move;
import utils.Position;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static utils.Move.DOWN;
import static utils.Move.RIGHT;

public record Platform(Grid<String> rocks) {

    private static final String ROUNDING_ROCK = "O";
    private static final String CUBE_SHAPED_ROCK = "#";

    public void displayInConsole() {
        this.rocks.displayInConsole();
    }

    public long totalLoadOnSupportBeamToward() {
        // First tilt the lever on given move (UP)
        Grid<String> updatedRocks = tilt(List.of(Move.UP), 1);

        // Compute the load of updated rocks
        return computeLoad(updatedRocks);
    }

    private Grid<String> tilt(List<Move> moves, int nbCycles) {

        // Extract the cube-shaped (#) rocks which won't move
        List<Cell<String>> cubeShapedRocks = this.rocks.cellsByline().values().stream().flatMap(Collection::stream).filter(cell -> cell.data().equals(CUBE_SHAPED_ROCK)).toList();
        System.out.println(cubeShapedRocks);

        // Extract the rounded rocks (O) which won't move
        List<Cell<String>> roundedRocks = this.rocks.cellsByline().values().stream().flatMap(Collection::stream).filter(cell -> cell.data().equals(ROUNDING_ROCK)).toList();
        System.out.println(roundedRocks);

        // Move as many times as nbCycles
        for (int i = 1; i <= nbCycles; i++) {
            // Each cycle, move in each listed directions (moves)
            for (Move move : moves) {
                // First sort the rounded rocks depending on move
                // if NORTH, we need to handle first the rocks which are the upper position first
                // same for WEST, we need to handle first the rocks which are the most on the left first
                // ...
                Stream<Cell<String>> sortedStream = roundedRocks.stream().sorted(Comparator.comparing(cell -> switch (move) {
                    case UP -> cell.position().y();
                    case DOWN -> -cell.position().y();
                    case LEFT -> cell.position().x();
                    case RIGHT -> -cell.position().x();
                }));

                // Move the rounded rocks until the border, a cube-shaped or another rounded rock
                for (Cell<String> roundedRock : sortedStream.toList()) {
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
                //System.out.println("After move " + move + " during cycle " + (i+1));
                //computeNewGridFrom(cubeShapedRocks, roundedRocks);
            }
            Grid<String> temp = computeNewGridFrom(cubeShapedRocks, roundedRocks);
            long load = computeLoad(temp);
            System.out.println("After full cycle " + i + " : load = " + load);

            /**
             *  MANUAL solution for part 2
             *
             *  Stop the program after running ~1000 cycles for instance
             *
             *  1. Analyse some series of number repeating
             *      => For the test input, a loop starts at cycle 3 and repeats at cycle 10
             *      => For the target input, a loop starts at cycle 83 and repeats at cycle 160
             *  2. To get the cycle which will match with cycle 1_000_000_000, we do :
             *   (1_000_000_000 - startLoopCycleNb) % (nextStartLoopCycleNb - startLoopCycleNb) + startLoopCycleNb
             *      => For test input, we got cycle 6 (3+3) which has a load of 64
             *      => For target input, we got cycle 153 (70+83) which has a load of 86069
             */
        }

        // Clone the grid
        System.out.println("After moving " + moves + " and " + nbCycles + " cycles : ");
        return computeNewGridFrom(cubeShapedRocks, roundedRocks);
    }

    private static Grid<String> computeNewGridFrom(List<Cell<String>> cubeShapedRocks, List<Cell<String>> roundedRocks) {
        List<Cell<String>> allRocks = new ArrayList<>();
        allRocks.addAll(cubeShapedRocks);
        allRocks.addAll(roundedRocks);

        Map<Integer, List<Cell<String>>> cellsByLine = allRocks.stream().collect(Collectors.groupingBy(cell -> cell.position().y()));
        Grid<String> updatedRocks = new Grid<>(cellsByLine);
        //System.out.println("updatedRocks : ");
        //updatedRocks.displayInConsole();

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

    public long totalLoadOnSupportBeamTowardAfterMultipleCycles(int nbCycles) {
        // Each cycle is to tilt NORTH, then WEST, then SOUTH, then EAST
        Grid<String> updatedRocks = tilt(List.of(Move.UP, Move.LEFT, DOWN, RIGHT), nbCycles);

        // Compute the load of updated rocks
        return computeLoad(updatedRocks);
    }
}
