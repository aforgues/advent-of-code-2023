package day16;

import utils.Cell;
import utils.Grid;
import utils.Position;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// TODO : init with Map<Position, Cell<String> instead
public record ContraptionLayout(List<Cell<String>> cells) {
    public Optional<Cell<String>> findCellAt(Position position) {
        return this.cells.stream().filter(cell -> cell.position().equals(position)).findFirst();
    }

    public boolean isPositionInsideGrid(Position position) {
        // FIXME : build grid at init to avoid generating it each time
        Grid<String> grid = new Grid<>(this.cells.stream().collect(Collectors.groupingBy(cell -> cell.position().y())));
        return grid.isInside(position);
    }

    public void displayAsGridInConsole() {
        Grid<String> grid = new Grid<>(this.cells.stream().collect(Collectors.groupingBy(cell -> cell.position().y())));
        grid.displayInConsole();
    }
}
