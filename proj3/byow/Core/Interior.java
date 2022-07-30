package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.*;

/**
 * Interior. Either a room or a hallway
 */
public class Interior implements Serializable {
    boolean isRoom;
    RectI rect;
    HashMap<Direction, HashSet<Interior>> connections;

    /**
     * Ctor
     */
    public Interior(PointI p, int w, int h, boolean isRoom) {
        this.isRoom = isRoom;
        this.rect = new RectI(p, w, h);
    }

    /**
     * Generate a new interior.
     */
    public static Interior newInterior(Random rng, PointI p, Direction d, boolean isRoom) {
        int w = randomDimension(rng, isRoom, d == Direction.North || d == Direction.South);
        int h = randomDimension(rng, isRoom, d == Direction.East || d == Direction.West);

        PointI startP = p;
        switch (d) {
            case North -> startP = new PointI(p.getX() - w/2, p.getY());
            case South -> startP = new PointI(p.getX() - w/2, p.getY() - h + 1);
            case East -> startP = new PointI(p.getX(), p.getY() - h/2);
            case West -> startP = new PointI(p.getX() - w + 1, p.getY() - h/2);
        };

        return new Interior(startP, w, h, isRoom);
    }

    /**
     * Branch off a new interior from this.
     */
    public Interior branchInterior(Random rng) {
        Direction nextDir = Direction.randomDirection(rng);
        PointI next = branchPoint(rng, nextDir);
        Interior res = newInterior(rng, next, nextDir, roomOrNot(rng));
        res.addConnection(this, Direction.opposite(nextDir));
        this.addConnection(res, nextDir);
        return res;
    }

    /**
     * Should the next interior be a room or a hallway.
     */
    private boolean roomOrNot(Random rng) {
        if (isRoom) return false;
        else return rng.nextBoolean();
    }

    /**
     * Dimensions of a new interior.
     */
    private static int randomDimension(Random rng, boolean isRoom, boolean narrow) {
        if (!isRoom) {
            if (narrow) {
                return RandomUtils.uniform(rng, 2) + 3;
            } else {
                return RandomUtils.uniform(rng, 4) + 4;
            }
        } else {
            return RandomUtils.uniform(rng, 3) + 4;
        }
    }

    /**
     * Branch point of the new interior from this.
     */
    private PointI branchPoint(Random rng, Direction nextDir) {
        return switch (nextDir) {
            case North -> new PointI(RandomUtils.uniform(rng, rect.w() - 2) + rect.xmin() + 1, rect.ymax());
            case South -> new PointI(RandomUtils.uniform(rng, rect.w() - 2) + rect.xmin() + 1, rect.ymin());
            case East -> new PointI(rect.xmax(),RandomUtils.uniform(rng, rect.h() - 2) + rect.ymin() + 1);
            case West -> new PointI(rect.xmin(),RandomUtils.uniform(rng, rect.h() - 2) + rect.ymin() + 1);
            default -> throw new IllegalStateException("Unexpected value: " + nextDir);
        };
    }

    /**
     * Collide with other interior?
     */
    public boolean collides(Interior other) {
        RectI selfShrunk = rect.shrunk(1);
        RectI otherShrunk = other.rect.shrunk(1);
        return selfShrunk.intersects(other.rect) || otherShrunk.intersects(rect);
    }

    /**
     * Connect with a previous interior along a direction.
     */
    private void addConnection(Interior prev, Direction dir) {
        if (connections == null) connections = new HashMap<>();
        HashSet<Interior> neighbors = connections.get(dir);
        if (neighbors == null) neighbors = new HashSet<>();
        neighbors.add(prev);
        connections.put(dir, neighbors);
    }

    public static void disconnect(Interior a, Interior b) {
        a.disconnect(b);
        b.disconnect(a);
    }

    public void disconnect(Interior other) {
        if (connections != null) {
            for (Map.Entry<Direction, HashSet<Interior>> entry : connections.entrySet()) {
                HashSet<Interior> hs = entry.getValue();
                hs.remove(other);
            }
        }
    }

    /**
     * Draw.
     */
    public void draw(TETile[][] currentWorld) {
        drawInterior(currentWorld);
        drawConnections(currentWorld);
    }

    /**
     * Draw interior.
     */
    public void drawInterior(TETile[][] currentWorld) {
        for (int i = rect.xmin(); i <= rect.xmax(); i++) {
            for (int j = rect.ymin(); j <= rect.ymax(); j++) {
                if (rect.isEdge(i, j)) {
                    currentWorld[i][j] = Tileset.WALL;
                } else {
                    currentWorld[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    /**
     * Draw connected areas as floors instead of walls.
     */
    public void drawConnections(TETile[][] currentWorld) {
        if (connections != null) {
            for (Map.Entry<Direction, HashSet<Interior>> entry : connections.entrySet()) {
                for (Interior neighbor : entry.getValue()) {
                    RectI overlap = rect.overlap(neighbor.rect);

                    for (int i = overlap.xmin(); i <= overlap.xmax(); i++) {
                        for (int j = overlap.ymin(); j <= overlap.ymax(); j++) {
                            if (rect.isCorner(i, j) || neighbor.rect.isCorner(i, j)) {
                                continue;
                            }
                            currentWorld[i][j] = Tileset.FLOOR;
                        }
                    }
                }
            }
        }
    }
}
