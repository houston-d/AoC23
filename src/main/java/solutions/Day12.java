package solutions;

import Runner.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Day12 implements Day {
    private static final Logger logger = Logger.getLogger(Day12.class.getSimpleName());

    public void part1(List<String> input) {
        int sum = 0;

        for (String s : input) {
            final String[] split = s.split(" ");
            final String condition = split[0];
            final List<Integer> record = Arrays.stream(split[1].split(",")).map(Integer::parseInt).toList();

            sum += new Ground(condition, record).walk();
        }

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    public void part2(List<String> input) {
        long sum = 0;

        for (String s : input) {
            final String[] split = s.split(" ");
            final String condition = split[0]
                    .concat("?")
                    .concat(split[0])
                    .concat("?")
                    .concat(split[0])
                    .concat("?")
                    .concat(split[0])
                    .concat("?")
                    .concat(split[0]);

            final List<Integer> record = Arrays.stream(
                    split[1]
                    .concat(",")
                    .repeat(5)
                    .split(",")
            ).map(Integer::parseInt).toList();

            sum += new Ground(condition, record).walk();
        }

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    static class Ground {
        char[] configuration;
        List<Integer> numbers;
        int currCount;
        Map<Ground, Long> cache;

        public Ground(String config, List<Integer> n) {
            configuration = config.toCharArray();
            numbers = n;
            cache = new HashMap<>();
            currCount = 0;
        }

        public Ground(char[] config, List<Integer> n, Map<Ground, Long> c, int count) {
            configuration = config;
            numbers = n;
            cache = c;
            currCount = count;
        }

        public long walk() {
            if (cache.containsKey(this)) {
                return cache.get(this);
            }

            if (configuration.length == 0) {
                if (this.numbers.isEmpty() || (numbers.size() == 1 && numbers.get(0) == currCount)) {
                    cache.put(this, 1L);
                    return 1L;
                } else {
                    cache.put(this, 0L);
                    return 0L;
                }
            }

            if (configuration[0] == '.') {
                long total = dot();
                cache.put(this, total);

                return total;
            } else if (configuration[0] == '#') {
                long total = hash();
                cache.put(this, total);

                return total;
            } else if (configuration[0] == '?') {
                long total = dot() + hash();
                cache.put(this, total);

                return total;
            }

            throw new IllegalStateException();
        }

        private long hash() {
            if (numbers.isEmpty()) {
                cache.put(this, 0L);

                return 0L;
            }
            if (currCount >= numbers.get(0)) {
                cache.put(this, 0L);

                return 0L;
            } else {
                char[] newConfig = copyGround();
                final List<Integer> newNumbers = copyNumbers(true);

                return new Ground(newConfig, newNumbers, cache, currCount + 1).walk();
            }
        }

        private long dot() {
            if (currCount == 0 || currCount == numbers.get(0)) {
                char[] newConfig = copyGround();
                final List<Integer> newNumbers = copyNumbers(currCount == 0);

                return new Ground(newConfig, newNumbers, cache, 0).walk();
            } else {
                cache.put(this, 0L);

                return 0L;
            }
        }

        private char[] copyGround() {
            if (configuration.length <= 1) {
                return new char[0];
            }

            return Arrays.copyOfRange(configuration, 1, configuration.length);
        }

        private List<Integer> copyNumbers(boolean keepFirst) {
            if (keepFirst) {
                return new ArrayList<>(numbers);
            }

            return new ArrayList<>(numbers.subList(1, numbers.size()));
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }

            if (!(object instanceof Ground ground)) {
                return false;
            }

            if (currCount != ground.currCount) {
                return false;
            }

            if (!Arrays.equals(configuration, ground.configuration)) {
                return false;
            }

            return numbers.equals(ground.numbers);
        }

        @Override
        public int hashCode() {
            return 35 * (currCount + 35 * (numbers.size() + 35 * (configuration.length)));
        }
    }
}
