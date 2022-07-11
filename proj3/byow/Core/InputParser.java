package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;

public class InputParser {
    public GameArg parseFirst(InputSource src) {
        if (src.possibleNextInput()) {
            char c = src.getNextKey();
            return gameArgLookup(c);
        }
        return GameArg.Unknown;
    }

    public long parseSeed(InputSource src) {
        StringBuilder ss = new StringBuilder();

        while (src.possibleNextInput()) {
            char c = src.getNextKey();
            GameArg ga = gameArgLookup(c);

            if (!Character.isDigit(c) && ga == GameArg.Unknown) return 0;
            else if (ga == GameArg.Seed) break;

            ss.append(c);
        }

        return Long.parseLong(ss.toString());
    }

    public GameArg gameArgLookup(char c) {
        c = Character.toLowerCase(c);
        switch (c) {
            case 'n': return GameArg.NewGame;
            case 'l': return GameArg.LoadGame;
            case 'q': return GameArg.QuitGame; // FIXME: :q ??
            case 's' : return GameArg.Seed;
            default : return GameArg.Unknown;
        }
    }

    public static void main(String[] args) {
        InputParser parser = new InputParser();
        StringInputDevice src = new StringInputDevice("N69420s");
        GameArg mode = parser.parseFirst(src);
        long seed = parser.parseSeed(src);
        System.out.println("Mode: " + mode);
        System.out.println("Seed: " + seed);
    }
}


;
