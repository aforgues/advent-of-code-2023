package utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public record Grid<T>(Map<Integer, List<Cell<T>>> cellsByline) {

    public void addLine(int lineNumber, List<Cell<T>> cells) {
        this.cellsByline.put(lineNumber, cells);
    }

    public int getLinesNumber() {
        return this.cellsByline.size();
    }

    public int getColumnsNumber() {
        return this.cellsByline.getOrDefault(1, Collections.emptyList()).size();
    }

    public List<Cell<T>> getLine(int lineNumber) {
        return this.cellsByline.get(lineNumber);
    }

    public List<Cell<T>> getColumn(int columnNumber) {
        return this.cellsByline.values().stream()
                .flatMap(Collection::stream)
                .filter(cell -> cell.position().x() == columnNumber)
                .toList();
    }

    public boolean hasSameColumnDataAt(int columnNumber, List<Cell<T>> columnCells) {
        for (int line = 0; line < this.getLinesNumber(); line++) {
            if (!this.cellsByline.get(line).get(columnNumber).data().equals(columnCells.get(line).data())) {
                return false;
            }
        }
        return true;
    }

    public boolean hasSameLineDataAt(int lineNumber, List<Cell<T>> lineCells) {
        for (int column = 0; column < this.getColumnsNumber(); column++) {
            if (!this.cellsByline.get(lineNumber).get(column).data().equals(lineCells.get(column).data())) {
                return false;
            }
        }
        return true;
    }

    public void displayInConsole() {
        StringBuilder sb = new StringBuilder();
        int maxCol = this.getColumnsNumber();
        for (int line = 0; line < this.getLinesNumber(); line++) {
            for (int col = 0; col < maxCol; col++) {
                Position pos = new Position(col, line);
                sb.append(this.cellsByline.get(line).get(col).data());
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }
}
