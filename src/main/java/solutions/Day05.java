package solutions;

import Runner.Answer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day05 implements Day {
    private static final Logger logger = Logger.getLogger(Day05.class.getSimpleName());

    public void part1(List<String> input) {
        final String seeds = input.remove(0);
        final List<BigInteger> allSeeds = getSeeds(seeds);
        final Map<Conversion, SeedMap> maps = splitInput(input);

        final List<BigInteger> locations = new ArrayList<>();
        for (BigInteger i : allSeeds) {
            Conversion current = Conversion.SEED;
            BigInteger currentValue = i;
            while (!current.equals(Conversion.LOCATION)) {
                final SeedMap currentSeedMap = maps.get(current);
                currentValue = currentSeedMap.convertValue(currentValue);
                current = currentSeedMap.target;
            }

            locations.add(currentValue);
        }

        final BigInteger minimum = Collections.min(locations);
        logger.log(Answer.ANSWER, String.valueOf(minimum));
    }

    public void part2(List<String> input) {
        final String seeds = input.remove(0);
        final List<BigInteger> allSeeds = getSeeds(seeds);
        final Map<Conversion, SeedMap> maps = splitInput(input, true);

        BigInteger currentLocation = new BigInteger("0");
        BigInteger maxLocation = new BigInteger("178159714");

        while (currentLocation.compareTo(maxLocation) < 0) {
            if (currentLocation.mod(BigInteger.valueOf(1000000L)).equals(BigInteger.ZERO)) {
                System.out.println(String.valueOf(currentLocation));
            }
            Conversion currentType = Conversion.LOCATION;
            BigInteger currentValue = currentLocation;
            while (!currentType.equals(Conversion.SEED)) {
                final SeedMap currentSeedMap = maps.get(currentType);
                currentValue = currentSeedMap.reverseConvert(currentValue);
                currentType = currentSeedMap.origin;
            }

            if (isValidSeed(currentValue, allSeeds)) {
                logger.log(Answer.ANSWER, String.valueOf(currentLocation));
                System.exit(0);
            }
            currentLocation = currentLocation.add(BigInteger.valueOf(1L));
        }
//
//        for

//        for (int i = 0; i < allSeeds.size(); i += 2) {
//            System.out.println(String.format("%d of %d", i, allSeeds.size()));
//            BigInteger current = allSeeds.get(i);
//            BigInteger max = current.add(allSeeds.get(i + 1));
//
//            while (current.compareTo(max) < 0) {
//                Conversion currentType = Conversion.SEED;
//                BigInteger currentValue = current;
//                while (!currentType.equals(Conversion.LOCATION)) {
//                    final SeedMap currentSeedMap = maps.get(currentType);
//                    currentValue = currentSeedMap.convertValue(currentValue);
//                    currentType = currentSeedMap.target;
//                }
//
//                if (bestLocation == null || currentValue.compareTo(bestLocation) < 0) {
//                    bestLocation = currentValue;
//                }
//                current = current.add(BigInteger.valueOf(1L));
//            }
//
//        }

//        logger.log(Answer.ANSWER, String.valueOf(bestLocation));
    }

    private static boolean isValidSeed(BigInteger seed, List<BigInteger> ranges) {
        for (int i = 0; i < ranges.size(); i += 2) {
            BigInteger first = ranges.get(i);
            BigInteger second = ranges.get(i + 1);
            BigInteger diff = seed.subtract(first);
            if (diff.compareTo(BigInteger.valueOf(0L)) > 0 && diff.compareTo(second) < 0) {
                return true;
            }
        }
        return false;
    }

    private static List<BigInteger> getSeeds(String seeds) {
        return Arrays.stream(seeds.split(": ")[1].split(" ")).map(BigInteger::new).toList();
    }

    private static Map<Conversion, SeedMap> splitInput(List<String> inputList) {
        return splitInput(inputList, false);
    }

    private static Map<Conversion, SeedMap> splitInput(List<String> inputList, boolean reverse) {
        List<String> sublist = new ArrayList<>();

        Map<Conversion, SeedMap> conversionSeedMap = new HashMap<>();

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

        private List<BigInteger> parseMap(String map) {
            return Arrays.stream(map.split(" ")).map(BigInteger::new).toList();
        }

        public BigInteger convertValue(BigInteger value) {
            Optional<BigInteger> o = maps.stream().map((SingleMap sm) -> sm.convertValue(value)).filter((BigInteger i) -> i.compareTo(BigInteger.valueOf(-1L)) != 0).findFirst();

            return o.orElse(value);
        }

        public BigInteger reverseConvert(BigInteger value) {
            Optional<BigInteger> o = maps.stream().map((SingleMap sm) -> sm.reverseConvert(value)).filter((BigInteger i) -> i.compareTo(BigInteger.valueOf(-1L)) != 0).findFirst();

            return o.orElse(value);
        }
    }

    static class SingleMap {
        private final BigInteger destStart;
        private final BigInteger srcStart;
        private final BigInteger range;

        public SingleMap(List<BigInteger> values) {
            destStart = values.get(0);
            srcStart = values.get(1);
            range = values.get(2);
        }

        public BigInteger convertValue(BigInteger value) {
            final BigInteger diff = value.subtract(srcStart);

            if (diff.compareTo(BigInteger.valueOf(0L)) < 0 || diff.compareTo(range) > 0) {
                return BigInteger.valueOf(-1L);
            }

            return destStart.add(diff);
        }

        public BigInteger reverseConvert(BigInteger value) {
            final BigInteger diff = value.subtract(destStart);

            if (diff.compareTo(BigInteger.valueOf(0L)) < 0 || diff.compareTo(range) > 0) {
                return BigInteger.valueOf(-1L);
            }

            return srcStart.add(diff);
        }
    }
}
