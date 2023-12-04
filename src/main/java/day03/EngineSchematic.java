package day03;

import utils.Direction;
import utils.Move;
import utils.Position;

import java.util.List;

public record EngineSchematic(List<Symbol> symbols, List<PartNumber> partNumbers, int lineSize, int lineNumber) {
    public void displaySchema() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < lineNumber; y++) {
            for (int x = 0; x < lineSize; x++) {
                Position p = new Position(x, y);
                if (hasSymbolAt(p)) {
                    sb.append(getSymbolAt(p).value());
                }
                else if (hasPartNumberAt(p)) {
                    PartNumber partNumber = getPartNumberAt(p);
                    sb.append(partNumber.value());
                    x += partNumber.size() - 1;
                }
                else {
                    sb.append(".");
                }
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    private boolean hasPartNumberAt(Position p) {
        return getPartNumberAt(p) != null;
    }

    private PartNumber getPartNumberAt(Position p) {
        return this.partNumbers.stream()
                .filter(partNumber -> partNumber.startPosition().equals(p))
                .findFirst()
                .orElse(null);
    }

    private Symbol getSymbolAt(Position p) {
        return this.symbols.stream()
                .filter(symbol -> symbol.position().equals(p))
                .findFirst()
                .orElse(null);
    }

    private boolean hasSymbolAt(Position p) {
        return getSymbolAt(p) != null;
    }

    public long sumPartNumbers() {
        // get partNumber which are adjacent to symbols and sum them
        return this.partNumbers.stream()
                .filter(this::isAdjacentToSymbol)
                .map(PartNumber::value)
                .reduce(0, Integer::sum);
    }

    private boolean isAdjacentToSymbol(PartNumber partNumber) {
        return this.symbols.stream()
                .anyMatch(symbol -> {
                    Position symbolPosition = symbol.position();
                    Position partNumberStartPosition = partNumber.startPosition();
                    if (partNumberStartPosition.adjacentPosition(Direction.NORTH_WEST).equals(symbolPosition)
                            || partNumberStartPosition.adjacentPosition(Direction.WEST).equals(symbolPosition)
                            || partNumberStartPosition.adjacentPosition(Direction.SOUTH_WEST).equals(symbolPosition)
                            || partNumberStartPosition.adjacentPosition(Direction.NORTH).equals(symbolPosition)
                            || partNumberStartPosition.adjacentPosition(Direction.SOUTH).equals(symbolPosition))
                        return true;

                    for (int i = 1; i < partNumber.size() - 1; i++) {
                        Position partNumberMiddlePosition = partNumberStartPosition.moveTo(Move.RIGHT);
                        if (partNumberMiddlePosition.adjacentPosition(Direction.NORTH).equals(symbolPosition)
                                || partNumberMiddlePosition.adjacentPosition(Direction.SOUTH).equals(symbolPosition))
                            return true;
                    }
                    Position partNumberEndPosition = new Position(partNumberStartPosition.x() + partNumber.size() - 1, partNumberStartPosition.y());
                    return partNumberEndPosition.adjacentPosition(Direction.NORTH).equals(symbolPosition)
                            || partNumberEndPosition.adjacentPosition(Direction.SOUTH).equals(symbolPosition)
                            || partNumberEndPosition.adjacentPosition(Direction.NORTH_EAST).equals(symbolPosition)
                            || partNumberEndPosition.adjacentPosition(Direction.SOUTH_EAST).equals(symbolPosition)
                            || partNumberEndPosition.adjacentPosition(Direction.EAST).equals(symbolPosition);
                });
    }
}
