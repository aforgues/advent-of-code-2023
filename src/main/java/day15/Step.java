package day15;

public record Step(String value) {
    public long hash() {
        return hash(this.value);
    }

    private static int hash(String valueToHash) {
        //System.out.println("Hashing step " + valueToHash);

        int currentValue = 0;
        for (char c : valueToHash.toCharArray()) {
            //System.out.println("Handling character : " + c);
            currentValue += (int) c;
            //System.out.println("Adding ascii value " + (int) c);
            currentValue *= 17;
            currentValue = currentValue % 256;
        }

        //System.out.println("Hashing value " + currentValue);
        return currentValue;
    }

    public String label() {
        return this.value.split("=|-")[0];
    }

    public Operation operation() {
        int operationIndex = label().length();
        return Operation.from(this.value.substring(operationIndex, operationIndex + 1));
    }

    public Long focalLength() {
        return operation() == Operation.EQUALS_SIGN ? Long.parseLong(this.value.split(Operation.EQUALS_SIGN.character)[1]) : null;
    }

    public int labelHash() {
        return hash(label());
    }
}
