package gitlet;

import java.io.File;
import java.util.HashMap;

import static gitlet.Utils.*;

/** Helper class to manipulate staging area.
 *
 *  @author TODO
 */
public class Stager {
    /** Map of filenames to Blob SHAs to add/stage */
    public static HashMap<String, String> stageAdds;
    public static final File stageAddsF = join(Repository.GITLET_DIR, "stageAdds");


    /** Setup stage area */
    public static void setupStageArea() {
        if (!stageAddsF.exists()) {
            stageAdds = new HashMap<>();
            writeContents(stageAddsF, serialize(stageAdds));
        } else {
            stageAdds = (HashMap<String, String>) readObject(stageAddsF, (new HashMap<String, String>()).getClass());
        }
    }


    /** Add a file for staging. Also removes blobs that are not tracked and not staged */
    public static void addFile(String fileName, String sha) {
        String oldSha = stageAdds.remove(fileName);
        if (oldSha != null && !oldSha.equals(sha)) {
            Blob oldBlob = Blob.read(oldSha);
            if (oldBlob.getCount() == 0) {
                File old = join(Repository.BLOB_DIR, oldSha);
                old.delete();
            }
        }
        stageAdds.put(fileName, sha);
        if (!sha.equals(oldSha)) writeContents(stageAddsF, serialize(stageAdds));
    }
}
