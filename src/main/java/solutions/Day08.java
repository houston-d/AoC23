package solutions;

import Runner.Answer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Day08 implements Day {
    private static final Logger logger = Logger.getLogger(Day08.class.getSimpleName());

    public void part1(List<String> input) {
        final char[] directions = input.remove(0).toCharArray();
        input.remove(0);

        final List<Node> nodes = input.stream().map(Node::new).toList();
        final Map<String, Node> nodeMap = new HashMap<>();

        for (Node n : nodes) {
            nodeMap.put(n.getFrom(), n);
        }

        String current = "AAA";
        int counter = 0;

        while (!current.equals("ZZZ")) {
            current = nodeMap.get(current).getNext(directions[counter % directions.length]);
            counter++;
        }

        logger.log(Answer.ANSWER, String.valueOf(counter));
    }

    public void part2(List<String> input) {
        final char[] directions = input.remove(0).toCharArray();
        input.remove(0);

        final List<Node> nodes = input.stream().map(Node::new).toList();
        final Map<String, Node> nodeMap = new HashMap<>();
        final List<String> startingNodes = new ArrayList<>();
        final List<Integer> stepsNeeded = new ArrayList<>();

        for (Node n : nodes) {
            final String from = n.getFrom();
            nodeMap.put(from, n);

            if (from.endsWith("A")) {
                startingNodes.add(from);
            }
        }

        for (String start : startingNodes) {
            String current = start;
            int counter = 0;

            while (!current.endsWith("Z")) {
                current = nodeMap.get(current).getNext(directions[counter % directions.length]);
                counter++;
            }

            stepsNeeded.add(counter);
        }

        final long lcm = lcmOfList(stepsNeeded);

        logger.log(Answer.ANSWER, String.valueOf(lcm));
    }

    private static long lcmOfList(List<Integer> list) {
        long lcm = list.get(0);

        for (int i = 1; i < list.size(); i++) {
            lcm = lcm(lcm, list.get(i));
        }

        return lcm;
    }

    public static long lcm(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }

        final long absNumber1 = Math.abs(number1);
        final long absNumber2 = Math.abs(number2);

        final long absHigherNumber = Math.max(absNumber1, absNumber2);
        final long absLowerNumber = Math.min(absNumber1, absNumber2);

        long lcm = absHigherNumber;

        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }

        return lcm;
    }

    static class Node {
        private final String from;
        private final String left;
        private final String right;

        public Node (String inputString) {
            final String[] split = inputString.split(" = ");
            from = split[0].trim();

            final String[] leftRight = split[1]
                    .replace("(", "")
                    .replace(")", "")
                    .split(", ");
            left = leftRight[0];
            right = leftRight[1];
        }

        public String getFrom() {
            return from;
        }

        public String getNext(char direction) {
            return switch (direction) {
                case 'L' -> left;
                case 'R' -> right;
                default -> null;
            };
        }
    }
}
