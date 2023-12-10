package day08;

import utils.MathUtils;
import utils.Move;

import java.util.*;

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

    // Is working only for test input, but not optimized for target input
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

    public long ghostStepsToFinishNodeV2() {
        Map<String, List<Long>> stepsHistoryByStartingNodeNames = new HashMap<>();

        for (NodesLink startNodesLink : this.network.getGhostStartList()) {
            NodesLink current = startNodesLink;
            String startNodeName = current.start().name();
            System.out.println("Start node : " + current.start().name());

            long steps = 1;
            for (Iterator<Move> it = this.navigationInstructions.iterator(); it.hasNext(); steps++) {
                Move move = it.next();
                NetworkNode nextNode = (move == Move.LEFT) ? current.left() : current.right();
                //System.out.println("Step : " + steps + ": you choose the " + move.name() + " path, leading to " + nextNode.name());

                if (nextNode.isGhostFinish()) {
                    List<Long> stepsHistory = stepsHistoryByStartingNodeNames.computeIfAbsent(startNodeName, k -> new ArrayList<>());
                    stepsHistory.add(steps);
                    // Store 4 finish steps to ensure that there is a regular loop in steps for finding finish nodes
                    if (stepsHistory.size() >= 4) {
                        System.out.println(stepsHistoryByStartingNodeNames);
                        break;
                    }
                }
                current = network.getByNetworkNode(nextNode.name());
            }

        }
        return lcm(stepsHistoryByStartingNodeNames);
    }

    private long lcm(Map<String, List<Long>> stepsHistoryByStartingNodeNames) {
        return stepsHistoryByStartingNodeNames.values().stream()
                .map(steps -> steps.get(0))
                .reduce(1L, MathUtils::lcm);
    }
}
