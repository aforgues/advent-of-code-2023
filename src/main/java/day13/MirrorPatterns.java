package day13;

import utils.Cell;
import utils.Grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record MirrorPatterns(List<Grid<String>> grids) {
    public void addPattern(Grid<String> grid) {
        this.grids.add(grid);
    }

    public void displayInConsole() {
        for (Grid<String> grid : this.grids) {
            grid.displayInConsole();
        }
    }

    public long summarize() {
        return this.grids.stream()
                .map(MirrorPatterns::reflectionScore)
                .reduce(0L, Long::sum);
    }

    private static long reflectionScore(Grid<String> grid) {
        System.out.println();
        grid.displayInConsole();

        long vscore = computeVerticalReflectionScore(grid);
        System.out.println("Vertical match score : " + vscore);

        long hscore = computeHorizontalReflectionScore(grid);
        System.out.println("Horizontal match score : " + hscore);
        return vscore + hscore;
    }

    private static long computeVerticalReflectionScore(Grid<String> grid) {
        // identify gap between matching columns
        Map<Integer, List<Integer>> matchingColumnsByColumnNumber = new HashMap<>();

        for (int i = 0; i < grid.getColumnsNumber(); i++) {
            List<Cell<String>> columnCells = grid.getColumn(i);
            for (int j = 0; j < grid.getColumnsNumber(); j++) {
                if (i != j) {
                    if (grid.hasSameColumnDataAt(j, columnCells)) {
                        List<Integer> matchingColumns = matchingColumnsByColumnNumber.computeIfAbsent(i, k -> new ArrayList<>());
                        matchingColumns.add(j);
                        //System.out.println("Column " + (i+1) + " is the same as column " + (j + 1));
                    }
                }
            }
        }
        System.out.println("Matching columns : " + matchingColumnsByColumnNumber);

        int previousGap = 0;
        Map<Integer, Integer> gapByColumnNumber = new HashMap<>();
        for (Integer columnNumber : matchingColumnsByColumnNumber.keySet()) {
            List<Integer> matchingColumns = matchingColumnsByColumnNumber.get(columnNumber);
            // FIXME : handle multiple matchings
            int gap = matchingColumns.get(0) - columnNumber;
            gapByColumnNumber.put(columnNumber, gap);

            // we already defined an expected gap => next one should be the same - 2
            if (previousGap != 0) {
                if (gap == (previousGap - 2)) {
                    previousGap = gap;
                    continue;
                }
                return 0;
            }
            previousGap = gap;
        }
        System.out.println("Gaps : " + gapByColumnNumber);

        Integer firstColumnWithGapOfOne = gapByColumnNumber.entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (firstColumnWithGapOfOne != null) {
            // Need to check that column out of the pattern can be ignored
            //  => potential reflection column is out of the pattern
            for (int column = 0; column < grid.getColumnsNumber(); column++) {
                if (gapByColumnNumber.get(column) != null) {
                    continue;
                }

                int targetReflectionColumnNumber = firstColumnWithGapOfOne * 2 - column + 1;
                if (targetReflectionColumnNumber >= 0 && targetReflectionColumnNumber < grid.getColumnsNumber()) {
                    return 0;
                }
            }
        }


        // Compute score based on firstColumnWithGapOfOne
        return firstColumnWithGapOfOne != null ? firstColumnWithGapOfOne + 1 : 0;
    }

    private static long computeHorizontalReflectionScore(Grid<String> grid) {
        // identify gap between matching lines
        Map<Integer, List<Integer>> matchingLinesByLineNumber = new HashMap<>();

        for (int i = 0; i < grid.getLinesNumber(); i++) {
            List<Cell<String>> lineCells = grid.getLine(i);
            for (int j = 0; j < grid.getLinesNumber(); j++) {
                if (i != j) {
                    if (grid.hasSameLineDataAt(j, lineCells)) {
                        List<Integer> matchingLines = matchingLinesByLineNumber.computeIfAbsent(i, k -> new ArrayList<>());
                        matchingLines.add(j);
                        //System.out.println("Line " + (i+1) + " is the same as line " + (j + 1));
                    }
                }
            }
        }
        System.out.println("Matching lines : " + matchingLinesByLineNumber);

        int previousGap = 0;
        Map<Integer, Integer> gapByLineNumber = new HashMap<>();
        for (Integer lineNumber : matchingLinesByLineNumber.keySet()) {
            List<Integer> matchingLines = matchingLinesByLineNumber.get(lineNumber);
            // FIXME : handle multiple matchings
            int gap = matchingLines.get(0) - lineNumber;
            gapByLineNumber.put(lineNumber, gap);

            // we already defined an expected gap => next one should be the same - 2
            if (previousGap != 0) {
                if (gap == (previousGap - 2)) {
                    previousGap = gap;
                    continue;
                }
                return 0;
            }
            previousGap = gap;
        }
        System.out.println("Gaps : " + gapByLineNumber);

        Integer firstLineWithGapOfOne = gapByLineNumber.entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (firstLineWithGapOfOne != null) {
            // Need to check that line out of the pattern can be ignored
            //  => potential reflection line is out of the pattern
            for (int line = 0; line < grid.getLinesNumber(); line++) {
                if (gapByLineNumber.get(line) != null) {
                    continue;
                }

                int targetReflectionLineNumber = firstLineWithGapOfOne * 2 - line + 1;
                if (targetReflectionLineNumber >= 0 && targetReflectionLineNumber < grid.getLinesNumber()) {
                    return 0;
                }
            }
        }

        // Compute score based on firstLineWithGapOfOne
        return firstLineWithGapOfOne != null ? (firstLineWithGapOfOne + 1) * 100L : 0;
    }
}
