package utils;

import java.util.Objects;

public class Cell<T> implements Comparable<Cell<T>> {
    private final T data;
    private Position position;

    public T data() {return this.data;}
    public Position position() {return this.position;}

    public Cell(T data, Position position) {
        this.data = data;
        this.position = position;
    }

    public void moveTo(Move move) {
        this.position = this.position.moveTo(move);
    }

    @Override
    public int compareTo(Cell<T> o) {
        return this.position.compareTo(o.position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell<?> cell = (Cell<?>) o;
        return data.equals(cell.data) && position.equals(cell.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, position);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "data=" + data +
                ", position=" + position +
                '}';
    }
}
