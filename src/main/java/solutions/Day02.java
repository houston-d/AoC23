package solutions;

import Runner.Answer;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Day02 implements Day {
    private static final Logger logger = Logger.getLogger(Day02.class.getSimpleName());

    public void part1(List<String> input) {
        final List<Game> allGames = input.stream().map(Game::new).toList();
        final List<Integer> validGames = allGames.stream().map((Game game) -> game.isValid(12, 14, 13)).toList();
        final int sum = validGames.stream()
                .reduce(0, Integer::sum);

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    public void part2(List<String> input) {
        final List<Game> allGames = input.stream().map(Game::new).toList();
        final List<Integer> powers = allGames.stream().map(Game::getPower).toList();
        final int sum = powers.stream()
                .reduce(0, Integer::sum);

        logger.log(Answer.ANSWER, String.valueOf(sum));
    }

    static class Game {
        final public int number;
        final public List<Event> events;

        public Game(String inputString) {
            final String[] titleAndEvents = inputString.split(":");
            final String[] title = titleAndEvents[0].split(" ");
            number = Integer.parseInt(title[title.length - 1]);

            final String[] eventList = titleAndEvents[1].split(";");
            events = Arrays.stream(eventList).map(Event::new).toList();
        }

        public int isValid(int red, int blue, int green) {
            final int invalid = events.stream().filter((Event event) -> !event.isValid(red, blue, green)).toList().size();

            return invalid == 0 ? number : 0;
        }

        public int getPower() {
            int maxRed = 0;
            int maxBlue = 0;
            int maxGreen = 0;

            for (Event event : events) {
                if (event.red > maxRed) {
                    maxRed = event.red;
                }
                if (event.blue > maxBlue) {
                    maxBlue = event.blue;
                }
                if (event.green > maxGreen) {
                    maxGreen = event.green;
                }
            }

            return maxRed * maxBlue * maxGreen;
        }

    }

    static class Event {
        public int red;
        public int blue;
        public int green;

        public Event(String eventString) {
            final String[] countsAndBalls = eventString.trim().replace(",", "").split(" ");

            red = 0;
            blue = 0;
            green = 0;

            for (int i = 0; i < countsAndBalls.length; i += 2) {
                final String count = countsAndBalls[i];
                final String colour = countsAndBalls[i + 1];

                switch (colour) {
                    case "red" -> red += Integer.parseInt(count);
                    case "green" -> green += Integer.parseInt(count);
                    case "blue" -> blue += Integer.parseInt(count);
                    default -> {}
                }
            }
        }

        public boolean isValid(int r, int b, int g) {
            return red <= r && blue <= b && green <= g;
        }
    }
}
