package day11;

import utils.Position;
import utils.Tuple;

import java.util.*;

public class GiantImage {
    private static final String EMPTY_SPACE = ".";
    private static final String GALAXY = "#";
    private final Map<Position, Data> dataByPosition;
    private int totalLines;
    private int totalColumns;

    public GiantImage() {
        this.dataByPosition = new TreeMap<>();
    }

    public void addLine(String content, int lineNumber) {
        int column = 0;
        for (char c : content.toCharArray()) {
            Position position = new Position(column++, lineNumber);
            this.dataByPosition.put(position, new Data(position, Character.toString(c)));
        }
        this.totalColumns = content.length();
        this.totalLines++;
    }

    private List<Data> getLineAt(int lineNumber) {
        return this.dataByPosition.entrySet().stream()
                .filter(positionDataEntry -> positionDataEntry.getKey().y() == lineNumber)
                .map(Map.Entry::getValue).toList();
    }

    private List<Data> getColumnAt(int columnNumber) {
        return this.dataByPosition.entrySet().stream()
                .filter(positionDataEntry -> positionDataEntry.getKey().x() == columnNumber)
                .map(Map.Entry::getValue).toList();
    }

    public void displayInConsole() {
        StringBuilder sb = new StringBuilder();
        for (int line = 0; line < this.totalLines; line++) {
            for (int column = 0; column < this.totalColumns; column++) {
                Position pos = new Position(column, line);
                Data data = this.dataByPosition.get(pos);
                sb.append(data != null ? data.value() : EMPTY_SPACE);
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    public long sumShortestPathBetweenGalaxies() {
        return sumShortestPathBetweenGalaxies(2);
    }

    public long sumShortestPathBetweenMuchOlderGalaxies() {
        return sumShortestPathBetweenGalaxies(1_000_000);
    }

    private long sumShortestPathBetweenGalaxies(int expansionScale) {
        // First expand universe : every row or column without column is doubled
        expandUniverse(findLinesToExpand(), findColumnsToExpand(), expansionScale);

        // Identify all galaxies, then for every pair of galaxies, compute shortest path
        List<Data> galaxies = this.dataByPosition.values().stream()
                .filter(data -> data.value().equals(GALAXY))
                .toList();
        long sum = 0;
        for (Tuple<Data, Data> pair : this.computeAllGalaxyPairs(galaxies)) {
            sum += this.computeShortestPathBetween(pair.x(), pair.y());
        }

        // sum these shortest path length
        return sum;
    }

    private void expandUniverse(List<Integer> linesToExpand, List<Integer> columnsToExpand, int expansionScale) {
        int expansionSize = expansionScale - 1;

        // Replace the full grid expansion by a simple Galaxies location update
        for (int y = 0; y < linesToExpand.size(); y++) {
            int lineToExpand = linesToExpand.get(y) + y * expansionSize;
            //System.out.println("Expanding line #" + lineToExpand);

            // find all galaxies under this line
            List<Data> galaxiesToShiftDown = this.dataByPosition.values().stream()
                    .filter(data -> data.value().equals(GALAXY) && data.position().y() > lineToExpand)
                    .toList();
            // Shift them down
            galaxiesToShiftDown.forEach(data -> {
                        this.dataByPosition.remove(data.position());
                        data.moveDown(expansionSize);
                        this.dataByPosition.put(data.position(), data);
                    });

            this.totalLines += expansionSize;
            //this.displayInConsole();
        }

        //this.displayInConsole();

        for (int x = 0; x < columnsToExpand.size(); x++) {
            int columnToExpand = columnsToExpand.get(x) + x * expansionSize;
            //System.out.println("Expanding column #"+ columnToExpand);

            // find all galaxies above this column
            List<Data> galaxiesToShiftRight = this.dataByPosition.values().stream()
                    .filter(data -> data.value().equals(GALAXY) && data.position().x() > columnToExpand)
                    .toList();
            // Shift them right
            galaxiesToShiftRight.forEach(data -> {
                        this.dataByPosition.remove(data.position());
                        data.moveRight(expansionSize);
                        this.dataByPosition.put(data.position(), data);
                    });

            this.totalColumns += expansionSize;
            //this.displayInConsole();
        }

        //System.out.println("Expanded universe :");
        //this.displayInConsole();
    }

    private List<Integer> findColumnsToExpand() {
        List<Integer> columnsToExpand = new ArrayList<>();
        for (int col = 0; col < this.totalColumns; col++) {
            List<Data> datas = this.getColumnAt(col);
            if (datas.stream().map(Data::value).allMatch(value -> value.equals(EMPTY_SPACE))) {
                columnsToExpand.add(col);
            }
        }
        return columnsToExpand;
    }

    private List<Integer> findLinesToExpand() {
        List<Integer> linesToExpand = new ArrayList<>();
        for (int line = 0; line < this.totalLines; line++) {
            List<Data> datas = this.getLineAt(line);
            if (datas.stream().map(Data::value).allMatch(value -> value.equals(EMPTY_SPACE))) {
                linesToExpand.add(line);
            }
        }
        return linesToExpand;
    }

    private List<Tuple<Data, Data>> computeAllGalaxyPairs(List<Data> galaxies) {
        List<Tuple<Data, Data>> pairs = new ArrayList<>();

        for (int i = 0; i < galaxies.size(); i++) {
            for (int j = 0; j < galaxies.size(); j++) {
                if (i > j) {
                    pairs.add(new Tuple<>(galaxies.get(i), galaxies.get(j)));
                }
            }
        }
        System.out.println(pairs.size() + " pairs found in " + galaxies.size() + " galaxies : " + pairs);
        return pairs;
    }

    private long computeShortestPathBetween(Data firstGalaxy, Data secondGalaxy) {
        int nbStep = Math.abs(secondGalaxy.position().x() - firstGalaxy.position().x())
                + Math.abs(secondGalaxy.position().y() - firstGalaxy.position().y());
        System.out.println("Shortest path size between " + firstGalaxy.position() + " and " + secondGalaxy.position() + " is : " + nbStep);
        return nbStep;
    }

    @Override
    public String toString() {
        return "GiantImage{" +
                "dataByPosition=" + dataByPosition +
                ", totalLines=" + totalLines +
                ", totalColumns=" + totalColumns +
                '}';
    }
}
