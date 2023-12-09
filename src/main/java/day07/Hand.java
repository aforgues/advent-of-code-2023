package day07;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record Hand(List<CardType> cardTypeList) implements Comparable<Hand> {
    public static Hand fromRawContent(String content) {
        List<CardType> cardTypeList = new ArrayList<>();
        for (char c : content.toCharArray()) {
            cardTypeList.add(CardType.from(c));
        }
        return new Hand(cardTypeList);
    }

    private Type getType() {
        var map = cardTypeList.stream().collect(Collectors.groupingBy(cardType -> cardType, Collectors.counting()));

        if (map.containsValue(5L)) {
            return Type.FIVE_OF_A_KIND;
        }
        else if (map.containsValue(4L)) {
            return Type.FOUR_OF_A_KIND;
        }
        else if (map.containsValue(3L)) {
            if (map.containsValue(2L)) {
                return Type.FULL_HOUSE;
            }
            return Type.THREE_OF_A_KIND;
        }
        else if (map.size() == 3) {
            return Type.TWO_PAIR;
        }
        else if (map.containsValue(2L)) {
            return Type.ONE_PAIR;
        }
        else {
            return Type.HIGH_CARD;
        }
    }

    @Override
    public int compareTo(Hand o) {
        Type myType = this.getType();
        Type otherType = o.getType();
        if (myType.ordinal() > otherType.ordinal()) {
            return 1;
        }

        if (myType.ordinal() < otherType.ordinal()) {
            return -1;
        }

        for(int i = 0; i < 5; i++) {
            int compare = this.cardTypeList.get(i).compareTo(o.cardTypeList.get(i));
            if (compare == 0)
                continue;
            return compare;
        }
        return 0;
    }
}
