package Runner;

import java.util.List;

public class Utils {

    public static char[][] toCharArray(List<String> input) {
        char[][] matrix = new char[input.size()][];

        for (int i = 0; i < input.size(); i++) {
            matrix[i] = input.get(i).toCharArray();
        }

        return matrix;
    }

    public static char[][] initializeCharArray(char val, int rows, int cols) {
        char[][] charArray = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                charArray[i][j] = val;
            }
        }

        return charArray;
    }

    public static char[][] rotateCharArray(char[][] array) {
        int rows = array.length;
        int cols = array[0].length;

        char[][] rotatedArray = new char[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotatedArray[j][rows - i - 1] = array[i][j];
            }
        }

        return rotatedArray;
    }

    private Utils(){}
}
