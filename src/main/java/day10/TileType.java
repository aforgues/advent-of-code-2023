package day10;

import java.util.Arrays;

public enum TileType {
    VERTICAL_PIPE("|", "│", "a vertical pipe connecting north and south"),
    HORIZONTAL_PIPE("-", "─", "a horizontal pipe connecting east and west"),
    NORTH_TO_EAST_PIPE("L", "└", "a 90-degree bend connecting north and east"),
    NORTH_TO_WEST_PIPE("J", "┘", "a 90-degree bend connecting north and west"),
    SOUTH_TO_WEST_PIPE("7", "┐", "a 90-degree bend connecting south and west"),
    SOUTH_TO_EAST_PIPE("F", "┌", "a 90-degree bend connecting south and east"),
    GROUND(".", ".", "a ground; there is no pipe in this tile"),
    STARTING_POINT("S", "S", "the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.");

    public final String symbol;
    public final String boxDrawingSymbol;
    public final String description;

    TileType(String symbol, String boxDrawingSymbol, String description) {
        this.symbol = symbol;
        this.boxDrawingSymbol = boxDrawingSymbol;
        this.description = description;
    }

    public static TileType fromSymbol(char symbol) {
        return Arrays.stream(TileType.values())
                .filter(tileType -> tileType.symbol.equals(Character.toString(symbol)))
                .findFirst()
                .orElse(null);
    }
}
