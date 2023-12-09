package day08;

import java.util.List;

public record Network(List<NodesLink> nodesLinks) {
    private static final String START_NAME = "AAA";

    public NodesLink getStart() {
        return this.getByNetworkNode(START_NAME);
    }

    public NodesLink getByNetworkNode(String nodeName) {
        return this.nodesLinks.stream().filter(nodesLink -> nodesLink.start().name().equals(nodeName)).findFirst().orElse(null);
    }
}
