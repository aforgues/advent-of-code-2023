package utils;

import java.util.*;

public record Grid<T>(Map<Integer, List<Cell<T>>> cellsByline) {

    public void addLine(int lineNumber, List<Cell<T>> cells) {
        this.cellsByline.put(lineNumber, cells);
    }

    public int getLinesNumber() {
        return this.cellsByline.values().stream().flatMap(Collection::stream).map(Cell::position).map(Position::y).max(Integer::compare).orElse(-1) + 1;
    }

    public int getColumnsNumber() {
        return this.cellsByline.values().stream().flatMap(Collection::stream).map(Cell::position).map(Position::x).max(Integer::compare).orElse(-1) + 1;
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

    public boolean isInside(Position position) {
        return position.x() >= 0 && position.x() < this.getColumnsNumber()
                && position.y() >= 0 && position.y() < this.getLinesNumber();
    }

    public void displayInConsole() {
        StringBuilder sb = new StringBuilder();
        int maxCol = this.getColumnsNumber();
        for (int line = 0; line < this.getLinesNumber(); line++) {
            for (int col = 0; col < maxCol; col++) {
                Position pos = new Position(col, line);
                List<Cell<T>> cellsInLine = this.cellsByline.get(line);
                if (cellsInLine == null || cellsInLine.size() == 0) {
                    sb.append(".");
                    continue;
                }
                Optional<Cell<T>> cellToDisplay = cellsInLine.stream().filter(cell -> cell.position().equals(pos)).findFirst();
                if (cellToDisplay.isPresent()) {
                    sb.append(cellToDisplay.get().data());
                }
                else {
                    sb.append(".");
                }
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }
}
