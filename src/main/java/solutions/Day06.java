package solutions;

import Runner.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Day06 implements Day {
    private static final Logger logger = Logger.getLogger(Day06.class.getSimpleName());

    public void part1(List<String> input) {
        final List<Integer> times = splitString(input.get(0));
        final List<Integer> distances = splitString(input.get(1));
        int product = 1;

        for (int i = 0; i < times.size(); i++) {
            product *= calculateDistance(times.get(i), distances.get(i));
        }

        logger.log(Answer.ANSWER, String.valueOf(product));
    }

    public void part2(List<String> input) {
        final long time = getStringValue(input.get(0));
        final long distance = getStringValue(input.get(1));
        final long answer = solveQuadratic(time, distance);

        logger.log(Answer.ANSWER, String.valueOf(answer));
    }


    private static List<Integer> splitString(String s) {
        return Arrays.stream(s.split(":")[1].split(" "))
                .filter((String string) -> !string.isEmpty())
                .map(Integer::parseInt)
                .toList();
    }

    private static Long getStringValue(String s) {
        return Long.valueOf(s.split(":")[1].replace(" ", ""));
    }

    private static Integer calculateDistance(Integer time, Integer distance) {
        final List<Integer> distances = new ArrayList<>();
        int currentTime = 0;

        while (currentTime < time) {
            final int travel = currentTime * (time - currentTime);
            distances.add(travel);
            currentTime++;
        }

        return distances.stream().filter(d -> d > distance).toList().size();
    }

    private static long solveQuadratic(long T, long D) {
        final long b = T * -1;
        final long root = (long) Math.sqrt((b * b) - (4 * D));
        final long first = (-b + root) / 2;
        final long second = (-b - root) / 2;

        return Math.abs(first - second);
    }
}
