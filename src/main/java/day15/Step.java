package day15;

public record Step(String value) {
    public long hash() {
        System.out.println("Hashing step " + value);

        long currentValue = 0;
        for (char c : value.toCharArray()) {
            //System.out.println("Handling character : " + c);
            currentValue += (int) c;
            //System.out.println("Adding ascii value " + (int) c);
            currentValue *= 17;
            currentValue = currentValue % 256;
        }

        System.out.println("Hashing value " + currentValue);
        return currentValue;
    }
}
