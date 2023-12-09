package day07;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Hand(List<CardType> cardTypeList, boolean useNewJokerRule) implements Comparable<Hand> {

    public static Hand fromRawContent(String content, boolean useNewJokerRule) {
        List<CardType> cardTypeList = new ArrayList<>();
        for (char c : content.toCharArray()) {
            cardTypeList.add(CardType.from(c));
        }
        return new Hand(cardTypeList, useNewJokerRule);
    }

    public Type getType() {
        var map = cardTypeList.stream().collect(Collectors.groupingBy(cardType -> cardType, Collectors.counting()));

        boolean optimizable = false;
        long jokerCount = 0;
        if (this.useNewJokerRule && map.containsKey(CardType.J)) {
            optimizable = true;
            jokerCount = map.entrySet().stream().filter(cardTypeLongEntry -> cardTypeLongEntry.getKey() == CardType.J)
                    .map(Map.Entry::getValue).findFirst().orElse(0L);
        }

        if (map.containsValue(5L)) {
            return Type.FIVE_OF_A_KIND;
        }
        else if (map.containsValue(4L)) {
            if (optimizable) {
                return Type.FIVE_OF_A_KIND;
            }
            return Type.FOUR_OF_A_KIND;
        }
        else if (map.containsValue(3L)) {
            if (map.containsValue(2L)) {
                if (optimizable) {
                    return Type.FIVE_OF_A_KIND;
                }
                return Type.FULL_HOUSE;
            }
            if (optimizable) {
                return Type.FOUR_OF_A_KIND;
            }
            return Type.THREE_OF_A_KIND;
        }
        else if (map.size() == 3) {
            if (optimizable) {
                if (jokerCount == 1) {
                    return Type.FULL_HOUSE;
                }
                return Type.FOUR_OF_A_KIND;
            }
            return Type.TWO_PAIR;
        }
        else if (map.containsValue(2L)) {
            if (optimizable) {
                return Type.THREE_OF_A_KIND;
            }
            return Type.ONE_PAIR;
        }
        else {
            if (optimizable) {
                return Type.ONE_PAIR;
            }
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

        for (int i = 0; i < 5; i++) {
            int compare = this.cardTypeList.get(i).compareTo(o.cardTypeList.get(i));
            if (this.useNewJokerRule) {
                CardType myCardType = this.cardTypeList.get(i);
                CardType otherCardType = o.cardTypeList.get(i);
                int myOrdinal = myCardType == CardType.J ? -1 : myCardType.ordinal();
                int otherOrdinal = otherCardType == CardType.J ? -1 : otherCardType.ordinal();
                compare = Integer.compare(myOrdinal, otherOrdinal);
            }
            if (compare == 0)
                continue;
            return compare;
        }
        return 0;
    }
}
