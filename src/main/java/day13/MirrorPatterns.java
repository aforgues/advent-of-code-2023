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

        long vscore = computeReflectionScore(grid, GridItemType.VERTICAL);
        System.out.println("Vertical match score : " + vscore);

        long hscore = computeReflectionScore(grid, GridItemType.HORIZONTAL);
        System.out.println("Horizontal match score : " + hscore);
        return vscore + hscore;
    }

    private enum GridItemType {
        VERTICAL("Column"), HORIZONTAL("Line");

        public final String itemName;

        GridItemType(String itemName) {
            this.itemName = itemName;
        }
    }

    private static long computeReflectionScore(Grid<String> grid, GridItemType gridItemType) {
        // identify gap between matching items (lines or columns)
        Map<Integer, List<Integer>> matchingItemsByItemNumber = new HashMap<>();

        boolean isVerticalOrElseHorizontal = gridItemType == GridItemType.VERTICAL;
        int itemNumber = isVerticalOrElseHorizontal ? grid.getColumnsNumber() : grid.getLinesNumber();
        for (int i = 0; i < itemNumber; i++) {
            List<Cell<String>> itemCells = isVerticalOrElseHorizontal ? grid.getColumn(i) : grid.getLine(i);
            for (int j = 0; j < itemNumber; j++) {
                if (i != j) {
                    if (isVerticalOrElseHorizontal ? grid.hasSameColumnDataAt(j, itemCells) : grid.hasSameLineDataAt(j, itemCells)) {
                        List<Integer> matchingItems = matchingItemsByItemNumber.computeIfAbsent(i, k -> new ArrayList<>());
                        matchingItems.add(j);
                        //System.out.println(gridItemType.itemName " #" + (i+1) + " is the same as #" + (j + 1));
                    }
                }
            }
        }
        if (DEBUG)
            System.out.println("Matching " + gridItemType.itemName + " : " + matchingItemsByItemNumber);

        // First find which item could be the target : the one with a gap of 1
        List<Integer> candidateItemsWithGapOfOne = matchingItemsByItemNumber.entrySet().stream()
                .filter(entry -> {
                    for (Integer matchItem : entry.getValue()) {
                        if (matchItem - entry.getKey() == 1) {
                            return true;
                        }
                    }
                    return false;
                })
                .map(Map.Entry::getKey)
                .toList();
        if (DEBUG)
            System.out.println("Potential " + gridItemType.itemName + " candidates :" + candidateItemsWithGapOfOne);

        // Analyse adjacent items for each item candidate
        for (Integer itemCandidate : candidateItemsWithGapOfOne) {
            if (DEBUG)
                System.out.println("- Analysing potential " + gridItemType.itemName + " candidate : " + itemCandidate);
            int currentPrevGap = 1;
            int currentNextGap = 1;
            int next = itemCandidate + 1;
            int prev = itemCandidate - 1;
            while (true) {
                boolean hasPrevCorrectlyMatched = false;
                boolean skipNext = prev < - 1;
                // Check if we can ignore prev because we reached the border with next
                if (next >= itemNumber - 1) {
                    hasPrevCorrectlyMatched = true;
                    if (DEBUG)
                        System.out.println("    Next is reaching the border " + (itemNumber - 1) + " => ignore prev " + prev);
                }
                else {
                    if (prev <= -1) {
                        hasPrevCorrectlyMatched = true;
                        if (DEBUG)
                            System.out.println("    Prev is reaching the border -1 => ignore prev " + prev);
                    }
                    else {
                        List<Integer> matchPrevs = matchingItemsByItemNumber.get(prev);
                        if (matchPrevs == null) {
                            if (DEBUG)
                                System.out.println("    No matching " + gridItemType.itemName + " for prev " + prev + " => discarding potential " + gridItemType.itemName + " candidate " + itemCandidate);
                            break;
                        }
                        if (DEBUG)
                            System.out.println("    Analysing matching " + gridItemType.itemName + "s for prev " + prev + " : " + matchPrevs);
                        for (int matchPrev : matchPrevs) {
                            if (DEBUG)
                                System.out.println("        Matching " + gridItemType.itemName + " for prev " + prev + " is " + matchPrev + " => gap of " + (matchPrev - prev));
                            // expect next gap to be previous gap + 2, else discard
                            if ((matchPrev - prev) == (currentPrevGap + 2)) {
                                if (DEBUG)
                                    System.out.println("        Prev gap is as expected : " + (matchPrev - prev));
                                currentPrevGap = matchPrev - prev;
                                hasPrevCorrectlyMatched = true;
                                break;
                            } else {
                                if (DEBUG)
                                    System.out.println("        Prev gap is wrong : " + (matchPrev - prev) + " => discard this matching " + gridItemType.itemName + " " + matchPrev);
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
                    List<Integer> matchNexts = matchingItemsByItemNumber.get(next);
                    if (matchNexts == null) {
                        if (DEBUG)
                            System.out.println("    No matching " + gridItemType.itemName + " for next " + next + " => discarding potential " + gridItemType.itemName + " candidate " + itemCandidate);
                        break;
                    }
                    if (DEBUG)
                        System.out.println("    Analysing matching " + gridItemType.itemName + "s for next " + next + " : " + matchNexts);
                    for (int matchNext : matchNexts) {
                        if (DEBUG)
                            System.out.println("        Matching " + gridItemType.itemName + " for next " + next + " is " + matchNext + " => gap of " + (matchNext - next));
                        // expect next gap to be next gap - 2, else discard
                        if ((matchNext - next) == (currentNextGap - 2)) {
                            if (DEBUG)
                                System.out.println("        Next gap is as expected : " + (matchNext - next));
                            currentNextGap = matchNext - next;
                            hasNextCorrectlyMatched = true;
                            break;
                        } else {
                            if (DEBUG)
                                System.out.println("        Next gap is wrong : " + (matchNext - next) + " => discard this matching " + gridItemType.itemName + " " + matchNext);
                        }
                    }
                    next = next + 1;
                }

                if (! hasNextCorrectlyMatched) {
                    if (DEBUG)
                        System.out.println("Not any correct match found for next " + next);
                    break;
                }

                if (prev < -1 || next >= itemNumber) {
                    return (itemCandidate + 1) * (isVerticalOrElseHorizontal ? 1L : 100L);
                }
            }
        }

        return 0;
    }
}
