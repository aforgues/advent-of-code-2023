package day03;

import utils.Position;

import java.util.ArrayList;
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
                .filter(this::isPartNumberAdjacentToAnySymbol)
                .map(PartNumber::value)
                .reduce(0, Integer::sum);
    }

    private boolean isPartNumberAdjacentToAnySymbol(PartNumber partNumber) {
        return this.symbols.stream()
                .anyMatch(partNumber::isAdjacentToSymbol);
    }

    public long sumGearRatios() {
        return this.getGearRatios().stream().reduce(0, Integer::sum);
    }

    private List<Integer> getGearRatios() {
        List<Integer> gearRatios = new ArrayList<>();
        this.symbols.stream().filter(symbol -> symbol.value() == '*')
                .forEach(symbol -> {
                    List<PartNumber> adjacentPartNumbers = new ArrayList<>();
                    this.partNumbers.stream().filter(partNumber -> partNumber.isAdjacentToSymbol(symbol))
                            .forEach(adjacentPartNumbers::add);
                    if (adjacentPartNumbers.size() == 2) {
                        gearRatios.add(adjacentPartNumbers.get(0).value() * adjacentPartNumbers.get(1).value());
                    }
                });
        return gearRatios;
    }
}
