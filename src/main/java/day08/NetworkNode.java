package day08;

public record NetworkNode(String name) {
    private static final String FINISH_NAME = "ZZZ";

    public boolean isFinish() {
        return this.name.equals(FINISH_NAME);
    }

    public boolean isGhostFinish() {
        return this.name.endsWith("Z");
    }
}
