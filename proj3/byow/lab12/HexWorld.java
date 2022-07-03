package byow.lab12;
import byow.Core.PointI;

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
    private static PointI shiftTopRight(PointI p, int hexSize) {
        int newX = p.getX() + (2*hexSize - 1);
        int newY = p.getY() + hexSize;
        return new PointI(newX, newY);
    }

    /**
     * Shifts a tile's center to the top-left
     */
    private static PointI shiftTopLeft(PointI p, int hexSize) {
        int newX = p.getX() - (2*hexSize - 1);
        int newY = p.getY() + hexSize;
        return new PointI(newX, newY);
    }

    /**
     * Shifts a tile's center to the bottom-right
     */
    private static PointI shiftBotRight(PointI p, int hexSize) {
        int newX = p.getX() + (2*hexSize - 1);
        int newY = p.getY() - hexSize;
        return new PointI(newX, newY);
    }

    /**
     * Tile hexagons from bottom-left
     */
    private static void tileHexagons(TETile[][] res, Integer hexSize) {
        if (hexSize == null) hexSize = RANDOM.nextInt( 5) + 1; // 2 to 5

        // Base: start from bottom-left.
        int bx = hexSize + 1;
        int by = hexSize;
        PointI base = new PointI(bx, by);

        HashSet<PointI> visited = new HashSet<>();
        tileHexagonHelper(res, base, hexSize, visited);
    }

    /**
     * Tile hexagons recursively.
     */
    private static void tileHexagonHelper(TETile[][] res, PointI p, int hexSize, HashSet<PointI> visited) {
        if (!checkHexagon(p, hexSize)) return;
        if (visited.contains(p)) return;

        generateHexagon(res, p, hexSize);
        visited.add(p);

        PointI topLeft = shiftTopLeft(p, hexSize);
        tileHexagonHelper(res, topLeft, hexSize, visited);
        PointI topRight = shiftTopRight(p, hexSize);
        tileHexagonHelper(res, topRight, hexSize, visited);
        PointI botRight = shiftBotRight(p, hexSize);
        tileHexagonHelper(res, botRight, hexSize, visited);
    }

    /**
     * Check if hexagon can be drawn
     */
    private static boolean checkHexagon(PointI p, int hexSize) {
        int left = p.getX() - hexSize;
        if (hexSize %2 == 0) left -= 1;
        if (!checkX(left)) return false;

        int right = p.getX() + hexSize;
        if (!checkX(right)) return false;

        int top = p.getY() + hexSize - 1;
        if (!checkY(top)) return false;

        int bottom = p.getY() - hexSize;
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
    private static void generateHexagon(TETile[][] res, PointI p, Integer hexSize) {
        if (!checkHexagon(p, hexSize)) return;

        TETile currentTile = randomTile();

        if (hexSize == null) hexSize = RANDOM.nextInt( 5) + 1; // 2 to 5
        // draw top and bottom rows (length = hexSize)
        // towards center, +2 size each time

        int x = p.getX();
        int y = p.getY();
        int count = 0;
        while (count < hexSize) {
            drawSingleRow(res, hexSize + 2*count, x, y + hexSize - count - 1, currentTile); // top
            drawSingleRow(res, hexSize + 2*count, x, y + count - hexSize, currentTile); // bottom
            count++;
        }
    }

    /**
     * Helper to draw a row of a hexagon
     */
    private static void drawSingleRow(TETile[][] res, int size, int x, int y, TETile currentTile) {
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
