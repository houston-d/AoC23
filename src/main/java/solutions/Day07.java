package solutions;
import Runner.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Day07 implements Day {
    private static final Logger logger = Logger.getLogger(Day07.class.getSimpleName());

    public void part1(List<String> input) {
        final List<HandAndBid> orderedHands = input.stream()
                .map(this::parseString)
                .sorted(this::compareHandsPart1)
                .toList();
        int sum = 0;

        for (int i = 0; i < orderedHands.size(); i++) {
            sum += (i + 1) * orderedHands.get(i).getBid();
        }

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    public void part2(List<String> input) {
        final List<HandAndBid> orderedHands = input.stream()
                .map(this::parseString)
                .sorted(this::compareHandsPart2)
                .toList();
        int sum = 0;

        for (int i = 0; i < orderedHands.size(); i++) {
            sum += (i + 1) * orderedHands.get(i).getBid();
        }

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    public HandAndBid parseString(String inputString) {
        final String[] splitString = inputString.split(" ");
        return new HandAndBid(splitString[0], Integer.parseInt(splitString[1]));
    }

    private int compareHandsPart1(HandAndBid first, HandAndBid second) {
        final Character[] rank = {'2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A'};
        final int firstType = getType1(first.getHand());
        final int secondType = getType1(second.getHand());

        if (firstType == secondType) {
            return compareCards(first, second, rank);
        }

        return firstType > secondType ? 1 : -1;
    }

    private int compareHandsPart2(HandAndBid first, HandAndBid second) {
        final Character[] rank = {'J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A'};

        final int firstType = getType2(first.getHand());
        final int secondType = getType2(second.getHand());

        if (firstType == secondType) {
            return compareCards(first, second, rank);
        }

        return firstType > secondType ? 1 : -1;
    }

    private int compareCards(HandAndBid first, HandAndBid second, Character[] rank) {
        List<Character> charList = Arrays.asList(rank);

        for (int i = 0; i < first.getHand().length(); i ++) {
            final int firstCard = charList.indexOf(first.getHand().charAt(i));
            final int secondCard = charList.indexOf(second.getHand().charAt(i));

            if (firstCard == secondCard) {
                continue;
            }

            return firstCard > secondCard ? 1 : -1;
        }

        return 0;
    }

    private int getType1(String hand) {
        final Map<Character, Integer> cardMap = new HashMap<>();

        for (char currentCard : hand.toCharArray()) {
            cardMap.put(currentCard, cardMap.getOrDefault(currentCard, 0) + 1);
        }

        final List<Integer> counts = new java.util.ArrayList<>(cardMap.values().stream().toList());
        counts.sort(Collections.reverseOrder());

        return switch (counts.get(0)) {
            case 5 -> 6;
            case 4 -> 5;
            case 3 -> counts.get(1) == 2 ? 4 : 3;
            case 2 -> counts.get(1) == 2 ? 2 : 1;
            case 1 -> 0;
            default -> -1;
        };
    }

    private int getType2(String hand) {
        final Map<Character, Integer> cardMap = new HashMap<>();
        int jokerCount = 0;

        for (char currentCard : hand.toCharArray()) {
            if (currentCard == 'J') {
                jokerCount++;
                continue;
            }

            cardMap.put(currentCard, cardMap.getOrDefault(currentCard, 0) + 1);
        }

        if (cardMap.isEmpty()) {
            return 6;
        }

        final List<Integer> counts = new java.util.ArrayList<>(cardMap.values().stream().toList());
        counts.sort(Collections.reverseOrder());

        return switch (counts.get(0) + jokerCount) {
            case 5 -> 6;
            case 4 -> 5;
            case 3 -> counts.get(1) == 2 ? 4 : 3;
            case 2 -> counts.get(1) == 2 ? 2 : 1;
            case 1 -> 0;
            default -> -1;
        };
    }

    static class HandAndBid {
        private final String hand;
        private final int bid;

        public HandAndBid(String h, int b) {
            hand = h;
            bid = b;
        }

        public int getBid() {
            return bid;
        }

        public String getHand() {
            return hand;
        }
    }
}
