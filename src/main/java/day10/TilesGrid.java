package day10;

import utils.Move;
import utils.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TilesGrid {
    private final Map<Position, Tile> tiles;
    private int totalLines;
    private int totalColumns;
    private final List<Tile> loopTiles;

    public TilesGrid() {
        this.tiles = new HashMap<>();
        this.totalLines = 0;
        this.totalColumns = 0;
        this.loopTiles = new ArrayList<>();
    }
    public void addLineFromRawContent(String content, int lineNumber) {
        for (int x = 0; x < content.length(); x++) {
            Position position = new Position(x, lineNumber);
            this.tiles.put(position, new Tile(position, TileType.fromSymbol(content.charAt(x))));
        }
        this.totalLines++;
        this.totalColumns = content.length();
    }

    public void displayInConsole() {
        StringBuilder sb = new StringBuilder();
        for (int line = 0; line < this.totalLines; line++) {
            for (int column = 0; column < this.totalColumns; column++) {
                sb.append(getTileByPosition(new Position(column, line)).tileType().symbol);
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    private Tile getTileByPosition(Position p) {
        return this.tiles.get(p);
    }

    public long stepsAlongTheLoopToTheFarthestPoint() {
        // Find the starting point
        Tile startTile = findStartingPoint();

        // Discover the loop
        discoverTheLoopFrom(startTile);

        // Compute the farthest point from the start
        return this.loopTiles.size()/2;
    }

    private Tile findStartingPoint() {
        return this.tiles.values().stream().filter(tile -> tile.tileType() == TileType.STARTING_POINT).findFirst().orElse(null);
    }

    private void discoverTheLoopFrom(Tile startTile) {
        System.out.println("Discovering loop from " + startTile);

        this.loopTiles.add(startTile);

        // Look for adjacent Tile
        for (Move move : Move.values()) {
            Tile adjacentTile = getTileByPosition(startTile.position().moveTo(move));
            if (adjacentTile != null && isAdjacentTileCompatibleWithMove(adjacentTile, move)) {
                System.out.println("Found adjacent Tile next to start : " + adjacentTile);

                // Then follow direction until coming back to Start tile
                while (! adjacentTile.equals(startTile)) {
                    this.loopTiles.add(adjacentTile);
                    adjacentTile = getNextTile();
                    System.out.println("Found next adjacent Tile : " + adjacentTile);
                }
                break;
            }
        }
    }

    private static boolean isAdjacentTileCompatibleWithMove(Tile adjacentTile, Move move) {
        return switch (move) {
            case UP -> (adjacentTile.tileType() == TileType.VERTICAL_PIPE
                            || adjacentTile.tileType() == TileType.SOUTH_TO_WEST_PIPE
                            || adjacentTile.tileType() == TileType.SOUTH_TO_EAST_PIPE);
            case DOWN -> (adjacentTile.tileType() == TileType.VERTICAL_PIPE
                            || adjacentTile.tileType() == TileType.NORTH_TO_WEST_PIPE
                            || adjacentTile.tileType() == TileType.NORTH_TO_EAST_PIPE);
            case LEFT -> (adjacentTile.tileType() == TileType.HORIZONTAL_PIPE
                            || adjacentTile.tileType() == TileType.SOUTH_TO_EAST_PIPE
                            || adjacentTile.tileType() == TileType.NORTH_TO_EAST_PIPE);
            case RIGHT -> (adjacentTile.tileType() == TileType.HORIZONTAL_PIPE
                            || adjacentTile.tileType() == TileType.SOUTH_TO_WEST_PIPE
                            || adjacentTile.tileType() == TileType.NORTH_TO_WEST_PIPE);

        };
    }

    private Tile getNextTile() {
        Tile currentTile = this.loopTiles.getLast();
        Tile previousTile = this.loopTiles.get(this.loopTiles.size() - 2);
        return switch (currentTile.tileType()) {
            case VERTICAL_PIPE -> getTileByPosition(currentTile.position().moveTo(Move.UP)).equals(previousTile)
                    ? getTileByPosition(currentTile.position().moveTo(Move.DOWN))
                    : getTileByPosition(currentTile.position().moveTo(Move.UP));
            case HORIZONTAL_PIPE -> getTileByPosition(currentTile.position().moveTo(Move.LEFT)).equals(previousTile)
                    ? getTileByPosition(currentTile.position().moveTo(Move.RIGHT))
                    : getTileByPosition(currentTile.position().moveTo(Move.LEFT));
            case NORTH_TO_EAST_PIPE -> getTileByPosition(currentTile.position().moveTo(Move.UP)).equals(previousTile)
                    ? getTileByPosition(currentTile.position().moveTo(Move.RIGHT))
                    : getTileByPosition(currentTile.position().moveTo(Move.UP));
            case NORTH_TO_WEST_PIPE -> getTileByPosition(currentTile.position().moveTo(Move.UP)).equals(previousTile)
                    ? getTileByPosition(currentTile.position().moveTo(Move.LEFT))
                    : getTileByPosition(currentTile.position().moveTo(Move.UP));
            case SOUTH_TO_EAST_PIPE -> getTileByPosition(currentTile.position().moveTo(Move.DOWN)).equals(previousTile)
                    ? getTileByPosition(currentTile.position().moveTo(Move.RIGHT))
                    : getTileByPosition(currentTile.position().moveTo(Move.DOWN));
            case SOUTH_TO_WEST_PIPE -> getTileByPosition(currentTile.position().moveTo(Move.DOWN)).equals(previousTile)
                    ? getTileByPosition(currentTile.position().moveTo(Move.LEFT))
                    : getTileByPosition(currentTile.position().moveTo(Move.DOWN));
            default -> null;
        };
    }

    @Override
    public String toString() {
        return "TilesGrid{" +
                "tiles=" + tiles +
                ", totalLines=" + totalLines +
                ", totalColumns=" + totalColumns +
                '}';
    }
}
