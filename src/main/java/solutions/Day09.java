package solutions;

import Runner.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class Day09 implements Day {
    private static final Logger logger = Logger.getLogger(Day09.class.getSimpleName());
    public void part1(List<String> input) {
        int sum = 0;

        for (String string : input) {
            final List<Integer> sequence = Arrays.stream(string.split(" ")).map(Integer::parseInt).toList();
            sum += recursion(sequence, true);
        }

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    public void part2(List<String> input) {
        int sum = 0;

        for (String string : input) {
            final List<Integer> sequence = Arrays.stream(string.split(" ")).map(Integer::parseInt).toList();
            sum += recursion(sequence, false);
        }

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    private int recursion(List<Integer> sequence, boolean part1) {
        if (Collections.frequency(sequence, 0) == sequence.size()) {
            return 0;
        }

        final List<Integer> differences = new ArrayList<>();
        int previous = sequence.get(0);

        for (int i = 1; i < sequence.size(); i++) {
            final int current = sequence.get(i);
            differences.add(current - previous);
            previous = current;
        }

        final int nextDifference = recursion(differences, part1);

        return part1 ? nextDifference + sequence.get(sequence.size() - 1) : sequence.get(0) - nextDifference;
    }

}
