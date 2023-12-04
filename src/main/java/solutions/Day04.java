package solutions;

import Runner.Answer;

import java.util.*;
import java.util.logging.Logger;

public class Day04 implements Day {
    private static final Logger logger = Logger.getLogger(Day04.class.getSimpleName());

    public void part1(List<String> input) {
        final List<Card> allCards = parseInput(input);
        final int sum = allCards.stream().map(Card::score).reduce(0, Integer::sum);
        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    public void part2(List<String> input) {
        final List<Card> allCards = parseInput(input);
        final int total = allCards.size();
        final List<Integer> copies = listOfOnes(total);

        for (Card card : allCards) {
            List<Integer> allCopies = card.copies(total);
            final int currNumber = card.getCardNumber() - 1;

            for (Integer i : allCopies) {
                copies.set(i - 1, copies.get(i - 1) + copies.get(currNumber));
            }
        }

        final int sum = copies.stream().reduce(0, Integer::sum);
        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    private static List<Card> parseInput(List<String> input) {
        final List<Card> allCards = new ArrayList<>();

        for (String s : input) {
            final String[] cardAndGame = s.split(":");
            final String[] cardNumberArray = cardAndGame[0].split(" ");
            final int cardNumber = Integer.parseInt(cardNumberArray[cardNumberArray.length - 1]);
            final String[] numbers = cardAndGame[1].split("\\|");
            allCards.add(new Card(cardNumber, numbers[0], numbers[1]));
        }

        return allCards;
    }

    private static List<Integer> listOfOnes(int length) {
        final List<Integer> onesList = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            onesList.add(1);
        }

        return onesList;
    }

    static class Card {
        private final int cardNumber;
        private final List<Integer> winningNumbers;
        private final List<Integer> numbers;

        public Card(int card, String w, String n) {
            cardNumber = card;
            winningNumbers = parseNumbers(w);
            numbers = parseNumbers(n);
        }

        public int getCardNumber() {
            return cardNumber;
        }

        private static List<Integer> parseNumbers(String numberString) {
            final List<Integer> numbers = new ArrayList<>();
            final String[] split = numberString.trim().split(" ");

            for (String s : split) {
                if (!s.isEmpty()) {
                    numbers.add(Integer.parseInt(s));
                }
            }

            return numbers;
        }

        public int score() {
            return (int) Math.pow(2, getMatching() - 1);
        }

        public List<Integer> copies(int total) {
            final int copies = Math.min(getMatching(), total - cardNumber);

            return copiedIndices(cardNumber + 1, copies);
        }

        private static List<Integer> copiedIndices(int startValue, int length) {
            final List<Integer> result = new ArrayList<>();

            for (int i = 0; i < length; i++) {
                result.add(startValue + i);
            }

            return result;
        }

        private int getMatching() {
            final Set<Integer> set1 = new HashSet<>(winningNumbers);
            final Set<Integer> set2 = new HashSet<>(numbers);

            set1.retainAll(set2);

            return set1.size();
        }
    }
}
