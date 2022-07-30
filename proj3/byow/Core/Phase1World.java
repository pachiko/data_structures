package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * World.
 */
public class Phase1World implements Serializable {
    transient TETile[][] currentWorld;
    RectI worldDims;
    int numInteriors;
    Random rng;
    List<Interior> rooms;

    /**
     * Used for debug
     */
    transient private TERenderer ter = null;
    transient private boolean debugMode = false;

    /**
     * Init tiles matrix of world
     */
    private void initWorld() {
        int w = worldDims.w();
        int h = worldDims.h();
        currentWorld = new TETile[w][h];
        for (int x = 0; x < w; x += 1) for (int y = 0; y < h; y += 1) currentWorld[x][y] = Tileset.NOTHING;
    }

    /**
     * Initialize the random world
     */
    private void initRandomWorld() {
        initWorld();
        int w = worldDims.w();
        int h = worldDims.h();
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
    public void generateNewWorld(int w, int h, Random rng) {
        this.rng = rng;
        worldDims = new RectI(0, 0, w - 1, h - 1);
        initRandomWorld();
        if (debugMode) ter.initialize(w, h);
        generateInterior();
        drawWorld();
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
    private void generateInterior() {
        Interior room;
        rooms = new ArrayList<>();

        do {
            room = Interior.newInterior(rng, randomWorldPos(), Direction.Unknown, true);
        } while(!validate(room));
        rooms.add(room);

        int fail = 0;
        int i = 0;
        while (rooms.size() < numInteriors) {
            Interior room2 = room.branchInterior(rng);

            if (!validate(room2)) {
                Interior.disconnect(room, room2);
                fail++;
                if (fail == 8) { // fallback to an earlier room if failed too many times
                    room = rooms.get(--i);
                    fail = 0;
                }
                continue;
            }

            room = room2;
            rooms.add(room);
            i = rooms.size() - 1;
        }
    }

    /**
     * Render the world's tiles
     */
    public void drawWorld() {
        initWorld();
        for (Interior interior : rooms) {
            interior.draw(currentWorld);
            if (debugMode) ter.renderFrame(currentWorld);
        }
    }

    /**
     * Debug mode to visualize world.
     */
    private void setDebugMode() {
        debugMode = true;
        ter = new TERenderer();
    }

    /**
     * Main sanity check.
     */
    public static void main(String[] args) {
        Phase1World w = new Phase1World();
        w.setDebugMode();
        w.generateNewWorld(80, 30, new Random(69420));
        w.ter.renderFrame(w.currentWorld);
    }
}
