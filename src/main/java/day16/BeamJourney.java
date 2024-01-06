package day16;

import utils.*;

import java.util.*;
import java.util.stream.Collectors;

public class BeamJourney {
    private static final boolean DEBUG = false;

    private final ContraptionLayout contraptionLayout;

    public BeamJourney(final ContraptionLayout contraptionLayout) {
        this.contraptionLayout = contraptionLayout;
    }

    public long countEnergizedTiles() {
        Set<Position> path = walkthrough(new Position(0,0), Move.RIGHT);
        displayEnergizedTilesInGrid(path);
        return path.size();
    }

    private static void displayEnergizedTilesInGrid(Set<Position> path) {
        List<Cell<String>> cellsUpdated = new ArrayList<>();
        for (Position position : path) {
            cellsUpdated.add(new Cell<>("#", position));
        }

        Grid<String> grid = new Grid<>(cellsUpdated.stream().collect(Collectors.groupingBy(cell -> cell.position().y())));
        grid.displayInConsole();
    }

    private Set<Position> walkthrough(Position initPosition, Move initMove) {
        Set<Position> path = new HashSet<>();
        Queue<Tuple<Position, Move>> pathToExplore = new ArrayDeque<>();
        pathToExplore.add(new Tuple<>(initPosition, initMove));

        List<Tuple<Position, Move>> alreadyExplored = new ArrayList<>();
        while(! pathToExplore.isEmpty()) {
            Tuple<Position, Move> step = pathToExplore.poll();
            Position currentPosition = step.x();
            Move currentMove = step.y();

            if (! alreadyExplored.contains(step)) {
                if (DEBUG)
                    System.out.println("Exploring " + step);
                alreadyExplored.add(step);
            }
            else {
                continue;
            }

            path.add(currentPosition);

            Position nextPosition = currentPosition.moveTo(currentMove);
            if (DEBUG)
                System.out.println("    Next position : " + nextPosition);

            if (this.contraptionLayout.isPositionInsideGrid(nextPosition)) {
                path.add(nextPosition);

                Optional<Cell<String>> nextCell = this.contraptionLayout.findCellAt(nextPosition);

                if (nextCell.isEmpty()) {
                    if (DEBUG)
                        System.out.println("        No cell (.) at " + nextPosition + " => beam continue to " + currentMove);
                    pathToExplore.add(new Tuple<>(nextPosition, currentMove));
                }
                else {
                    if (DEBUG)
                        System.out.println("        Next position " + nextPosition + " hit a cell " + nextCell.get().data());
                    switch (nextCell.get().data()) {
                        case "|":
                            if (currentMove == Move.RIGHT || currentMove == Move.LEFT) {
                                if (DEBUG)
                                    System.out.println("            Need to split the beam going next to UP and DOWN");
                                // Split the beam and change currentMove
                                pathToExplore.add(new Tuple<>(nextPosition, Move.UP));
                                pathToExplore.add(new Tuple<>(nextPosition, Move.DOWN));
                            }
                            else {
                                if (DEBUG)
                                    System.out.println("            Beam continue through the splitter as going " + currentMove + " through |");
                                pathToExplore.add(new Tuple<>(nextPosition, currentMove));
                            }
                            break;

                        case "-":
                            if (currentMove == Move.UP || currentMove == Move.DOWN) {
                                if (DEBUG)
                                    System.out.println("            Need to split the beam going next to LEFT and RIGHT");
                                // Split the beam and change currentMove
                                pathToExplore.add(new Tuple<>(nextPosition, Move.LEFT));
                                pathToExplore.add(new Tuple<>(nextPosition, Move.RIGHT));
                            }
                            else {
                                if (DEBUG)
                                    System.out.println("            Beam continue through the splitter as going " + currentMove + " through -");
                                pathToExplore.add(new Tuple<>(nextPosition, currentMove));
                            }
                            break;

                        case "/":
                            Move nextMove = switch (currentMove) {
                                case RIGHT -> Move.UP;
                                case LEFT -> Move.DOWN;
                                case DOWN -> Move.LEFT;
                                case UP -> Move.RIGHT;
                            };
                            if (DEBUG)
                                System.out.println("            Need to reflect the beam as going next at 90° toward " + nextMove);
                            pathToExplore.add(new Tuple<>(nextPosition, nextMove));
                            break;

                        case "\\":
                            Move nextMove2 = switch (currentMove) {
                                case RIGHT -> Move.DOWN;
                                case LEFT -> Move.UP;
                                case DOWN -> Move.RIGHT;
                                case UP -> Move.LEFT;
                            };
                            if (DEBUG)
                                System.out.println("            Need to reflect the beam as going next at 90° toward " + nextMove2);
                            pathToExplore.add(new Tuple<>(nextPosition, nextMove2));
                            break;
                    }
                }
            }
            else {
                if (DEBUG)
                    System.out.println("    Out of grid => ignoring !");
            }
        }
        return path;
    }

    public long countLargestEnergizedTiles() {
        long largestPathSize = 0;

        // Check every possible option
        // - First Line to DOWN
        for (int column = 0; column < this.contraptionLayout.grid().getColumnsNumber(); column++) {
            System.out.println("First line DOWN " + column);
            Set<Position> path = walkthrough(new Position(column,0), Move.DOWN);
            if (DEBUG)
                displayEnergizedTilesInGrid(path);
            largestPathSize = Math.max(largestPathSize, path.size());
        }

        // - Last Line to UP
        for (int column = 0; column < this.contraptionLayout.grid().getColumnsNumber(); column++) {
            System.out.println("Last line UP " + column);
            Set<Position> path = walkthrough(new Position(column,this.contraptionLayout.grid().getLinesNumber() - 1), Move.UP);
            if (DEBUG)
                displayEnergizedTilesInGrid(path);
            largestPathSize = Math.max(largestPathSize, path.size());
        }

        // - First Column to RIGHT
        for (int line = 0; line < this.contraptionLayout.grid().getLinesNumber(); line++) {
            System.out.println("First column RIGHT " + line);
            Set<Position> path = walkthrough(new Position(0,line), Move.RIGHT);
            if (DEBUG)
                displayEnergizedTilesInGrid(path);
            largestPathSize = Math.max(largestPathSize, path.size());
        }

        // - Last Column to LEFT
        for (int line = 0; line < this.contraptionLayout.grid().getLinesNumber(); line++) {
            System.out.println("Last column LEFT " + line);
            Set<Position> path = walkthrough(new Position(this.contraptionLayout.grid().getColumnsNumber() - 1, line), Move.LEFT);
            if (DEBUG)
                displayEnergizedTilesInGrid(path);
            largestPathSize = Math.max(largestPathSize, path.size());
        }
        return largestPathSize;
    }
}
