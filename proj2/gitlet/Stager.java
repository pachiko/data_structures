package gitlet;

import java.io.File;
import java.util.TreeMap;

import static gitlet.Utils.*;

/** Helper class to manipulate staging area.
 *
 *  @author TODO
 */
public class Stager {
    /** Map of filenames to Blob SHAs to add/stage */
    public static TreeMap<String, String> stageAdds;
    public static final File stageAddsF = join(Repository.GITLET_DIR, "stageAdds");


    /** Setup stage area */
    public static void setupStageArea() {
        if (!stageAddsF.exists()) {
            stageAdds = new TreeMap<>();
            writeContents(stageAddsF, serialize(stageAdds));
        } else {
            stageAdds = (TreeMap<String, String>) readObject(stageAddsF, (new TreeMap<String, String>()).getClass());
        }
    }


    /** Clear staging area */
    public static void clearStageArea() {
        stageAdds = null;
        stageAddsF.delete();
    }


    /** Remove blob if not tracked */
    public static void removeUntrackedBlob(String sha) {
        Blob blob = Blob.read(sha);
        if (blob.getCount() <= 0) {
            File old = join(Repository.BLOB_DIR, sha);
            old.delete();
        }
    }

    /** Add a file for staging. Also removes blobs that are not tracked and not staged */
    public static void addFile(String fileName, String sha) {
        String oldSha = stageAdds.remove(fileName);
        boolean diffSha = !sha.equals(oldSha); // Same file already staged
        if (oldSha != null && diffSha) {
            removeUntrackedBlob(oldSha);
        }
        stageAdds.put(fileName, sha);
        if (diffSha) writeContents(stageAddsF, serialize(stageAdds));
    }


    /** Remove file from staging area */
    public static void removeFile(String fileName) {
        if (stageAdds.containsKey(fileName)) {
            stageAdds.remove(fileName);
            writeContents(stageAddsF, serialize(stageAdds));
        }
    }
}
