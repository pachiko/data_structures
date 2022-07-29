package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.*;

public class Player implements Serializable {
    PointI pos;
    Interior currentRoom;

    public void spawn(List<Interior> rooms, Random rng) {
        currentRoom = rooms.get(0);
        RectI startZone = currentRoom.rect.shrunk(1);
        pos = new PointI(RandomUtils.uniform(rng, startZone.xmin(), startZone.xmax()),
                RandomUtils.uniform(rng, startZone.ymin(), startZone.ymax()));
    }

    public void draw(TETile[][] currentWorld) {
        currentWorld[pos.getX()][pos.getY()] = Tileset.AVATAR;
    }

    public void move(Direction d) {
        PointI oldPos = new PointI(pos);
        switch (d) {
            case North -> pos.setY(pos.getY() + 1);
            case South -> pos.setY(pos.getY() - 1);
            case East -> pos.setX(pos.getX() + 1);
            case West -> pos.setX(pos.getX() - 1);
        }
        if (hasCollision()) pos = oldPos;
    }

    private boolean hasCollision() {
        boolean res = !currentRoom.rect.shrunk(1).contains(pos);

        if (res) {
            if (currentRoom.connections != null) {
                for (Map.Entry<Direction, HashSet<Interior>> entry : currentRoom.connections.entrySet()) {
                    for (Interior neighbor : entry.getValue()) {
                        RectI overlap = currentRoom.rect.overlap(neighbor.rect);
                        if (overlap.contains(pos) && !overlap.isCorner(pos.getX(), pos.getY())) {
                            return false;
                        } else if (neighbor.rect.shrunk(1).contains(pos)) {
                            currentRoom = neighbor;
                            return false;
                        }
                    }
                }
            }
        }

        return res;
    }
}
