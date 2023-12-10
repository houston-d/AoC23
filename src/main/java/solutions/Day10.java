package solutions;

import Runner.Answer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Day10 implements Day {
    private static final Logger logger = Logger.getLogger(Day10.class.getSimpleName());

    private static final List<PipePart> loopParts = new ArrayList<>();

    private static final Point[] threeBlock = {
            new Point(-1, -1),
            new Point(0, -1),
            new Point(1, -1),
            new Point(-1, 0),
            new Point(0, 0),
            new Point(1, 0),
            new Point(-1, 1),
            new Point(0, 1),
            new Point(1, 1),
    };

    public void part1(List<String> input) {
        final Character[][] pipeMap = parseInput(input);
        final Point start = getStart(pipeMap);
        final PipePart startPart = getStartChar(pipeMap, start);
        final int count = traversePipe(pipeMap, startPart) / 2;

        logger.log(Answer.ANSWER, String.valueOf(count));
    }

    public void part2(List<String> input) {
        final Character[][] pipeMap = parseInput(input);
        final Point start = getStart(pipeMap);
        final PipePart startPart = getStartChar(pipeMap, start);

        traversePipe(pipeMap, startPart);

        final Character[][] simpleMap = simpleMap(pipeMap.length, pipeMap[0].length);
        final List<Point> notSeen = countGround(simpleMap);
        final int count = getEnclosed(pipeMap, notSeen);

        logger.log(Answer.ANSWER, String.valueOf(count));
    }

    private Character[][] parseInput(List<String> input) {
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

    private Point getStart(Character[][] pipeMap) {
        final Point start = new Point();

        for (int x = 0; x < pipeMap.length; x++) {
            for (int y = 0; y < pipeMap[0].length; y++) {
                if (pipeMap[x][y] == 'S') {
                    start.x = x;
                    start.y = y;
                }
            }
        }

        return start;
    }

    private PipePart getStartChar(Character[][] map, Point start) {
        boolean up;
        boolean right;
        boolean down;
        boolean left;

        try {
            up = charIs(map[start.x - 1][start.y], '|','7','F');
        } catch (ArrayIndexOutOfBoundsException e) {
            up = false;
        }

        try {
            right = charIs(map[start.x][start.y + 1], '-','7','J');
        } catch (ArrayIndexOutOfBoundsException e) {
            right = false;
        }

        try {
            down = charIs(map[start.x + 1][start.y], '|','L','J');
        } catch (ArrayIndexOutOfBoundsException e) {
            down = false;
        }

        try {
            left = charIs(map[start.x][start.y - 1],'-','L','F');
        } catch (ArrayIndexOutOfBoundsException e) {
            left = false;
        }

        if (up && down) {
            return new PipePart(start, 0,'|', 0);
        }

        if (up && right) {
            return new PipePart(start, 0,'L', 0);
        }

        if (up && left) {
            return new PipePart(start, 0,'J', 0);
        }

        if (down && right) {
            return new PipePart(start, 0,'F', 2);
        }

        if (down && left){
            return new PipePart(start, 0,'7', 2);
        }

        if (left && right){
            return new PipePart(start, 0,'-', 1);
        }

        System.exit(1);
        return new PipePart(new Point(), 0, '.', 0);
    }

    private boolean charIs(char toCheck, char a, char b, char c) {
        return toCheck == a || toCheck == b || toCheck == c;
    }

    private int traversePipe(Character[][] pipeMap, PipePart start) {
        int count = 0;
        final int[] nextRow = {0,0,1,-1};
        final int[] nextCol = {1,-1,0,0};
        PipePart current = new PipePart(new Point(start.getRow(), start.getColumn()), start.length, start.part, start.direction);

        while (current.getRow() != start.getRow() || current.getColumn() != start.getColumn() || count == 0) {
            loopParts.add(current);
            count++;

            final int nextD = getNextPart(current);
            final int nextR = current.getRow() + nextRow[nextD];
            final int nextC = current.getColumn() + nextCol[nextD];
            final Point nextPoint = new Point(nextR, nextC);

            current = new PipePart(nextPoint, count, pipeMap[nextR][nextC], nextD);
        }

        return count;
    }

    private int getNextPart(PipePart part) {
        return switch (part.getPart()) {
            case 'L' -> part.getDirection() == 2 ? 0 : 3;
            case '7' -> part.getDirection() == 0 ? 2 : 1;
            case 'F' -> part.getDirection() == 3 ? 0 : 2;
            case 'J' -> part.getDirection() == 0 ? 3 : 1;
            default -> part.getDirection();
        };
    }

    private Character[][] simpleMap(int maxRow, int maxCol) {
        final Character[][] simpleMap = new Character[3 * maxRow][3 * maxCol];

        for (int i = 0; i < 3 * maxRow; i++) {
            Arrays.fill(simpleMap[i], '.');
        }

        for (PipePart part : loopParts) {
            int centreRow = 3 * part.getRow() + 1;
            int centreCol = 3 * part.getColumn() + 1;
            simpleMap[centreRow][centreCol] = 'X';

            switch (part.part) {
                case 'L' -> {
                    simpleMap[centreRow - 1][centreCol] = 'X';
                    simpleMap[centreRow][centreCol + 1] = 'X';
                }
                case '7' -> {
                    simpleMap[centreRow][centreCol - 1] = 'X';
                    simpleMap[centreRow + 1][centreCol] = 'X';
                }
                case 'F' -> {
                    simpleMap[centreRow][centreCol + 1] = 'X';
                    simpleMap[centreRow + 1][centreCol] = 'X';
                }
                case 'J' -> {
                    simpleMap[centreRow -1][centreCol] = 'X';
                    simpleMap[centreRow][centreCol - 1] = 'X';
                }
                case '|' -> {
                    simpleMap[centreRow - 1][centreCol] = 'X';
                    simpleMap[centreRow + 1][centreCol] = 'X';
                }
                case '-' -> {
                    simpleMap[centreRow][centreCol - 1] = 'X';
                    simpleMap[centreRow][centreCol + 1] = 'X';
                }
            }
        }

        return simpleMap;
    }

    private List<Point> countGround(Character[][] simpleMap) {
        final List<Point> queue = new ArrayList<>();
        final List<Point> seen = new ArrayList<>();

        queue.add(new Point(0, 0));

        while (!queue.isEmpty()) {
            final Point current = queue.remove(0);

            if (current.x < 0 || current.y < 0 || current.x >= simpleMap.length || current.y >= simpleMap[0].length) {
                continue;
            }

            if (seen.contains(current)) {
                continue;
            }

            seen.add(current);

            if (simpleMap[current.x][current.y] == 'X') {
                continue;
            }

            for (Point offset : threeBlock) {
                queue.add(new Point(current.x + offset.x, current.y + offset.y));
            }
        }

        return seen;
    }

    private int getEnclosed(Character[][] pipeMap, List<Point> notSeen) {
        int count = 0;

        for (int x = 0; x < pipeMap.length; x++) {
            for (int y = 0; y < pipeMap[0].length; y++) {
                boolean notIn = false;

                for (Point offset : threeBlock) {
                    if (notSeen.contains(new Point(3 * x + offset.x, 3 * y + offset.y))) {
                        notIn = true;
                    }
                }

                if (!notIn) {
                    count++;
                }
            }
        }

        return count;
    }

    static class PipePart {
        private final Point point;
        private final int length;
        private final char part;
        private final int direction;

        public PipePart (Point p, int l, char p_, int d) {
            point = p;
            length = l;
            part = p_;
            direction = d;
        }

        public int getRow() {
            return point.x;
        }

        public int getColumn() {
            return point.y;
        }

        public char getPart() {
            return part;
        }

        public int getDirection() {
            return direction;
        }
    }
}
