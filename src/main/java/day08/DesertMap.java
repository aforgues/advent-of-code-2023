package day08;

import utils.Move;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public long ghostStepsToFinishNode() {
        List<NodesLink> currentNodesLinks = this.network.getGhostStartList();
        System.out.println("Start nodes : " + currentNodesLinks);

        System.out.println("Step 0: you are at " + currentNodesLinks.stream().map(NodesLink::start).toList());

        long steps = 1;
        for (Iterator<Move> it = this.navigationInstructions.iterator(); it.hasNext(); steps++) {
            Move move = it.next();

            List<NetworkNode> nextNodes = new ArrayList<>();
            currentNodesLinks.forEach(nodesLink -> nextNodes.add((move == Move.LEFT) ? nodesLink.left() : nodesLink.right()));

            //System.out.println("Step : " + steps + ": you choose all the " + move.name() + " paths, leading to " + nextNodes.stream().map(NetworkNode::name).toList());

            if (nextNodes.stream().allMatch(NetworkNode::isGhostFinish)) {
                break;
            }
            currentNodesLinks = new ArrayList<>();
            for (NetworkNode networkNode : nextNodes) {
                currentNodesLinks.add(network.getByNetworkNode(networkNode.name()));
            }

            if (steps % 10000000 == 0) {
                System.out.println("Step : " + steps);
            }
        }

        return steps;
    }
}
