package byow.Core;

import java.io.*;
import java.nio.file.Files;

/**
 * Game IO. Saves or loads game files
 */
public class GameIO {
    /**
     * Serialize game data
     */
    private static byte[] serialize(Phase1World w, Player p) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(stream);
            GameState state = new GameState(w, p);
            objectStream.writeObject(state);
            objectStream.close();
            return stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Write game data
     */
    public static void write(File file, Phase1World w, Player p) {
        byte[] res = serialize(w, p);
        if (res == null) return;

        try {
            if (file.isDirectory()) {
                throw new IllegalArgumentException("cannot overwrite directory");
            }
            BufferedOutputStream str = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            str.write(res);
            str.close();
        } catch (IOException | ClassCastException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /**
     * Read game data
     */
    public static GameState read(File file) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            GameState result = GameState.class.cast(in.readObject());
            in.close();
            return result;
        } catch (IOException | ClassCastException
                | ClassNotFoundException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /**
     * Game state.
     * A static nested class may be instantiated without instantiating its outer class.
     */
    static class GameState implements Serializable {
        Phase1World world;
        Player player;

        public GameState(Phase1World world, Player player) {
            this.world = world;
            this.player = player;
        }
    }
}
