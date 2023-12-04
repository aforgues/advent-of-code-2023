package day03;

import utils.Position;

public record PartNumber(Position startPosition, int value) {
    public int size() {
        return String.valueOf(this.value).length();
    }
}
