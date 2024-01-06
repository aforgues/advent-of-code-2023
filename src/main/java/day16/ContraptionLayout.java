package day16;

import utils.Cell;
import utils.Grid;
import utils.Position;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public record ContraptionLayout(Grid<String> grid) {
    public static ContraptionLayout fromCells(List<Cell<String>> cells) {
        Grid<String> grid = new Grid<>(cells.stream().collect(Collectors.groupingBy(cell -> cell.position().y())));
        return new ContraptionLayout(grid);
    }

    public Optional<Cell<String>> findCellAt(Position position) {
        List<Cell<String>> cellsByLine = this.grid.cellsByline().get(position.y());
        return cellsByLine == null ? Optional.empty() : cellsByLine.stream().filter(cell -> cell.position().x() == position.x()).findFirst();
    }

    public boolean isPositionInsideGrid(Position position) {
        return this.grid.isInside(position);
    }

    public void displayAsGridInConsole() {
        this.grid.displayInConsole();
    }
}
