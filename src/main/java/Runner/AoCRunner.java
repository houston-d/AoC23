package Runner;

import solutions.Day;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class AoCRunner {

    public static final Logger logger = Logger.getLogger("Runner");

    public static void main(String[] args) {
        if (args.length != 2) {
            final String err = String.format("Error when parsing %s arguments. Please provide exactly 2%n", args.length);
            logger.log(Level.SEVERE, err);
            System.exit(1);
        }

        final String day = args[0];
        final String part = args[1];

        if (Integer.parseInt(day) < 1 || Integer.parseInt(day) > 25) {
            final String err = String.format("Day must be an integer between 1 and 25 but was %s", day);
            logger.log(Level.SEVERE, err);
            System.exit(1);
        }

        if (Integer.parseInt(part) < 1 || Integer.parseInt(part) > 2) {
            final String err = String.format("Part must be an integer between 1 and 2 but was %s", part);
            logger.log(Level.SEVERE, err);
            System.exit(1);
        }

        final SolutionMapper solutionMapper = new SolutionMapper();
        final Day currentDay = solutionMapper.getInstance(day);

        final List<String> inputList = PuzzleInput.getPuzzleInput(day);

        switch (part) {
            case "1" -> currentDay.part1(inputList);
            case "2" -> currentDay.part2(inputList);
            default -> logger.log(Level.INFO, "Incorrect part specified");
        }
    }
}
