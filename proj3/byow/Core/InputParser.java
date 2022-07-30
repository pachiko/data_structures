package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Input parser.
 */
public class InputParser {
    Character prevChar = null;

    /**
     * Parse first argument/game mode
     */
    public GameArg parseFirst(InputSource src) {
        if (src.possibleNextInput()) {
            char c = src.getNextKey();
            prevChar = c;
            return gameArgLookup(c);
        }
        return GameArg.Unknown;
    }

    /**
     * Parse seed for new game
     */
    public long parseSeed(InputSource src, Engine e) {
        StringBuilder ss = new StringBuilder();

        while (src.possibleNextInput()) {
            char c = src.getNextKey();
            if (c == '\u0000') continue; // Default char, so continue

            prevChar = c;
            GameArg ga = gameArgLookup(c);
            if (!Character.isDigit(c) && ga != GameArg.Seed) return -1;
            else if (ga == GameArg.Seed) break;

            ss.append(c);
            if (e != null) e.displaySeed(ss.toString());
        }

        return Long.parseLong(ss.toString());
    }

    /**
     * Parse toggle of low-visiblity mode
     */
    public boolean parseLowVis(char c) {
        return Character.toLowerCase(c) == 'v';
    }

    /**
     * Parse movement
     */
    public List<Direction> parseMovement(InputSource src, Engine e) {
        ArrayList<Direction> moves = new ArrayList<>();

        while (src.possibleNextInput()) {
            if (e != null) {
                e.drawWorld();
                e.displayMouseInfo(); // Always show mouse-info in HUD
            }

            char c = src.getNextKey();
            if (c == '\u0000') continue; // Default char, so continue

            prevChar = c;
            Direction d = Direction.movement(c);
            boolean lowVis = parseLowVis(c);
            if (lowVis && e != null) {
                e.toggleLowVis();
                continue;
            } else if (d == Direction.Unknown) break;

            moves.add(d);
            if (e != null) e.updateWorld(d);
        }

        return moves;
    }

    /**
     * Parse whether to save or not
     */
    public boolean parseSave(InputSource src) {
        boolean hasColon = prevChar == ':';

        while (src.possibleNextInput()) {
            char c = src.getNextKey();
            if (c == '\u0000') continue; // Default char, so continue

            GameArg ga = gameArgLookup(c);
            if (c == ':' && !hasColon) {
                hasColon = true;
            } else if (ga == GameArg.QuitGame && hasColon) {
                return true;
            } else {
                break;
            }
        }

        return false;
    }

    /**
     * Game argument look up
     */
    public GameArg gameArgLookup(char c) {
        c = Character.toLowerCase(c);
        return switch (c) {
            case 'n' -> GameArg.NewGame;
            case 'l' -> GameArg.LoadGame;
            case 'q' -> GameArg.QuitGame;
            case 's' -> GameArg.Seed;
            default -> GameArg.Unknown;
        };
    }

    /**
     * Main sanity check
     */
    public static void main(String[] args) {
        InputParser parser = new InputParser();
        StringInputSource src = new StringInputSource("N69420s:q");
        GameArg mode = parser.parseFirst(src);
        long seed = parser.parseSeed(src, null);
        List<Direction> moves = parser.parseMovement(src, null);
        boolean save = parser.parseSave(src);

        System.out.println("Mode: " + mode);
        System.out.println("Seed: " + seed);
        System.out.println("Moves: " + moves);
        System.out.println("Save: " + save);
    }
}


;
