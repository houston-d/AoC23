package solutions;

import Runner.Answer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.*;

public class Day11 implements Day {
    private static final Logger logger = Logger.getLogger(Day11.class.getSimpleName());

    public void part1(List<String> input) {
        final Character[][] galaxyMap = createMap(input);
        final List<Integer> emptyColumns = emptyColumns(galaxyMap);
        final List<Integer> emptyRows = emptyRows(input);
        final List<Point> galaxyLocations = galaxies(galaxyMap);

        long multiplier = 1;
        long sum = calculateDistances(galaxyLocations, multiplier, emptyRows, emptyColumns);

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    public void part2(List<String> input) {
        final Character[][] galaxyMap = createMap(input);
        final List<Integer> emptyColumns = emptyColumns(galaxyMap);
        final List<Integer> emptyRows = emptyRows(input);
        final List<Point> galaxyLocations = galaxies(galaxyMap);

        long multiplier = 999999;
        long sum = calculateDistances(galaxyLocations, multiplier, emptyRows, emptyColumns);

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    private Character[][] createMap(List<String> input) {
        final List<Character[]> map = new ArrayList<>();

        for (String s : input) {
            Character[] charArray = new Character[s.length()];

            for (int i = 0; i < s.length(); i++) {
                charArray[i] = s.charAt(i);
            }

            map.add(charArray);
        }

        return map.toArray(new Character[0][]);
    }

    private List<Integer> emptyColumns(Character[][] galaxyMap) {
        final List<Integer> indicesToExpand = new ArrayList<>();

        for (int i = 0; i < galaxyMap[0].length; i++) {
            boolean hasGalaxy = false;

            for (Character[] s : galaxyMap) {
                if (s[i] == '#') {
                    hasGalaxy = true;
                    break;
                }
            }
            
            if (!hasGalaxy) {
                indicesToExpand.add(i);
            }
        }

        return indicesToExpand;
    }

    private List<Integer> emptyRows(List<String> galaxyMap) {
        final List<Integer> indicesToExpand = new ArrayList<>();

        for (int i = 0; i < galaxyMap.size(); i++) {
            if (!galaxyMap.get(i).contains("#")) {
                indicesToExpand.add(i);
            }
        }

        return indicesToExpand;
    }

    private List<Point> galaxies(Character[][] map) {
        final List<Point> galaxies = new ArrayList<>();

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == '#') {
                    galaxies.add(new Point(x, y));
                }
            }
        }

        return galaxies;
    }

    private static long calculateDistances(List<Point> galaxyLocations, long multiplier, List<Integer> emptyRows, List<Integer> emptyColumns) {
        long sum = 0;

        for (int i = 0; i < galaxyLocations.size() - 1; i++) {
            for (int j = i + 1; j < galaxyLocations.size(); j++) {
                final Point pointA = galaxyLocations.get(i);
                final Point pointB = galaxyLocations.get(j);

                final long xChange = abs(pointA.x - pointB.x) + (multiplier * countValuesInRange(emptyRows, pointA.x, pointB.x));
                final long yChange = abs(pointA.y - pointB.y) + (multiplier * countValuesInRange(emptyColumns, pointA.y, pointB.y));

                sum += xChange + yChange;
            }
        }

        return sum;
    }


    private static long countValuesInRange(List<Integer> list, int first, int second) {
        final int lowerBound = min(first, second);
        final int upperBound = max(first, second);
        int count = 0;

        for (int value : list) {
            if (value >= lowerBound && value <= upperBound) {
                count++;
            }
        }

        return count;
    }
}
