package day08;

public record NodesLink(NetworkNode start, NetworkNode left, NetworkNode right) {
    public static NodesLink fromRawContent(String content) {
        String[] arr = content.split(" = ");
        String[] nextArr = arr[1].replaceAll("\\(", "").replaceAll("\\)", "").split(", ");
        return new NodesLink(new NetworkNode(arr[0]), new NetworkNode(nextArr[0]), new NetworkNode(nextArr[1]));
    }
}
