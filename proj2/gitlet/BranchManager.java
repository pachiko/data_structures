package gitlet;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static gitlet.Utils.*;
import static gitlet.Utils.readContentsAsString;


/** Helper class to manipulate branches and their commits.
 *
 *  @author TODO
 */
public class BranchManager {
    /** Current Commit */
    public static Commit HEAD;

    /** Current branch */
    public static String branch;
    /** File containing current branch name */
    public static File branchF = join(Repository.GITLET_DIR, "branch");

    /** Map of branch name to its most recent commit. Needs to be lexicographic */
    public static TreeMap<String, String> branches;
    /** Directory containing branches */
    public static final File BRANCH_DIR = join(Repository.GITLET_DIR, "branches");


    /** Initialize master branch with initial commit */
    public static void init() {
        String headSha = Repository.initCommit.write();

        // Set branch name and HEAD commit
        HEAD = Repository.initCommit;
        branch = "master";

        // Write current branch name to file
        writeContents(branchF, branch);

        // Initialize branch folder and write HEAD in a file with current branch name
        BRANCH_DIR.mkdir();
        writeContents(join(BRANCH_DIR, branch), headSha);
    }


    /** Load current branch name and HEAD commit */
    public static String loadCurrent() {
        branch = readContentsAsString(branchF);
        String sha = readContentsAsString(join(BRANCH_DIR, branch));
        HEAD = Commit.read(sha);
        return sha;
    }


    /** Load all branches and their latest commit SHAs into the map */
    public static void loadAll() {
        loadCurrent();
        branches = new TreeMap<>();
        List<String> branchFileList = plainFilenamesIn(BRANCH_DIR);
        for (String branchFile : branchFileList) {
            String sha = readContentsAsString(join(BRANCH_DIR, branchFile));
            branches.put(branchFile, sha);
        }
    }


    /** Updates the HEAD commit with a new one for the current branch */
    public static void newCommit(String message) {
        Commit newCommit = new Commit(message, BranchManager.HEAD);
        newCommit.update(Stager.stageAdds, false);
        newCommit.untrack(Stager.stageRemoves);

        String newSha = newCommit.write();
        HEAD = newCommit;
        writeContents(join(BRANCH_DIR, branch), newSha);
        HEAD.incrTracks();
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
