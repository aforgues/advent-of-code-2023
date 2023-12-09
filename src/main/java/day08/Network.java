package day08;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Network(Map<String, NodesLink> nodesLinksByNodeStartName) {
    private static final String START_NAME = "AAA";

    public static Network fromNodesLinkList(List<NodesLink> nodesLinkList) {
        return new Network(nodesLinkList.stream().collect(Collectors.toMap(nodesLink -> nodesLink.start().name(), nodesLink -> nodesLink)));
    }

    public NodesLink getStart() {
        return this.getByNetworkNode(START_NAME);
    }

    public NodesLink getByNetworkNode(String nodeStartName) {
        return this.nodesLinksByNodeStartName.get(nodeStartName);
    }

    public List<NodesLink> getGhostStartList() {
        return this.nodesLinksByNodeStartName.keySet().stream()
                .filter(name -> name.endsWith("A"))
                .map(this::getByNetworkNode)
                .toList();
    }
}
