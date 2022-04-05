package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable, Dumpable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. Cannot be NULL or empty! */
    private String message;

    /** Date of commit. Starts at the UNIX epoch (Jan 1 1970) for each initial commit */
    private Date commitDate;

    /** Commit author's name*/
    private String author;

    /** Parent commit in the form of SHA-1 hash
     * Do not keep a reference to parent Commit else Serialization will recursively serialize the entire commit tree! */
    private String parent;

    /** SHA of this commit */
    private String SHA;

    /** Mapping of file names to Blob SHAs */
    private HashMap<String, String> fileBlobs;


    /** Constructor */
    public Commit(String msg, String name, Commit parent) {
        commitDate = new Date(0);
        message = msg;
        author = name;
        if (parent != null) {
            this.parent = sha1(serialize(parent));
        } else {
            this.parent = "";
        }
        fileBlobs = new HashMap<>();

        byte[] serial = serialize(this);
        SHA = sha1(serial);
    }


    /** Get SHA */
    public String getSHA() { return SHA; }


    /** Write to file */
    public void write() {
        File f = join(Repository.COMMIT_DIR, SHA);
        if (f.exists()) {
            System.out.println("Commit already exists! " + SHA);
            System.exit(0);
        }
        writeContents(f, serialize(this));
    }

    /** Dump */
    public void dump() {
        System.out.println("=====START Dump Commit=====");
        System.out.println("Commit SHA: " + SHA);
        System.out.println("Commit Message: " + message);
        System.out.println("Commit Date: " + commitDate);
        System.out.println("Commit Author: " + author);
        System.out.println("Commit Parent SHA: " + parent);
        System.out.println("Commit File-Blobs: " + fileBlobs);
        System.out.println("=====END Dump Commit=====");
    }
}
