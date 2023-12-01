package solutions;

import Runner.Answer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day01 implements Day {
    private static final Logger logger = Logger.getLogger(Day01.class.getSimpleName());

    private static final Map<String, Integer> wordMap = new HashMap<>();

    public void part1(List<String> input) {
        int sum = 0;

        for (String s : input) {
            sum += extractDigits(s);
        }

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    public void part2(List<String> input) {
        populateMap();
        int sum = 0;

        for (String s : input) {
            sum += includeWrittenNumbers(s);
        }

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    private void populateMap() {
        wordMap.put("one", 1);
        wordMap.put("two", 2);
        wordMap.put("three", 3);
        wordMap.put("four", 4);
        wordMap.put("five", 5);
        wordMap.put("six", 6);
        wordMap.put("seven", 7);
        wordMap.put("eight", 8);
        wordMap.put("nine", 9);
    }


    private static int extractDigits(String inputString) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(inputString);

        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            result.append(matcher.group());
        }

        final String allDigits = result.toString();

        return Integer.parseInt(String.format("%s%s", allDigits.charAt(0), allDigits.charAt(allDigits.length() - 1)));
    }

    private static Integer includeWrittenNumbers(String inputString) {
        int firstIndex = 99;
        int lastIndex = -1;
        String first = null;
        String last = null;

        for (int i = 0; i < inputString.length(); i++) {
            final char ch = inputString.charAt(i);
            if (Character.isDigit(ch)) {
                if (i < firstIndex) {
                    firstIndex = i;
                    first = String.valueOf(ch);
                }
                lastIndex = i;
                last = String.valueOf(ch);
            }
        }

        for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            if (inputString.contains(entry.getKey()) && inputString.indexOf(entry.getKey()) < firstIndex) {
                first = entry.getValue().toString();
                firstIndex = inputString.indexOf(entry.getKey());
            }

            if (inputString.lastIndexOf(entry.getKey()) > lastIndex) {
                last = entry.getValue().toString();
                lastIndex = inputString.lastIndexOf(entry.getKey());
            }
        }

        return Integer.parseInt(String.format("%s%s", first, last));
    }
}
