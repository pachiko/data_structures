package gitlet;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import static gitlet.Utils.*;


/** Helper class to manipulate branches and their commits.
 *
 *  @author TODO
 */
public class BranchManager {
    /** Current Commit */
    public static Commit HEAD;
    /** File containing current commit SHA */
    public static File HEADF = join(Repository.GITLET_DIR, "HEAD");

    /** Current branch */
    public static String branch;
    /** File containing current branch name */
    public static File branchF = join(Repository.GITLET_DIR, "branch");

    /** Map of branch name to its most recent commit. Needs to be lexicographic */
    public static TreeMap<String, String> branches;
    /** File containing branches */
    public static final File branchesF = join(Repository.GITLET_DIR, "branches");


    /** Initialize master branch with initial commit */
    public static void init() {
        String headSha = Repository.initCommit.write();

        // Set branch name and HEAD commit
        HEAD = Repository.initCommit;
        branch = "master";

        // Write branch name and HEAD commit SHA
        writeContents(HEADF, headSha);
        writeContents(branchF, branch);

        // Initialize and write branch-commit map
        branches = new TreeMap<>();
        branches.put(branch, headSha);
        writeContents(branchesF, serialize(branches));
    }


    /** Load current branch name and HEAD commit */
    public static String loadCurrent() {
        branches = (TreeMap<String, String>) readObject(branchesF, (new TreeMap<String, String>()).getClass());
        String sha = readContentsAsString(HEADF);
        HEAD = Commit.read(sha);
        branch = readContentsAsString(branchF);
        return sha;
    }


    /** Updates the HEAD commit with a new one for the current branch */
    public static void newCommit(String message) {
        Commit newCommit = new Commit(message, BranchManager.HEAD);
        newCommit.update(Stager.stageAdds, false);
        newCommit.untrack(Stager.stageRemoves);

        String newSha = newCommit.write();
        HEAD = newCommit;
        writeContents(HEADF, newSha);
        HEAD.incrTracks();

        branches.put(branch, newSha);
        writeContents(branchesF, serialize(branches));
    }


    /** Print branches, with an asterisk on the current branch */
    public static void printBranches() {
        System.out.println("=== Branches ===");
        for (Map.Entry<String, String> entry : branches.entrySet()) {
            String branchName = entry.getKey();
            if (branchName.equals(branch)) System.out.println("*" + branchName);
            else System.out.println(branchName);
        }
        System.out.println();
    }
}
