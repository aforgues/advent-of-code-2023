package day11;

import utils.Move;
import utils.Position;

public class Data {
    private Position position;
    private final String value;

    public Position position() {
        return position;
    }

    public String value() {
        return value;
    }

    public Data(Position position, String value) {
        this.position = position;
        this.value = value;
    }

    public void moveDown() {
        this.position = this.position.moveTo(Move.DOWN);
    }

    public void moveRight() {
        this.position = this.position.moveTo(Move.RIGHT);
    }

    @Override
    public String toString() {
        return "Data{" +
                "position=" + position +
                ", value='" + value + '\'' +
                '}';
    }
}
