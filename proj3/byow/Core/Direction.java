package byow.Core;

import java.util.Random;

public enum Direction  {
    Unknown,
    North,
    South,
    East,
    West;

    public static Direction movement(char c) {
        return switch (Character.toLowerCase(c)) {
            case 'w' -> North;
            case 's' -> South;
            case 'a' -> West;
            case 'd' -> East;
            default -> Unknown;
        };
    }

    public static Direction opposite(Direction d) {
        return switch (d) {
            case North -> South;
            case South -> North;
            case East -> West;
            case West -> East;
            default -> d;
        };
    }

    public static Direction randomDirection(Random rng) {
        int res = RandomUtils.uniform(rng, 4);
        return switch (res) {
            case 0 -> Direction.North;
            case 1 -> Direction.South;
            case 2 -> Direction.East;
            case 3 -> Direction.West;
            default -> Direction.Unknown;
        };
    }

    public static Direction randomDirection(Random rng, Direction d) {
        int[] freqs = {1, 1, 1, 1};
        if (d != Direction.Unknown) {
            switch (d) {
                case North -> freqs = new int[]{0, 1, 1, 1};
                case South -> freqs = new int[]{1, 0, 1, 1};
                case East -> freqs = new int[]{1, 1, 0, 1};
                case West -> freqs = new int[]{1, 1, 1, 0};
            }
        }

        int res = RandomUtils.discrete(rng, freqs);
        return switch (res) {
            case 0 -> Direction.North;
            case 1 -> Direction.South;
            case 2 -> Direction.East;
            case 3 -> Direction.West;
            default -> d;
        };
    }
}