package solutions;

import Runner.Answer;
import Runner.Utils;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Day14 implements Day {
    private static final Logger logger = Logger.getLogger(Day14.class.getSimpleName());

    public void part1(List<String> input) {
        final char[][] matrix = Utils.toCharArray(input);

        int sum = 0;

        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[0].length; y++) {
                if (matrix[x][y] == 'O') {
                    sum += rollRock(matrix, new Point(x, y));
                }
            }
        }

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    public void part2(List<String> input) {
        final char[][] matrix = Utils.toCharArray(input);
        Config cycle = new Config(matrix);
        final Map<Config, Integer> cache = new HashMap<>();
        int cycleCount = 0;
        boolean found = false;

        while(!found) {
            cache.put(cycle, cycleCount);
            cycleCount++;
            cycle = performCycle(cycle);
            found = cache.containsKey(cycle);
        }

        final int totalCycles = 1000000000;
        final int remainingCycles = totalCycles - cycleCount;
        final int cycleLength = cycleCount - cache.get(cycle);
        final int cycleIndex = totalCycles % cycleLength;
        final int newValue = remainingCycles / cycleLength;
        int newCycleCount = cycleCount + newValue * cycleLength;

        while (newCycleCount % cycleLength != cycleIndex) {
            cycle = performCycle(cycle);
            newCycleCount++;
        }

        final int sum = getWeight(cycle.matrix);

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    private int rollRock(char[][] matrix, Point rock) {
        int stop = 0;
        final int maxLoad = matrix.length + 1;

        for (int i = rock.x; i >= 0; i--) {
            if (matrix[i][rock.y] == 'O') {
                stop++;
                continue;
            }

            if (matrix[i][rock.y] == '#') {
                stop += i + 1;
                break;
            }
        }

        return maxLoad - stop;
    }

    private Config performCycle(Config c) {
        char[][] current = c.matrix;

        for (int i = 0; i < 4; i++) {
            current = moveRocks(current);
            current = Utils.rotateCharArray(current);
        }

        return new Config(current);
    }

    private char[][] moveRocks(char[][] matrix) {
        final char[][] base = Utils.initializeCharArray('.', matrix.length, matrix[0].length);

        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[0].length; y++) {
                switch (matrix[x][y]) {
                    case '#' -> base[x][y] = '#';
                    case 'O' -> {
                        final Point newLocation = rollRockLocation(matrix, new Point(x, y));
                        base[newLocation.x][newLocation.y] = 'O';
                    }
                    default -> {}
                }
            }
        }

        return base;
    }

    private Point rollRockLocation(char[][] matrix, Point rock) {
        int stop = 0;

        for (int i = rock.x; i >= 0; i--) {
            if (matrix[i][rock.y] == 'O') {
                stop++;
                continue;
            }
            if (matrix[i][rock.y] == '#') {
                stop += i + 1;
                break;
            }
        }

        return new Point(stop - 1, rock.y);
    }

    private int getWeight(char[][] matrix) {
        final int maxWeight = matrix.length;
        int sum = 0;

        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[0].length; y++) {
                if (matrix[x][y] == 'O') {
                    sum += maxWeight - x;
                }
            }
        }

        return sum;
    }

    static class Config {
        final char[][] matrix;

        public Config(char[][] m) {
            matrix = m;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }

            if (!(object instanceof Day14.Config config)) {
                return false;
            }

            if (matrix.length != config.matrix.length || matrix[0].length != config.matrix[0].length) {
                return false;
            }

            for (int x = 0; x < matrix.length; x++) {
                for (int y = 0; y < matrix[0].length; y++) {
                    if (matrix[x][y] != config.matrix[x][y]) {
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        public int hashCode() {
            int sum = 0;
            int count = 0;
            final int multiplier = matrix.length * matrix[0].length;

            for (int x = 0; x < matrix.length; x++) {
                for (int y = 0; y < matrix[0].length; y++) {
                    if (matrix[x][y] == 'O') {
                        sum += (multiplier * count) * (x * matrix.length + y);
                        count++;
                    }
                }
            }

            return sum;
        }
    }
}
