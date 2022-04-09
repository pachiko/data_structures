package gitlet;


import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;

/** Represents a gitlet blob object. A blob is basically the contents of file(s).
 * Blobs reside in the blob directory
 * Blobs can be removed if no commits point to them and they are unstaged from staging area.
 *
 *  @author TODO
 */
public class Blob implements Serializable, Dumpable {
    /** Contents of this blob */
    private String contents;

    /** How many commits are tracking this blob */
    private int trackCount;


    /** Creates a blob with a given filename */
    public Blob(String contents) {
        this.contents = contents;
        trackCount = 0;
    }


    /** Read track count */
    public int getCount() { return trackCount; }


    /** Change track count */
    public void incrCount() { trackCount++; }
    public void decrCount() { trackCount--; }


    /** Returns the SHA */
    public String sha() {
        return sha1(contents);
    }


    /** Read blob if any */
    public static Blob read(String sha) {
        File f = join(Repository.BLOB_DIR, sha);
        if (f.exists()) {
            return readObject(f, Blob.class);
        }
        return null;
    }


    /** Write blob to directory. Returns SHA */
    public String write(boolean exitDuplicate) {
        byte[] bytes = serialize(this);
        String SHA = sha1(contents);
        File f = join(Repository.BLOB_DIR, SHA);
        if (exitDuplicate) checkDuplicate(f, SHA);
        if (!f.exists()) writeContents(f, bytes);
        return SHA;
    }


    /** Check duplicate blob */
    public static void checkDuplicate(File f, String sha) {
        if (f.exists()) {
            System.out.println("Blob already exists! " + sha);
            System.exit(0);
        }
    }


    /** Dump */
    public void dump() {
        System.out.println("=====START Dump Blob=====");
        System.out.println("Blob Track Count: " + trackCount);
        System.out.println("Blob Contents: " + contents);
        System.out.println("=====END Dump Blob=====");
    }
}
