package solutions;

import Runner.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Day15 implements Day {
    private static final Logger logger = Logger.getLogger(Day15.class.getSimpleName());

    public void part1(List<String> input) {
        final List<char[]> sequence = parseInput(input);
        int sum = 0;
        for (char[] step : sequence) {
            sum += hash(step);
        }

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    public void part2(List<String> input) {
        final List<char[]> sequence = parseInput(input);
        final List<Box> allBoxes = new ArrayList<>();

        for (int i = 0; i < 256; i++) {
            allBoxes.add(new Box(i));
        }

        for (char[] step : sequence) {
            final char operator = isCharInArray(step, '=') ? '=' : '-';
            final String[] split = splitCharArray(step, operator);
            final String identifier = split[0];
            final String lense = split.length > 1 ? split[1] : "";
            final int hash = hash(identifier.toCharArray());

            if (operator == '=') {
                allBoxes.get(hash).addLense(identifier, lense);
            } else {
                allBoxes.get(hash).removeLense(identifier);
            }
        }

        int sum = 0;

        for (Box b : allBoxes) {
            sum += b.getFocalPower();
        }

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    private static List<char[]> parseInput(List<String> input) {
        final List<String[]> a = input.stream().map((String s) -> s.split(",")).toList();
        final List<char[]> c = new ArrayList<>();

        for (String[] b : a) {
            for (String d : b) {
                c.add(d.toCharArray());
            }
        }

        return c;
    }

    private static int hash(char[] input) {
        int hash = 0;

        for (char c : input) {
            hash += c;
            hash *= 17;
            hash %= 256;
        }

        return hash;
    }

    private static boolean isCharInArray(char[] array, char target) {
        for (char element : array) {
            if (element == target) {
                return true;
            }
        }
        return false;
    }

    static class Box {
        final int number;
        final List<String> identifiers = new ArrayList<>();
        final List<String> lenses = new ArrayList<>();

        public Box(int n) {
            number = n;
        }

        public void addLense(String identifier, String lense) {
            final int idx = identifiers.indexOf(identifier);

            if (idx == -1) {
                identifiers.add(identifier);
                lenses.add(lense);
                return;
            }

            lenses.set(idx, lense);
        }

        public void removeLense(String identifier) {
            final int idx = identifiers.indexOf(identifier);

            if (idx != -1) {
                identifiers.remove(idx);
                lenses.remove(idx);
            }
        }

        public int getFocalPower() {
            final int mult = number + 1;
            int sum = 0;

            for (int i = 0; i < lenses.size(); i++) {
                sum += mult * (i + 1) * Integer.parseInt(lenses.get(i));
            }

            return sum;
        }
    }

    private static String[] splitCharArray(char[] array, char delimiter) {
        StringBuilder currentPart = new StringBuilder();
        final List<String> parts = new ArrayList<>();

        for (char c : array) {
            if (c == delimiter) {
                parts.add(currentPart.toString());
                currentPart = new StringBuilder();
            } else {
                currentPart.append(c);
            }
        }

        if (currentPart.length() > 0) {
            parts.add(currentPart.toString());
        }

        return parts.toArray(new String[0]);
    }
}
