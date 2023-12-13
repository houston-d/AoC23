package solutions;

import Runner.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Day13 implements Day {
    private static final Logger logger = Logger.getLogger(Day13.class.getSimpleName());

    public void part1(List<String> input) {
        final List<char[][]> inputMatrices = parseInput(input);

        int sum = 0;

        for (char[][] matrix : inputMatrices) {
            final List<Integer> mirrors = findMirror(matrix);
            if (!mirrors.isEmpty()) {
                sum += mirrors.get(0);
            }
        }

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    public void part2(List<String> input) {
        final List<char[][]> inputMatrices = parseInput(input);

        int sum = 0;

        for (char[][] matrix : inputMatrices) {
            sum += findMirrorWithSmudge(matrix);
        }

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    private static List<char[][]> parseInput(List<String> input) {
        final List<char[][]> matrices = new ArrayList<>();
        List<String> current = new ArrayList<>();

        for (String s : input) {
            if (s.isEmpty()) {
                matrices.add(convertToMatrix(current));
                current = new ArrayList<>();
                continue;
            }

            current.add(s);
        }

        matrices.add(convertToMatrix(current));
        return matrices;
    }

    private static char[][] convertToMatrix(List<String> input) {
        char[][] matrix = new char[input.size()][];

        for (int i = 0; i < input.size(); i++) {
            matrix[i] = input.get(i).toCharArray();
        }

        return matrix;
    }

    private static int findMirrorWithSmudge(char[][] inputMatrix) {
        final List<Integer> existing = findMirror(inputMatrix);

        for (int i = 0; i < inputMatrix.length; i++) {
            for (int j = 0; j < inputMatrix[0].length; j++) {
                final char[][] copy = deepCopyCharMatrix(inputMatrix);
                copy[i][j] = copy[i][j] == '#' ? '.' : '#';

                final List<Integer> newMirror = findMirror(copy);
                final List<Integer> remaining = new ArrayList<>(newMirror);

                remaining.removeAll(existing);

                if (!remaining.isEmpty()) {
                    return remaining.get(0);
                }
            }
        }

        throw new IllegalStateException();
    }

    private static List<Integer> findMirror(char[][] inputMatrix) {
        final List<Integer> reflectionPoint = findReflectionPoints(inputMatrix);
        final List<Integer> reflectionPointTrans = findReflectionPoints(transposeCharMatrix(inputMatrix));

        final List<Integer> points = new ArrayList<>(reflectionPointTrans);

        for (Integer i : reflectionPoint) {
            points.add(100 * i);
        }

        return points;
    }

    private static List<Integer> findReflectionPoints(char[][] matrix) {
        int size = matrix.length;
        final List<Integer> points = new ArrayList<>();

        for (int i = 1; i < size; i++) {
            if (isReflection(matrix, i)) {
                points.add(i);
            }
        }

        return points;
    }

    private static boolean isReflection(char[][] matrix, int reflectionIndex) {
        int size = matrix.length;

        for (int i = reflectionIndex, j = reflectionIndex - 1; i < size && j>=0; i++, j--) {
            if (!Arrays.equals(matrix[i], matrix[j])) {
                return false;
            }
        }

        return true;
    }

    private static char[][] transposeCharMatrix(char[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        char[][] transposedMatrix = new char[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }

        return transposedMatrix;
    }

    private static char[][] deepCopyCharMatrix(char[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        char[][] copiedMatrix = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            System.arraycopy(matrix[i], 0, copiedMatrix[i], 0, cols);
        }

        return copiedMatrix;
    }
}
