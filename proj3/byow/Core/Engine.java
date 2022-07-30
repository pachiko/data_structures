package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.File;
import java.nio.file.Paths;

import java.util.List;
import java.util.Random;

public class Engine {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    static final int HUD = (int) (9.*HEIGHT/10.);
    static final double HUDTEXT = 9.5*HEIGHT/10.;

    static final File CWD = new File(System.getProperty("user.dir"));
    static final File saveFile = Paths.get(CWD.getPath(), "BYoWGameState.txt").toFile();
    static final Font bigFont = new Font("Monaco", Font.BOLD, 30);
    static final Font mediumFont = new Font("Monaco", Font.BOLD, 20);

    private Phase1World world;
    private Player player;
    private TERenderer render = null;

    public void initializeRender() {
        render = new TERenderer();
        render.initialize(WIDTH, HEIGHT);
    }

    public void displayMainMenu() {
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(bigFont);
        StdDraw.text(WIDTH/2., 9.*HEIGHT/10.,"BYoW Game");
        StdDraw.text(WIDTH/2., 7.*HEIGHT/10.,"New Game (n)");
        StdDraw.text(WIDTH/2., 5.*HEIGHT/10.,"Load Game (l)");
        StdDraw.text(WIDTH/2., 3.*HEIGHT/10.,"Quit Game (q)");
        StdDraw.show();
    }

    public void displaySeed(String seed) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(bigFont);
        StdDraw.text(WIDTH/2., HEIGHT/2.,"Seed: " + seed);
        StdDraw.show();
    }

    public void displayGoodbye() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(bigFont);
        StdDraw.text(WIDTH/2., HEIGHT/2.,"Thank you for playing, see you again!");
        StdDraw.show();
    }

    public void drawWorld() {
        world.drawWorld();
        player.draw(world.currentWorld);
        render.renderFrame(world.currentWorld);
    }

    public void updateWorld(List<Direction> moves) {
        if (moves != null && !moves.isEmpty()) {
            Direction d = moves.get(moves.size() - 1);
            player.move(d);
        }
    }

    public void displayMouseInfo() {
        drawWorld();
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        if (y >= HUD) return;
        TETile pointed = world.currentWorld[(int) x][(int) y];
        if (pointed == Tileset.NOTHING) return;
        StdDraw.setFont(mediumFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH/2., HUDTEXT, pointed.description());
        StdDraw.show();
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        initializeRender();
        displayMainMenu();
        KeyboardInputSource src = new KeyboardInputSource();
        InputParser parser = new InputParser();
        GameArg mode;
        do {
            mode = parser.parseFirst(src);
        } while(mode == GameArg.Unknown);

        switch (mode) {
            case NewGame -> newGame(parser, src, true);
            case LoadGame -> loadGame();
            default -> {
                displayGoodbye();
                return;
            }
        }

	    if (world == null || player == null) return;
        drawWorld();

        do {
            parser.parseMovement(src, this);
        } while (!parser.parseSave(src));

        displayGoodbye();
        saveGame();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        InputParser parser = new InputParser();
        StringInputSource src = new StringInputSource(input);
        GameArg mode = parser.parseFirst(src);

        if (mode == GameArg.NewGame) newGame(parser, src, false);
        else if (mode == GameArg.LoadGame) loadGame();

        if (world == null || player == null) return null;

        List<Direction> moves = parser.parseMovement(src, null);
        boolean save = parser.parseSave(src);

        for (Direction d: moves) player.move(d);
        world.drawWorld();
        player.draw(world.currentWorld);

        if (save) saveGame();

        return world.currentWorld;
    }

    public void newGame(InputParser parser, InputSource src, boolean display) {
        if (display) displaySeed("");
        long seed = parser.parseSeed(src, display ? this : null);
        if (seed < 0) return;
        Random rng = new Random(seed);

        world = new Phase1World();
        world.generateNewWorld(WIDTH, HUD, rng);

        player = new Player();
        player.spawn(world.rooms, rng);
    }

    public void loadGame() {
        if (!saveFile.exists()) return;
        GameIO.GameState gs = GameIO.read(saveFile);
        if (gs == null) return;
        world = gs.world;
        player = gs.player;
    }

    public void saveGame() {
        if (world != null && player != null) {
            GameIO.write(saveFile, world, player);
        }
    }

    public static void main(String[] args) {
        Engine e = new Engine();
        e.interactWithKeyboard();

//        TETile[][] res = e.interactWithInputString("l"); // "N69420Swww:q");
//        TERenderer render = new TERenderer();
//        render.initialize(WIDTH, HEIGHT);
//        render.renderFrame(res);
    }
}
