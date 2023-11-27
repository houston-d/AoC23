package Runner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PuzzleInput {

    private static final Logger logger = Logger.getLogger(PuzzleInput.class.getSimpleName());

    private static final String URL_TEMPLATE = "https://adventofcode.com/2023/day/%s/input";

    public static List<String> getPuzzleInput(String day) {

        final String fileName = String.format("src/main/resources/day%s.txt", padWithZeros(Integer.parseInt(day), 2));
        final File f = new File(fileName);

        if (!f.isFile()) {
            try {
                final List<String> inputList = getInputFromURL(day);
                writeToFile(fileName, inputList);
                return inputList;
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
                System.exit(1);
            }
        }

        try {
            return getInputFromFile(f);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(1);
        }

        return new ArrayList<>();
    }

    private static void writeToFile(String filePath, List<String> lines) throws IOException{
        final Path path = Paths.get(filePath);
        Files.write(path, lines);
    }

    private static String padWithZeros(int number, int length) {
        return String.format("%0" + length + "d", number);
    }

    private static List<String> getInputFromFile(File file) throws IOException {
        final FileInputStream fileInputStream = new FileInputStream(file);
        final InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            return reader.lines().collect(Collectors.toList());
        }
    }

    private static List<String> getInputFromURL(String day) throws IOException {
        final URL url = new URL(String.format(URL_TEMPLATE, day));
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        connection.setRequestProperty("Cookie", "session=" + System.getenv("COOKIE"));

        final int responseCode = connection.getResponseCode();
        final String responseString = String.format("Response Code: %s", responseCode);
        logger.log(Level.INFO, responseString);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return reader.lines().collect(Collectors.toList());
        } finally {
            connection.disconnect();
        }
    }

    private PuzzleInput() {}
}
