package byow.lab12;
import org.antlr.v4.runtime.misc.Pair;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashSet;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final Random RANDOM = new Random();
    private static TETile currentTile;

    /**
     * Picks a RANDOM tile with a 33% change of being
     * a wall, 33% chance of being a flower, and 33%
     * chance of being empty space.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.TREE;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            default: return Tileset.WALL;
        }
    }

    /**
     * Shifts a tile's center to the top-right
     */
    private static Pair<Integer, Integer> shiftTopRight(int x, int y, int hexSize) {
        int newX = x + (2*hexSize - 1);
        int newY = y + hexSize;
        return new Pair<>(newX, newY);
    }

    /**
     * Shifts a tile's center to the top-left
     */
    private static Pair<Integer, Integer> shiftTopLeft(int x, int y, int hexSize) {
        int newX = x - (2*hexSize - 1);
        int newY = y + hexSize;
        return new Pair<>(newX, newY);
    }

    /**
     * Shifts a tile's center to the bottom-right
     */
    private static Pair<Integer, Integer> shiftBotRight(int x, int y, int hexSize) {
        int newX = x + (2*hexSize - 1);
        int newY = y - hexSize;
        return new Pair<>(newX, newY);
    }

    /**
     * Tile hexagons from bottom-left
     */
    private static void tileHexagons(TETile[][] res, Integer hexSize) {
        if (hexSize == null) hexSize = RANDOM.nextInt( 5) + 1; // 2 to 5

        // Base: start from bottom-left.
        int bx = hexSize + 1;
        int by = hexSize;

        HashSet<Pair<Integer, Integer>> visited = new HashSet<>();
        tileHexagonHelper(res, bx, by, hexSize, visited);
    }

    /**
     * Tile hexagons recursively.
     */
    private static void tileHexagonHelper(TETile[][] res, int x, int y, int hexSize, HashSet<Pair<Integer, Integer>> visited) {
        if (!checkHexagon(x, y, hexSize)) return;
        Pair<Integer, Integer> p = new Pair<>(x, y);
        if (visited.contains(p)) return;

        generateHexagon(res, x, y, hexSize);
        visited.add(p);

        Pair<Integer, Integer> topLeft = shiftTopLeft(x, y, hexSize);
        tileHexagonHelper(res, topLeft.a, topLeft.b, hexSize, visited);
        Pair<Integer, Integer> topRight = shiftTopRight(x, y, hexSize);
        tileHexagonHelper(res, topRight.a, topRight.b, hexSize, visited);
        Pair<Integer, Integer> botRight = shiftBotRight(x, y, hexSize);
        tileHexagonHelper(res, botRight.a, botRight.b, hexSize, visited);
    }

    /**
     * Check if hexagon can be drawn
     */
    private static boolean checkHexagon(int x, int y, int hexSize) {
        int left = x - hexSize;
        if (hexSize %2 == 0) left -= 1;
        if (!checkX(left)) return false;

        int right = x + hexSize;
        if (!checkX(right)) return false;

        int top = y + hexSize - 1;
        if (!checkY(top)) return false;

        int bottom = y - hexSize;
        return checkY(bottom);
    }

    /**
     * Check if x and y is within image bounds
     */
    private static boolean checkX(int x) { return x >= 0 && x <= WIDTH - 1; }
    private static boolean checkY(int y) { return y >= 0 && y <= HEIGHT - 1; }

    /**
     * Generate a single hexagon at the location (x, y)
     */
    private static void generateHexagon(TETile[][] res, int x, int y, Integer hexSize) {
        if (!checkHexagon(x, y, hexSize)) return;

        currentTile = randomTile();

        if (hexSize == null) hexSize = RANDOM.nextInt( 5) + 1; // 2 to 5
        // draw top and bottom rows (length = hexSize)
        // towards center, +2 size each time

        int count = 0;
        while (count < hexSize) {
            drawSingleRow(res, hexSize + 2*count, x, y + hexSize - count - 1); // top
            drawSingleRow(res, hexSize + 2*count, x, y + count - hexSize); // bottom
            count++;
        }
    }

    /**
     * Helper to draw a row of a hexagon
     */
    private static void drawSingleRow(TETile[][] res, int size, int x, int y) {
        boolean even = (size%2 == 0);
        for (int c = -size/2; even ? c < size/2 : c <= size/2; c++) {
            int xc = x + c;
            res[xc][y] = currentTile;
        }
    }

    /**
     * Main
     */
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] res = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) for (int y = 0; y < HEIGHT; y += 1) res[x][y] = Tileset.NOTHING; // >:)

        tileHexagons(res, 5);
        ter.renderFrame(res);
    }
}
