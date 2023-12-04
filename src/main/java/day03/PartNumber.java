package day03;

import utils.Direction;
import utils.Move;
import utils.Position;

public record PartNumber(Position startPosition, int value) {
    public int size() {
        return String.valueOf(this.value).length();
    }

    public boolean isAdjacentToSymbol(Symbol symbol) {
        Position symbolPosition = symbol.position();
        Position partNumberStartPosition = this.startPosition;
        if (partNumberStartPosition.adjacentPosition(Direction.NORTH_WEST).equals(symbolPosition)
                || partNumberStartPosition.adjacentPosition(Direction.WEST).equals(symbolPosition)
                || partNumberStartPosition.adjacentPosition(Direction.SOUTH_WEST).equals(symbolPosition)
                || partNumberStartPosition.adjacentPosition(Direction.NORTH).equals(symbolPosition)
                || partNumberStartPosition.adjacentPosition(Direction.SOUTH).equals(symbolPosition))
            return true;

        for (int i = 1; i < this.size() - 1; i++) {
            Position partNumberMiddlePosition = partNumberStartPosition.moveTo(Move.RIGHT);
            if (partNumberMiddlePosition.adjacentPosition(Direction.NORTH).equals(symbolPosition)
                    || partNumberMiddlePosition.adjacentPosition(Direction.SOUTH).equals(symbolPosition))
                return true;
        }
        Position partNumberEndPosition = new Position(partNumberStartPosition.x() + this.size() - 1, partNumberStartPosition.y());
        return partNumberEndPosition.adjacentPosition(Direction.NORTH).equals(symbolPosition)
                || partNumberEndPosition.adjacentPosition(Direction.SOUTH).equals(symbolPosition)
                || partNumberEndPosition.adjacentPosition(Direction.NORTH_EAST).equals(symbolPosition)
                || partNumberEndPosition.adjacentPosition(Direction.SOUTH_EAST).equals(symbolPosition)
                || partNumberEndPosition.adjacentPosition(Direction.EAST).equals(symbolPosition);
    }
}
