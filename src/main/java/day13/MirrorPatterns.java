package day13;

import utils.Cell;
import utils.Grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record MirrorPatterns(List<Grid<String>> grids) {
    private static final boolean DEBUG = false;

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
        if (DEBUG)
            System.out.println("Matching columns : " + matchingColumnsByColumnNumber);

        // First find which column could be the target : the one with a gap of 1
        List<Integer> candidateColumnsWithGapOfOne = matchingColumnsByColumnNumber.entrySet().stream()
                .filter(entry -> {
                    for (Integer matchColumn : entry.getValue()) {
                        if (matchColumn - entry.getKey() == 1) {
                            return true;
                        }
                    }
                    return false;
                })
                .map(Map.Entry::getKey)
                .toList();
        if (DEBUG)
            System.out.println("Potential column candidates :" + candidateColumnsWithGapOfOne);

        // Analyse adjacent columns for each column candidate
        for (Integer columnCandidate : candidateColumnsWithGapOfOne) {
            if (DEBUG)
                System.out.println("- Analysing potential column candidate : " + columnCandidate);
            int currentPrevGap = 1;
            int currentNextGap = 1;
            int next = columnCandidate + 1;
            int prev = columnCandidate - 1;
            while (true) {
                boolean hasPrevCorrectlyMatched = false;
                boolean skipNext = prev < - 1;
                // Check if we can ignore prev because we reached the border with next
                if (next >= grid.getColumnsNumber() - 1) {
                    hasPrevCorrectlyMatched = true;
                    if (DEBUG)
                        System.out.println("    Next is reaching the border " + (grid.getColumnsNumber() - 1) + " => ignore prev " + prev);
                }
                else {
                    if (prev <= -1) {
                        hasPrevCorrectlyMatched = true;
                        if (DEBUG)
                            System.out.println("    Prev is reaching the border -1 => ignore prev " + prev);
                    }
                    else {
                        List<Integer> matchPrevs = matchingColumnsByColumnNumber.get(prev);
                        if (matchPrevs == null) {
                            if (DEBUG)
                                System.out.println("    No matching column for prev " + prev + " => discarding potential column candidate " + columnCandidate);
                            break;
                        }
                        if (DEBUG)
                            System.out.println("    Analysing matching columns for prev " + prev + " : " + matchPrevs);
                        for (int matchPrev : matchPrevs) {
                            if (DEBUG)
                                System.out.println("        Matching column for prev " + prev + " is " + matchPrev + " => gap of " + (matchPrev - prev));
                            // expect next gap to be previous gap + 2, else discard
                            if ((matchPrev - prev) == (currentPrevGap + 2)) {
                                if (DEBUG)
                                    System.out.println("        Prev gap is as expected : " + (matchPrev - prev));
                                currentPrevGap = matchPrev - prev;
                                hasPrevCorrectlyMatched = true;
                                break;
                            } else {
                                if (DEBUG)
                                    System.out.println("        Prev gap is wrong : " + (matchPrev - prev) + " => discard this matching column " + matchPrev);
                            }
                        }
                    }
                }

                if (! hasPrevCorrectlyMatched) {
                    if (DEBUG)
                        System.out.println("Not any correct match found for prev " + prev);
                    break;
                }
                prev = prev - 1;


                boolean hasNextCorrectlyMatched = false;
                // Check if we can ignore next because we reached the border with previous
                if (skipNext) {
                    hasNextCorrectlyMatched = true;
                    if (DEBUG)
                        System.out.println("    Prev is reaching the border - 1 => ignore next " + next);
                }
                else {
                    List<Integer> matchNexts = matchingColumnsByColumnNumber.get(next);
                    if (matchNexts == null) {
                        if (DEBUG)
                            System.out.println("    No matching column for next " + next + " => discarding potential column candidate " + columnCandidate);
                        break;
                    }
                    if (DEBUG)
                        System.out.println("    Analysing matching columns for next " + next + " : " + matchNexts);
                    for (int matchNext : matchNexts) {
                        if (DEBUG)
                            System.out.println("        Matching column for next " + next + " is " + matchNext + " => gap of " + (matchNext - next));
                        // expect next gap to be next gap - 2, else discard
                        if ((matchNext - next) == (currentNextGap - 2)) {
                            if (DEBUG)
                                System.out.println("        Next gap is as expected : " + (matchNext - next));
                            currentNextGap = matchNext - next;
                            hasNextCorrectlyMatched = true;
                            break;
                        } else {
                            if (DEBUG)
                                System.out.println("        Next gap is wrong : " + (matchNext - next) + " => discard this matching column " + matchNext);
                        }
                    }
                    next = next + 1;
                }

                if (! hasNextCorrectlyMatched) {
                    if (DEBUG)
                        System.out.println("Not any correct match found for next " + next);
                    break;
                }

                if (prev < -1 || next >= grid.getColumnsNumber()) {
                    return (columnCandidate + 1);
                }
            }
        }

        return 0;
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
        if (DEBUG)
            System.out.println("Matching lines : " + matchingLinesByLineNumber);

        // First find which line could be the target : the one with a gap of 1
        List<Integer> candidateLinesWithGapOfOne = matchingLinesByLineNumber.entrySet().stream()
                .filter(entry -> {
                    for (Integer matchLine : entry.getValue()) {
                        if (matchLine - entry.getKey() == 1) {
                            return true;
                        }
                    }
                    return false;
                })
                .map(Map.Entry::getKey)
                .toList();
        if (DEBUG)
            System.out.println("Potential line candidates :" + candidateLinesWithGapOfOne);

        // Analyse adjacent lines for each line candidate
        for (Integer lineCandidate : candidateLinesWithGapOfOne) {
            if (DEBUG)
                System.out.println("- Analysing potential line candidate : " + lineCandidate);
            int currentPrevGap = 1;
            int currentNextGap = 1;
            int next = lineCandidate + 1;
            int prev = lineCandidate - 1;
            while (true) {
                boolean hasPrevCorrectlyMatched = false;
                boolean skipNext = prev < - 1;
                // Check if we can ignore prev because we reached the border with next
                if (next >= grid.getLinesNumber() - 1) {
                    hasPrevCorrectlyMatched = true;
                    if (DEBUG)
                        System.out.println("    Next is reaching the border " + (grid.getLinesNumber() - 1) + " => ignore prev " + prev);
                }
                else {
                    if (prev <= -1) {
                        hasPrevCorrectlyMatched = true;
                        if (DEBUG)
                            System.out.println("    Prev is reaching the border -1 => ignore prev " + prev);
                    }
                    else {
                        List<Integer> matchPrevs = matchingLinesByLineNumber.get(prev);
                        if (matchPrevs == null) {
                            if (DEBUG)
                                System.out.println("    No matching line for prev " + prev + " => discarding potential line candidate " + lineCandidate);
                            break;
                        }
                        if (DEBUG)
                            System.out.println("    Analysing matching lines for prev " + prev + " : " + matchPrevs);
                        for (int matchPrev : matchPrevs) {
                            if (DEBUG)
                                System.out.println("        Matching line for prev " + prev + " is " + matchPrev + " => gap of " + (matchPrev - prev));
                            // expect next gap to be previous gap + 2, else discard
                            if ((matchPrev - prev) == (currentPrevGap + 2)) {
                                if (DEBUG)
                                    System.out.println("        Prev gap is as expected : " + (matchPrev - prev));
                                hasPrevCorrectlyMatched = true;
                                currentPrevGap = matchPrev - prev;
                                break;
                            } else {
                                if (DEBUG)
                                    System.out.println("        Prev gap is wrong : " + (matchPrev - prev) + " => discard this matching line " + matchPrev);
                            }
                        }
                    }
                }

                if (! hasPrevCorrectlyMatched) {
                    if (DEBUG)
                        System.out.println("Not any correct match found for prev " + prev);
                    break;
                }
                prev = prev - 1;

                boolean hasNextCorrectlyMatched = false;

                // Check if we can ignore next because we reached the border with previous
                if (skipNext) {
                    hasNextCorrectlyMatched = true;
                    if (DEBUG)
                        System.out.println("    Prev is reaching the border - 1 => ignore next " + next);
                }
                else {
                    List<Integer> matchNexts = matchingLinesByLineNumber.get(next);
                    if (matchNexts == null) {
                        if (DEBUG)
                            System.out.println("    No matching line for next " + next + " => discarding potential line candidate " + lineCandidate);
                        break;
                    }
                    if (DEBUG)
                        System.out.println("    Analysing matching lines for next " + next + " : " + matchNexts);
                    for (int matchNext : matchNexts) {
                        if (DEBUG)
                            System.out.println("        Matching line for next " + next + " is " + matchNext + " => gap of " + (matchNext - next));
                        // expect next gap to be next gap - 2, else discard
                        if ((matchNext - next) == (currentNextGap - 2)) {
                            if (DEBUG)
                                System.out.println("        Next gap is as expected : " + (matchNext - next));
                            currentNextGap = matchNext - next;
                            hasNextCorrectlyMatched = true;
                            break;
                        } else {
                            if (DEBUG)
                                System.out.println("        Next gap is wrong : " + (matchNext - next) + " => discard this matching line " + matchNext);
                        }
                    }
                    next = next + 1;
                }

                if (! hasNextCorrectlyMatched) {
                    if (DEBUG)
                        System.out.println("Not any correct match found for next " + next);
                    break;
                }

                if (prev < -1 || next >= grid.getLinesNumber()) {
                    return (lineCandidate + 1) * 100L;
                }
            }
        }

        return 0;
    }
}
