package gitlet;


import java.io.File;
import java.io.Serializable;

/** Represents a gitlet blob object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Blob implements Serializable, Dumpable {
    /** The filename represented by this blob */
    private File fileName;

    /** SHA of this blob */
    private String SHA;

    /** Contents of this blob */
    private String contents;

    /** Creates a blob with a given filename */
    public Blob(String fName) {
        fileName = new File(fName);
    }

    public void add() {

    }

    /** Dump */
    public void dump() {
        System.out.println("=====START Dump Blob=====");
        System.out.println("Blob Filename: " + fileName);
        System.out.println("=====END Dump Blob=====");
    }
}
