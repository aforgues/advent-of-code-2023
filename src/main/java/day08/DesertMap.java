package day08;

import utils.Move;

import java.util.Iterator;

public record DesertMap(NavigationInstructions navigationInstructions, Network network) {
    public long stepsToFinishNode() {
        NodesLink current = this.network.getStart();

        long steps = 1;
        for (Iterator<Move> it = this.navigationInstructions.iterator(); it.hasNext(); steps++) {
            Move move = it.next();
            NetworkNode nextNode = (move == Move.LEFT) ? current.left() : current.right();
            if (nextNode.isFinish()) {
                break;
            }
            current = network.getByNetworkNode(nextNode.name());
        }

        return steps;
    }
}
