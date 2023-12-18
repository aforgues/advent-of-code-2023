package day11;

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

    public void moveDown(int lineToExpand) {
        this.position = new Position(this.position.x(), this.position.y() + lineToExpand);
    }

    public void moveRight(int colToExpand) {
        this.position = new Position(this.position.x() + colToExpand, this.position.y());
    }

    @Override
    public String toString() {
        return "Data{" +
                "position=" + position +
                ", value='" + value + '\'' +
                '}';
    }
}
