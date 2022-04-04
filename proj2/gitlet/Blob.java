package gitlet;


import java.io.File;
import java.io.Serializable;

/** Represents a gitlet blob object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Blob implements Serializable {
    /** The filename represented by this blob */
    File fileName;

    /** Creates a blob with a given filename */
    public Blob(String fName) {
        fileName = new File(fName);
    }

    public void add() {

    }
}
