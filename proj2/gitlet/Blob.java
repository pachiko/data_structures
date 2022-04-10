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


    /** Change track count. */
    public void incrCount() {
        trackCount++;
    }
    public void decrCount() {
        trackCount--;
    }


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


    /** Write a new blob to directory. Returns SHA.
     *  If force: always write to disk
     *  If duplicate: exit early if exitDuplicate, else ignore writing
     *  Calculates sha if not passed-in.
     *  */
    public String write(boolean exitDuplicate, boolean force, String sha) {
        byte[] bytes = serialize(this);
        if (sha == null) sha = sha1(contents);
        File f = join(Repository.BLOB_DIR, sha);

        if (force) {
            writeContents(f, bytes);
        } else {
            if (exitDuplicate) checkDuplicate(f, sha);
            if (!f.exists()) writeContents(f, bytes);
        }
        return sha;
    }


    /** Check duplicate blob */
    public static void checkDuplicate(File f, String sha) {
        if (f.exists()) {
            System.out.println("Blob already exists! " + sha);
            System.exit(0);
        }
    }


    /** Dump */
    @Override
    public void dump() {
        System.out.println("=====START Dump Blob=====");
        System.out.println("Blob Track Count: " + trackCount);
        System.out.println("Blob Contents: " + contents);
        System.out.println("=====END Dump Blob=====");
    }
}
