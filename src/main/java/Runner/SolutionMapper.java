package Runner;

import solutions.Day;
import solutions.Day01;
import solutions.Day02;
import solutions.Day03;
import solutions.Day04;
import solutions.Day05;
import solutions.Day06;
import solutions.Day07;
import solutions.Day08;
import solutions.Day09;
import solutions.Day10;
import solutions.Day11;
import solutions.Day12;
import solutions.Day13;
import solutions.Day14;
import solutions.Day15;
import solutions.Day16;
import solutions.Day17;
import solutions.Day18;
import solutions.Day19;
import solutions.Day20;
import solutions.Day21;
import solutions.Day22;
import solutions.Day23;
import solutions.Day24;
import solutions.Day25;

import java.util.HashMap;
import java.util.Map;

public class SolutionMapper {

    private final Map<String, Day> classMap = new HashMap<>();

    public SolutionMapper() {
        classMap.put("1", new Day01());
        classMap.put("2", new Day02());
        classMap.put("3", new Day03());
        classMap.put("4", new Day04());
        classMap.put("5", new Day05());
        classMap.put("6", new Day06());
        classMap.put("7", new Day07());
        classMap.put("8", new Day08());
        classMap.put("9", new Day09());
        classMap.put("10", new Day10());
        classMap.put("11", new Day11());
        classMap.put("12", new Day12());
        classMap.put("13", new Day13());
        classMap.put("14", new Day14());
        classMap.put("15", new Day15());
        classMap.put("16", new Day16());
        classMap.put("17", new Day17());
        classMap.put("18", new Day18());
        classMap.put("19", new Day19());
        classMap.put("20", new Day20());
        classMap.put("21", new Day21());
        classMap.put("22", new Day22());
        classMap.put("23", new Day23());
        classMap.put("24", new Day24());
        classMap.put("25", new Day25());
    }

    public Day getInstance(String day) {
        return classMap.get(day);
    }
}
