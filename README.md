# AoC23

Contained here are my solutions for Advent of Code 2023 using java.

To run the solution to a specific day and part, please include the numbers as command line arguments in the format `{day} {part}`

For example, to run day 3 part 1 use `java src/main/java/Runner/AoCRunner 3 1` or set them in the IDE config

When retrieving the puzzle input, the code will first look for the appropriate text file within the resources folder (eg `day03.txt`). If this file exists then it will be parsed. If no file is found then it will retrieve the input from the AoC website. To make use of this feature, the environment variable `COOKIE` must be defined with the session id. This will then also write the input to the correct file to prevent repeated polling.

To use the test input, set the environment variable `TEST` to any value and create a file `testXX.txt` within the resources folder. 