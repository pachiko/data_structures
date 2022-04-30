package gitlet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static gitlet.Utils.*;

/** Helper class to manipulate staging area.
 *
 *  @author phill
 */
public class Stager {
    /** Map of filenames to Blob SHAs to add */
    public static TreeMap<String, String> stageAdds;
    public static final File stageAddsF = join(Repository.GITLET_DIR, "stageAdds");

    /** Map of filenames to Blob SHAs to remove */
    public static HashMap<String, String> stageRemoves;
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
            stageRemoves = new HashMap<>();
            writeContents(stageRemovesF, serialize(stageRemoves));
        } else {
            stageRemoves = (HashMap<String, String>) readObject(stageRemovesF, (new HashMap<String, String>()).getClass());
        }
    }


    /** Clear staging area */
    public static void clearStageArea() {
        stageAdds = null;
        if (stageAddsF.exists()) stageAddsF.delete();
        stageRemoves = null;
        if (stageRemovesF.exists()) stageRemovesF.delete();
    }


    /** Remove blob if not tracked. Usually done when adding modified versions of untracked files */
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


    /** Stage a tracked file for removal */
    public static void removeFile(String fileName) {
        if (BranchManager.HEAD.tracked(fileName)) {
            restrictedDelete(fileName);
            stageRemoves.put(fileName, BranchManager.HEAD.getBlob(fileName));
            writeContents(stageRemovesF, serialize(stageRemoves));
        }
    }


    /** Unstage file from staging area and removes the blob if untracked.
     * If file is staged for removal, put the file back into CWD */
    public static void unstageFile(String fileName) {
        if (stageAdds.containsKey(fileName)) {
            String oldSha = stageAdds.remove(fileName);
            removeUntrackedBlob(oldSha);
            writeContents(stageAddsF, serialize(stageAdds));
        } else if (stageRemoves.containsKey(fileName)) {
            String blobSha = stageRemoves.remove(fileName);
            Blob restore = Blob.read(blobSha);
            restore.putCWD(fileName);
            writeContents(stageRemovesF, serialize(stageRemoves));
        }
    }


    /** Print staging area (add/remove) */
    public static void printStageArea() {
        System.out.println("=== Staged Files ===");
        for (Map.Entry<String, String> entry : stageAdds.entrySet()) {
            System.out.println(entry.getKey());
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        for (Map.Entry<String, String> entry : stageRemoves.entrySet()) {
            System.out.println(entry.getKey());
        }
        System.out.println();
    }


    /** Prints unstaged & untracked files (Extra credit) */
    public static void printUnstagedUntracked() {
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();

        System.out.println("=== Untracked Files ===");
        System.out.println();
    }
}
