package solutions;

import Runner.Answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day03 implements Day {
    private static final Logger logger = Logger.getLogger(Day03.class.getSimpleName());

    public void part1(List<String> input) {
        final char[][] charMatrix = convertToMatrix(input);
        final List<String> allNumbers = getNumbers(charMatrix);
        final int sum = allNumbers.stream().map(Integer::parseInt).reduce(0, Integer::sum);
        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    public void part2(List<String> input) {
        final char[][] charMatrix = convertToMatrix(input);
        final Map<String, Gear> allGears = getGearLocations(charMatrix);
        final Map<String, Gear> updatedGears = updateGears(charMatrix, allGears);
        final List<Gear> filteredGears = filterGears(updatedGears);
        final int sum = filteredGears.stream().map(Gear::getRatio).reduce(0, Integer::sum);
        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    private static char[][] convertToMatrix(List<String> input) {
        char[][] matrix = new char[input.size()][];

        for (int i = 0; i < input.size(); i++) {
            matrix[i] = input.get(i).toCharArray();
        }

        return matrix;
    }

    private static List<String> getNumbers(char[][] matrix) {
        List<Character> currentNumber = new ArrayList<>();
        final List<String> validNumbers = new ArrayList<>();
        boolean hasSymbol = false;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                final char curr = matrix[i][j];

                if (Character.isDigit(curr)) {
                    currentNumber.add(curr);
                    hasSymbol = hasSymbol ? true : ajdacentSymbol(i, j ,matrix);
                    continue;
                }

                if (!currentNumber.isEmpty() && hasSymbol) {
                    validNumbers.add(convertListToString(currentNumber));
                }

                currentNumber = new ArrayList<>();
                hasSymbol = false;
            }

            if (!currentNumber.isEmpty() && hasSymbol) {
                validNumbers.add(convertListToString(currentNumber));
            }

            currentNumber = new ArrayList<>();
            hasSymbol = false;
        }

        return validNumbers;
    }

    private static boolean ajdacentSymbol(int i, int j, char[][] matrix) {
        final int[] iIndex = {i - 1, i, i + 1};
        final int[] jIndex = {j - 1, j, j + 1};

        for (int iIdx : iIndex) {
            for (int jIdx : jIndex) {
                try {
                    final char matrixChar = matrix[iIdx][jIdx];

                    if (!Character.isDigit(matrixChar) && !(matrixChar == '.')) {
                        return true;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            }
        }

        return false;
    }

    private static String ajdacentGear(int i, int j, char[][] matrix) {
        final int[] iIndex = {i - 1, i, i + 1};
        final int[] jIndex = {j - 1, j, j + 1};

        for (int iIdx : iIndex) {
            for (int jIdx : jIndex) {
                try {
                    final char matrixChar = matrix[iIdx][jIdx];

                    if (matrixChar == '*') {
                        return getGearString(iIdx, jIdx);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            }
        }

        return null;
    }

    private static Map<String, Gear> updateGears(char[][] matrix, Map<String, Gear> allGears) {
        List<Character> currentNumber = new ArrayList<>();
        List<String> adjacentGears = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                final char curr = matrix[i][j];

                if (Character.isDigit(curr)) {
                    currentNumber.add(curr);
                    final String adjGear = ajdacentGear(i, j, matrix);

                    if (adjGear != null && !adjacentGears.contains(adjGear)) {
                        adjacentGears.add(adjGear);
                    }

                    continue;
                }
                if (!currentNumber.isEmpty() && !adjacentGears.isEmpty()) {
                    for (String gearLocation : adjacentGears) {
                        allGears.get(gearLocation).addNumber(convertListToString(currentNumber));
                    }
                }

                currentNumber = new ArrayList<>();
                adjacentGears = new ArrayList<>();
            }

            if (!currentNumber.isEmpty() && !adjacentGears.isEmpty()) {
                for (String gearLocation : adjacentGears) {
                    allGears.get(gearLocation).addNumber(convertListToString(currentNumber));
                }
            }

            currentNumber = new ArrayList<>();
            adjacentGears = new ArrayList<>();
        }

        return allGears;
    }

    private static String convertListToString(List<Character> charList) {
        return charList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private static Map<String, Gear> getGearLocations(char[][] matrix) {
        final Map<String, Gear> allGears = new HashMap<>();
        
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                final char curr = matrix[i][j];

                if (!(curr == '*')) {
                    continue;
                }

                allGears.put(getGearString(i, j), new Gear());
            }
        }

        return allGears;
    }

    private static List<Gear> filterGears(Map<String, Gear> objectMap) {
        final List<Gear> resultList = new ArrayList<>();

        for (Gear obj : objectMap.values()) {
            if (obj.getCount() == 2) {
                resultList.add(obj);
            }
        }

        return resultList;
    }

    private static String getGearString(int i, int j) {
        return String.format("%d,%d", i, j);
    }

    static class Gear {
        private int count = 0;
        private final List<String> adjacentNumbers = new ArrayList<>();

        public Gear() {}

        public void addNumber(String number) {
            adjacentNumbers.add(number);
            count++;
        }

        public int getCount() {
            return count;
        }

        public int getRatio() {
            int ratio = 1;

            for (String number : adjacentNumbers) {
                ratio *= Integer.parseInt(number);
            }

            return ratio;
        }
    }
}
