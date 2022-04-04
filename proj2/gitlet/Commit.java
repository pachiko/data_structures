package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
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

    public Commit(String msg, String name, Commit parent) {
        commitDate = new Date(0);
        message = msg;
        author = name;
        if (parent != null) {
            this.parent = sha1(serialize(parent));
        } else {
            this.parent = "";
        }
    }

    public void write() {
        byte[] serial = serialize(this);
        String SHA = sha1(serial);
        // TODO: Check if commit exists?
        File f = join(Repository.COMMIT_DIR, SHA);
//        writeObject(f, this);
        writeContents(f, serial);
    }
}
