package solutions;

import Runner.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class Day05 implements Day {
    private static final Logger logger = Logger.getLogger(Day05.class.getSimpleName());

    public void part1(List<String> input) {
        final String seeds = input.remove(0);
        final List<Long> allSeeds = getSeeds(seeds);
        final Map<Conversion, SeedMap> maps = splitInput(input);

        final List<Long> locations = new ArrayList<>();
        for (Long i : allSeeds) {
            Conversion current = Conversion.SEED;
            Long currentValue = i;
            while (!current.equals(Conversion.LOCATION)) {
                final SeedMap currentSeedMap = maps.get(current);
                currentValue = currentSeedMap.convertValue(currentValue);
                current = currentSeedMap.target;
            }

            locations.add(currentValue);
        }

        final Long minimum = Collections.min(locations);
        logger.log(Answer.ANSWER, String.valueOf(minimum));
    }

    public void part2(List<String> input) {
        final String seeds = input.remove(0);
        final List<Long> allSeeds = getSeeds(seeds);
        final Map<Conversion, SeedMap> maps = splitInput(input, true);
        final Long maxLocation = 178159714L;
        Long currentLocation = 0L;

        while (currentLocation.compareTo(maxLocation) < 0) {
            Conversion currentType = Conversion.LOCATION;
            Long currentValue = currentLocation;

            while (!currentType.equals(Conversion.SEED)) {
                final SeedMap currentSeedMap = maps.get(currentType);
                currentValue = currentSeedMap.reverseConvert(currentValue);
                currentType = currentSeedMap.origin;
            }

            if (isValidSeed(currentValue, allSeeds)) {
                logger.log(Answer.ANSWER, String.valueOf(currentLocation));
                System.exit(0);
            }

            currentLocation += 1L;
        }
    }

    private static boolean isValidSeed(Long seed, List<Long> ranges) {
        for (int i = 0; i < ranges.size(); i += 2) {
            final Long first = ranges.get(i);
            final Long second = ranges.get(i + 1);
            final Long diff = seed - first;

            if (diff.compareTo(0L) > 0 && diff.compareTo(second) < 0) {
                return true;
            }
        }
        return false;
    }

    private static List<Long> getSeeds(String seeds) {
        return Arrays.stream(seeds.split(": ")[1].split(" ")).map(Long::new).toList();
    }

    private static Map<Conversion, SeedMap> splitInput(List<String> inputList) {
        return splitInput(inputList, false);
    }

    private static Map<Conversion, SeedMap> splitInput(List<String> inputList, boolean reverse) {
        final List<String> sublist = new ArrayList<>();
        final Map<Conversion, SeedMap> conversionSeedMap = new HashMap<>();

        for (String str : inputList) {
            if (str.isEmpty()) {
                if (!sublist.isEmpty()) {
                    final SeedMap newMap = new SeedMap(sublist);
                    final Conversion loc = reverse ? newMap.target : newMap.origin;
                    conversionSeedMap.put(loc, newMap);
                    sublist.clear();
                }
            } else {
                sublist.add(str);
            }
        }

        if (!sublist.isEmpty()) {
            final SeedMap newMap = new SeedMap(sublist);
            final Conversion loc = reverse ? newMap.target : newMap.origin;
            conversionSeedMap.put(loc, newMap);
        }

        return conversionSeedMap;
    }

    enum Conversion {
        SEED,
        SOIL,
        FERTILIZER,
        WATER,
        LIGHT,
        TEMPERATURE,
        HUMIDITY,
        LOCATION,
    }


    static class SeedMap {

        public Conversion origin;
        public Conversion target;
        private List<SingleMap> maps;

        public SeedMap(List<String> mapList) {
            final String title = mapList.remove(0);

            setOriginTarget(title);
            parseMaps(mapList);
        }

        private void setOriginTarget(String title) {
            final String[] directions = title.replace(" map:", "").replace("-to-", "-").split("-");
            origin = Conversion.valueOf(directions[0].toUpperCase());
            target = Conversion.valueOf(directions[1].toUpperCase());
        }

        private void parseMaps(List<String> mapList) {
            maps = mapList.stream()
                    .map(this::parseMap)
                    .map(SingleMap::new)
                    .toList();
        }

        private List<Long> parseMap(String map) {
            return Arrays.stream(map.split(" ")).map(Long::valueOf).toList();
        }

        public Long convertValue(Long value) {
            final Optional<Long> o = maps.stream().map((SingleMap sm) -> sm.convertValue(value)).filter((Long i) -> i.compareTo(-1L) != 0).findFirst();

            return o.orElse(value);
        }

        public Long reverseConvert(Long value) {
            final Optional<Long> o = maps.stream().map((SingleMap sm) -> sm.reverseConvert(value)).filter((Long i) -> i.compareTo(-1L) != 0).findFirst();

            return o.orElse(value);
        }
    }

    static class SingleMap {
        private final Long destStart;
        private final Long srcStart;
        private final Long range;

        public SingleMap(List<Long> values) {
            destStart = values.get(0);
            srcStart = values.get(1);
            range = values.get(2);
        }

        public Long convertValue(Long value) {
            final Long diff = value - srcStart;

            if (diff.compareTo(0L) < 0 || diff.compareTo(range) > 0) {
                return -1L;
            }

            return destStart + diff;
        }

        public Long reverseConvert(Long value) {
            final Long diff = value - destStart;

            if (diff.compareTo(0L) < 0 || diff.compareTo(range) > 0) {
                return -1L;
            }

            return srcStart + diff;
        }
    }
}
