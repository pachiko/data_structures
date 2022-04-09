package gitlet;

import java.io.File;

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

    /** Map of branch name to its most recent commit */
//    public static HashMap<String, String> branches;

    /** File containing branches */
//    public static final File branchesF = join(GITLET_DIR, "branches");


    /** Initialize master branch with initial commit */
    public static void init() {
        String headSha = Repository.initCommit.write();

        // Set branch name and HEAD commit
        HEAD = Repository.initCommit;
        branch = "master";

        // Write branch name and HEAD commit SHA
        writeContents(HEADF, headSha);
        writeContents(branchF, branch);

//      // Initialize and write branch-commit map
//      branches = new HashMap<>();
//      branches.put(branch, HEADSHA); // SHA-ing branch names seems like overkill?
//      writeContents(branchesF, serialize(branches));
    }


    /** Load current branch name and HEAD commit */
    public static void loadCurrent() {
//        branches = (HashMap<String, String>) readObject(branchesF, (new HashMap<String, String>()).getClass());
//        System.out.println(branches);
        HEAD = Commit.read(readContentsAsString(HEADF));
        branch = readContentsAsString(branchF);
    }


    /** Updates the HEAD commit with a new one for the current branch */
    public static void newCommit(String message) {
        Commit newCommit = new Commit(message, BranchManager.HEAD);
        newCommit.update(Stager.stageAdds, false);
        String newSha = newCommit.write();
        HEAD = newCommit;
        writeContents(HEADF, newSha);
        HEAD.incrTracks();
    }
}
