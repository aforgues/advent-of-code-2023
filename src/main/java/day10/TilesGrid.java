package day10;

import utils.Move;
import utils.Position;

import java.util.*;

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
        displayInConsole(Collections.emptyList());
    }

    public void displayInConsole(List<Tile> eligibleEnclosedTiles) {
        StringBuilder sb = new StringBuilder();
        for (int line = 0; line < this.totalLines; line++) {
            for (int column = 0; column < this.totalColumns; column++) {
                Tile tile = getTileByPosition(new Position(column, line));
                if (tile.tileType() == TileType.STARTING_POINT) {
                    sb.append("S");
                }
                else if (this.loopTiles.contains(tile)) {
                    sb.append(tile.tileType().boxDrawingSymbol);
                }
                else if (eligibleEnclosedTiles.contains(tile)) {
                    sb.append("E");
                }
                else {
                    sb.append(tile.tileType().symbol);
                }
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

    public long countEnclosedTilesByTheLoop() {
        // Compute min/max x & y of the loop
        int minLoopX = this.loopTiles.stream().map(tile -> tile.position().x()).min(Integer::compare).orElse(0);
        int maxLoopX = this.loopTiles.stream().map(tile -> tile.position().x()).max(Integer::compare).orElse(this.totalColumns - 1);
        int minLoopY = this.loopTiles.stream().map(tile -> tile.position().y()).min(Integer::compare).orElse(0);
        int maxLoopY = this.loopTiles.stream().map(tile -> tile.position().y()).max(Integer::compare).orElse(this.totalLines - 1);

        // Exclude all ground tile above these limits
        List<Tile> eligibileTiles = this.tiles.values().stream()
                .filter(tile -> tile.tileType() == TileType.GROUND)
                .filter(tile -> tile.position().x() > minLoopX
                        && tile.position().x() < maxLoopX
                        && tile.position().y() > minLoopY
                        && tile.position().y() < maxLoopY)
                .toList();

        System.out.println(eligibileTiles);
        this.displayInConsole(eligibileTiles);

        return eligibileTiles.stream().filter(tile -> ! this.isTileOutOfTheLoop(tile, minLoopX, maxLoopX, minLoopY, maxLoopY)).count();
    }

    private boolean isTileOutOfTheLoop(Tile eligibleTile, int minLoopX, int maxLoopX, int minLoopY, int maxLoopY) {
        //System.out.println("Exploring Way out from the loop for eligible tile : " + eligibleTile);

        // find a ground tile outside of the min/max of the loop tiles
        Tile targetGroundTile = this.tiles.values().stream()
                .filter(tile -> tile.tileType() == TileType.GROUND)
                .filter(tile -> tile.position().x() < minLoopX || tile.position().x() > maxLoopX
                        && tile.position().y() > minLoopY || tile.position().y() < maxLoopY)
                .findFirst()
                .orElse(null);

        if (targetGroundTile == null)
            return false;

        //System.out.println("Target ground tile : " + targetGroundTile);

        // try to go to this tile without crossing a loop tile
        Map<Position, Tile> pathAlreadySeen = new HashMap<>();
        Queue<Tile> nextTilesToExplore = new ArrayDeque<>();
        nextTilesToExplore.add(eligibleTile);
        Tile current;
        while((current = nextTilesToExplore.poll()) != null) {
            if (pathAlreadySeen.containsKey(current.position()))
                continue;
            pathAlreadySeen.put(current.position(), current);

            /*if (current.equals(targetGroundTile)) {
                return true;
            }*/
            if (current.position().x() <= minLoopX || current.position().x() >= maxLoopX
            ||  current.position().y() <= minLoopY || current.position().y() >= maxLoopY)
                return true;

            for (Move move : Move.values()) {
                Tile adjacentTile = this.getTileByPosition(current.position().moveTo(move));
                if (adjacentTile != null && adjacentTile.tileType() == TileType.GROUND) {
                    nextTilesToExplore.add(adjacentTile);
                }
            }
        }


        return false;
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
