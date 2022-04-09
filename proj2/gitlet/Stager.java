package gitlet;

import java.io.File;
import java.util.HashSet;
import java.util.TreeMap;

import static gitlet.Utils.*;

/** Helper class to manipulate staging area.
 *
 *  @author TODO
 */
public class Stager {
    /** Map of filenames to Blob SHAs to add */
    public static TreeMap<String, String> stageAdds;
    public static final File stageAddsF = join(Repository.GITLET_DIR, "stageAdds");

    /** Map of filenames to Blob SHAs to remove */
    public static HashSet<String> stageRemoves;
    public static final File stageRemovesF = join(Repository.GITLET_DIR, "stageRemoves");


    /** Setup stage area */
    public static void setupStageArea() {
        if (!stageAddsF.exists()) {
            stageAdds = new TreeMap<>();
            writeContents(stageAddsF, serialize(stageAdds));
        } else {
            stageAdds = (TreeMap<String, String>) readObject(stageAddsF, (new TreeMap<String, String>()).getClass());
        }

        if (!stageRemovesF.exists()) {
            stageRemoves = new HashSet<>();
            writeContents(stageRemovesF, serialize(stageRemoves));
        } else {
            stageRemoves = (HashSet<String>) readObject(stageRemovesF, (new HashSet<String>()).getClass());
        }
    }


    /** Clear staging area */
    public static void clearStageArea() {
        stageAdds = null;
        stageAddsF.delete();
        stageRemoves = null;
        stageRemovesF.delete();
    }


    /** Remove blob if not tracked */
    public static void removeUntrackedBlob(String sha) {
        Blob blob = Blob.read(sha);
        if (blob.getCount() <= 0) {
            File old = join(Repository.BLOB_DIR, sha);
            old.delete();
        }
    }


    /** Add a file for staging. Also removes the replaced blob if untracked */
    public static void addFile(String fileName, String sha) {
        String oldSha = stageAdds.remove(fileName);
        boolean diffSha = !sha.equals(oldSha); // Same file already staged
        if (oldSha != null && diffSha) {
            removeUntrackedBlob(oldSha);
        }
        stageAdds.put(fileName, sha);
        if (diffSha) writeContents(stageAddsF, serialize(stageAdds));
    }


    /** Unstage file from staging area and removes the blob if untracked */
    public static void unstageFile(String fileName) {
        if (stageAdds.containsKey(fileName)) {
            String oldSha = stageAdds.remove(fileName);
            removeUntrackedBlob(oldSha);
            writeContents(stageAddsF, serialize(stageAdds));
        }
    }


    /** Stage a tracked file for removal */
    public static void removeFile(String fileName) {
        if (BranchManager.HEAD.tracked(fileName)) {
            restrictedDelete(fileName);
            stageRemoves.add(fileName);
            writeContents(stageRemovesF, serialize(stageRemoves));
        }
    }
}
