package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Map;

import static gitlet.Utils.*;

/** Represents a gitlet commit object. A commit represents a snapshot of the repo at a particular time.
 * Commits reside in the commit directory.
 * It also tracks a map of blobs to indicate the current contents.
 * Also knows about its parent commits and uses their tracked files initially.
 *
 *  @author TODO
 */
public class Commit implements Serializable, Dumpable {
    /** The message of this Commit. Cannot be NULL or empty! */
    private String message;

    /** Date of commit. Starts at the UNIX epoch (Jan 1 1970) for initial commit */
    private Date commitDate;

    /** Commit author's name*/
    private String author;

    /** Parent commit in the form of SHA-1 hash
     * Do not keep a reference to parent Commit else Serialization will recursively serialize the entire commit tree! */
    private String parent;

    /** Mapping of file names to Blob SHAs
     * DO NOT USE HASH! Order is different each time it gets (de)serialized and it messes with the SHA-ing later
     * For hashing, order matters! Eg hash(abc) != hash(bac)
     * You won't see this bug if you track a single file
     * */
    private TreeMap<String, String> fileBlobs;


    /** Constructor */
    public Commit(String msg, Commit parent) {
        message = msg;
        author = System.getProperty("user.name");
        if (parent != null) {
            this.parent = sha1(serialize(parent));
            commitDate = new Date();
            update(parent.fileBlobs, true);
        } else {
            this.parent = "";
            commitDate = new Date(0);
            fileBlobs = new TreeMap<>();
        }
    }


    /** Returns the SHA */
    public String sha() {
        byte[] bytes = serialize(this);
        return sha1(bytes);
    }


    /** Read commit if any */
    public static Commit read(String sha) {
        File f = join(Repository.COMMIT_DIR, sha);
        if (f.exists()) {
            return readObject(f, Commit.class);
        }
        return null;
    }


    /** Write commit to directory. Returns SHA */
    public String write() {
        byte[] bytes = serialize(this);
        String sha = sha1(bytes);
        File f = join(Repository.COMMIT_DIR, sha);
        checkDuplicate(f, sha);
        writeContents(f, bytes);
        return sha;
    }


    /** Check duplicate commit */
    public static void checkDuplicate(File f, String sha) {
        if (f.exists()) {
            System.out.println("Commit already exists! " + sha);
            System.exit(0);
        }
    }


    /** Increase track count of blobs. Usually performed when commit is freshly-made, after being written */
    public void incrTracks() {
        for (Map.Entry<String, String> pair : fileBlobs.entrySet()) {
            String blobSha = pair.getValue();
            Blob b = Blob.read(blobSha);
            assert b != null;
            b.incrCount();
            b.write(false, true, blobSha);
        }
    }


    /** Is file being tracked? */
    public boolean tracked(String fileName) {
        return fileBlobs.containsKey(fileName);
    }


    /** Untrack files. Usually performed when commit is freshly-made, before being written and incrTracks() */
    public void untrack(HashSet<String> removes) {
        for (String fileName: removes) {
            fileBlobs.remove(fileName);
        }
    }


    /** Should Stage file? Stage if not tracked or different SHA */
    public boolean stage(String fileName, String blobSha) {
        return !blobSha.equals(fileBlobs.get(fileName));
    }


    /** Get blob of tracked file */
    public String getBlob(String fileName) {
        return fileBlobs.get(fileName);
    }


    /** Update the file-blob map */
    public void update(TreeMap<String, String> newMap, boolean replace) {
        if (replace) {
            fileBlobs = (TreeMap<String, String>) newMap.clone(); // shallow copy!
        } else {
            fileBlobs.putAll(newMap);
        }
    }


    /** Dump */
    public void dump() {
        System.out.println("=====START Dump Commit=====");
        System.out.println("Commit Message: " + message);
        System.out.println("Commit Date: " + commitDate);
        System.out.println("Commit Author: " + author);
        System.out.println("Commit Parent SHA: " + parent);
        System.out.println("Commit File-Blobs: " + fileBlobs);
        System.out.println("=====END Dump Commit=====");
    }
}
