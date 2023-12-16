package solutions;

import Runner.Answer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Day16 implements Day {
    private static final Logger logger = Logger.getLogger(Day16.class.getSimpleName());

    public void part1(List<String> input) {
        final Tile[][] map = input.stream().map(Day16::charToTile).toList().toArray(new Tile[0][]);
        final int energised = fireLaser(map, new Laser(0, new Point(0, 0)));

        logger.log(Answer.ANSWER, String.valueOf(energised));
    }

    public void part2(List<String> input) {
        final Tile[][] map = input.stream().map(Day16::charToTile).toList().toArray(new Tile[0][]);
        int max = 0;

        for (int i = 0; i < map.length; i++) {
            final int lower = fireLaser(map, new Laser(0, new Point(i, 0)));

            if (lower > max) {
                max = lower;
            }

            final int upper = fireLaser(map, new Laser(2, new Point(i, map[0].length - 1)));

            if (upper > max) {
                max = upper;
            }
        }

        for (int i = 0; i < map.length; i++) {
            final int lower = fireLaser(map, new Laser(1, new Point(0, i)));

            if (lower > max) {
                max = lower;
            }

            final int upper = fireLaser(map, new Laser(3, new Point(map.length - 1, i)));

            if (upper > max) {
                max = upper;
            }
        }

        logger.log(Answer.ANSWER, String.valueOf(max));
    }

    private static int fireLaser(Tile[][] map, Laser initial) {
        final List<Laser> lasers = new ArrayList<>();
        final List<Point> seen = new ArrayList<>();
        final List<Laser> seenLasers = new ArrayList<>();

        lasers.add(initial);
        seenLasers.add(initial);
        seen.add(new Point(initial.location.x, initial.location.y));

        while(!lasers.isEmpty()) {
            final Laser current = lasers.remove(0);
            try {
                final List<Laser> newLasers = map[current.location.x][current.location.y].energise(current);

                for (Laser l : newLasers) {
                    if (!seenLasers.contains(l) && l.inMap(map)) {
                        seenLasers.add(l);
                        lasers.add(l);
                    }
                }

                if (!seen.contains(current.location)) {
                    seen.add(new Point(current.location));
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {}
        }

        return seen.size();
    }

    private static Tile[] charToTile(String s) {
        return s.chars().mapToObj(Tile::new).toList().toArray(new Tile[0]);
    }

    static class Laser {
        final int direction;
        final Point location;

        public Laser(int d, Point l) {
            direction = d;
            location = l;
        }

        public boolean inMap(Tile[][] map) {
            if (location.x < 0 || location.x >= map.length) {
                return false;
            }

            return location.y >= 0 && location.y < map[0].length;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }

            if (!(object instanceof Laser laser)) {
                return false;
            }

            if (direction != laser.direction) {
                return false;
            }

            return location.equals(laser.location);
        }

        @Override
        public int hashCode() {
            return direction + (4 * location.x) * 1000 * location.y;
        }
    }

    static class Tile {
        final char value;
        boolean energised;
        final Point[] dirs = {
                new Point(0, 1),
                new Point(1, 0),
                new Point(0, -1),
                new Point(-1, 0),
        };

        public Tile(int i) {
            value = (char) i;
        }

        public List<Laser> energise(Laser l) {
            energised = true;

            return switch (value) {
                case '.' -> dot(l);
                case '|' -> pipe(l);
                case '-' -> hyphen(l);
                case '\\' -> backward(l);
                case '/' -> forward(l);
                default -> new ArrayList<>();
            };
        }

        public List<Laser> dot(Laser l) {
            final Laser a = new Laser(l.direction, new Point(l.location));
            a.location.translate(dirs[l.direction].x, dirs[l.direction].y);
            return List.of(a);
        }

        public List<Laser> pipe(Laser l) {
            return switch (l.direction) {
                case 1, 3 -> dot(l);
                default -> {
                    final Laser a = new Laser(1, new Point(l.location));
                    a.location.translate(dirs[a.direction].x, dirs[a.direction].y);

                    final Laser b = new Laser(3, new Point(l.location));
                    b.location.translate(dirs[b.direction].x, dirs[b.direction].y);

                    yield  List.of(a, b);
                }
            };
        }

        public List<Laser> hyphen(Laser l) {
            return switch (l.direction) {
                case 0, 2 -> dot(l);
                default -> {
                    final Laser a = new Laser(0, new Point(l.location));
                    a.location.translate(dirs[a.direction].x, dirs[a.direction].y);

                    final Laser b = new Laser(2, new Point(l.location));
                    b.location.translate(dirs[b.direction].x, dirs[b.direction].y);

                    yield  List.of(a, b);
                }
            };
        }

        public List<Laser> backward(Laser l) {
            return switch (l.direction) {
                case 0, 2 -> {
                    final Laser a = new Laser(l.direction + 1, new Point(l.location));
                    a.location.translate(dirs[a.direction].x, dirs[a.direction].y);
                    yield List.of(a);
                }
                case 1 , 3-> {
                    final Laser a = new Laser(l.direction - 1, new Point(l.location));
                    a.location.translate(dirs[a.direction].x, dirs[a.direction].y);
                    yield List.of(a);
                }
                default -> new ArrayList<>();
            };
        }

        public List<Laser> forward(Laser l) {
            return switch (l.direction) {
                case 0, 2 -> {
                    final Laser a = new Laser(((l.direction - 1) % 4 + 4) % 4, new Point(l.location));
                    a.location.translate(dirs[a.direction].x, dirs[a.direction].y);
                    yield List.of(a);
                }
                case 1 , 3-> {
                    final Laser a = new Laser((l.direction + 1) % 4, new Point(l.location));
                    a.location.translate(dirs[a.direction].x, dirs[a.direction].y);
                    yield List.of(a);
                }
                default -> new ArrayList<>();
            };
        }
    }
}
