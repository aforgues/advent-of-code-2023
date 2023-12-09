package day08;

import utils.Move;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NavigationInstructions implements Iterable<Move> {
    private final List<Move> moves;

    private Iterator<Move> moveIterator;

    public NavigationInstructions(List<Move> moves) {
        this.moves = moves;
        this.moveIterator = this.moves.iterator();
    }

    public static NavigationInstructions fromRawContent(String content) {
        List<Move> moves = new ArrayList<>();
        for (char c : content.toCharArray()) {
            moves.add(Move.fromUDLR(c));
        }
        return new NavigationInstructions(moves);
    }

    @Override
    public Iterator<Move> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Move next() {
                if (! moveIterator.hasNext()) {
                    moveIterator = moves.iterator();
                }
                return moveIterator.next();
            }
        };
    }

    @Override
    public String toString() {
        return "NavigationInstructions{" +
                "moves=" + moves +
                '}';
    }
}
