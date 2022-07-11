package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Phase1WorldGen {
    private TETile[][] currentWorld;
    private RectI worldDims;
    private Random rng;
    private int numInteriors;
    private List<Interior> rooms;

    /**
     * Used for debug
     */
    private TERenderer ter;
    private boolean debugMode = false;

    /**
     * Initialize the world
     */
    private void initWorld() {
        int w = worldDims.w();
        int h = worldDims.h();

        currentWorld = new TETile[w][h];
        for (int x = 0; x < w; x += 1) for (int y = 0; y < h; y += 1) currentWorld[x][y] = Tileset.NOTHING;

        int avg = (w + h)/2;
        numInteriors = RandomUtils.uniform(rng,avg - 5, avg + 5);
    }

    /**
     * Random position to start from the left.
     */
    private PointI randomWorldPos() {
        int x = RandomUtils.uniform(rng, 5);
        int y = RandomUtils.uniform(rng, worldDims.h());
        return new PointI(x, y);
    }

    /**
     * Generate the world of size w x h using seed
     */
    public TETile[][] generateNewWorld(int w, int h, long seed) {
        rng = new Random(seed);
        worldDims = new RectI(0, 0, w - 1, h - 1);
        initWorld();
        if (debugMode) ter.initialize(w, h);

        PointI start = randomWorldPos();
        generateInterior(start, Direction.Unknown);

        return currentWorld;
    }

    /**
     * Check if this room is valid (ie not outside world and not colliding with other rooms)
     */
    private boolean validate(Interior room) {
        RectI r = room.rect;
        if (r.xmin() < 0 || r.ymin() < 0 || r.xmax() > worldDims.xmax() || r.ymax() > worldDims.ymax()) {
            if (debugMode) System.out.println("Out of world bounds");
            return false;
        }
        int i = 0;
        for (Interior other: rooms) {
            if (room.collides(other)) {
                if (debugMode) System.out.println("Collide with interior " + i);
                return false;
            }
            i++;
        }
        return true;
    }

    /**
     * Generate interiors using a point p and a direction d.
     */
    private void generateInterior(PointI p, Direction d) {
        Interior room = Interior.newInterior(rng, p, d, true);
        room.draw(currentWorld);
        if (debugMode) ter.renderFrame(currentWorld);

        rooms = new ArrayList<>();
        rooms.add(room);

        int fail = 0;
        int i = 0;
        while (rooms.size() < numInteriors) {
            Interior room2 = room.branchInterior(rng);

            if (!validate(room2)) {
                fail++;
                if (fail == 8) { // fallback to an earlier room if failed too many times
                    room = rooms.get(--i);
                    fail = 0;
                }
                continue;
            }
            room2.draw(currentWorld);
            if (debugMode) ter.renderFrame(currentWorld);

            room = room2;
            rooms.add(room);
            i = rooms.size() - 1;
        }
    }

    private void setDebugMode() {
        debugMode = true;
        ter = new TERenderer();
    }

    public static void main(String[] args) {
        Phase1WorldGen gen = new Phase1WorldGen();
        gen.setDebugMode();
        gen.generateNewWorld(80, 30, 69420);
        gen.ter.renderFrame(gen.currentWorld);
    }
}
