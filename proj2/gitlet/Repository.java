package gitlet;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import static gitlet.Utils.*;


/** Represents a gitlet repository. Contains paths to various directories (commits, blobs etc)
 * Also responsible for running all the commands.
 *
 *  @author TODO
 */
public class Repository {
    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));

    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /** The commit directory */
    public static final File COMMIT_DIR = join(GITLET_DIR, "commit");

    /** The blob directory */
    public static final File BLOB_DIR = join(GITLET_DIR, "blob");

    /** The OG commit for all repos */
    public static final Commit initCommit = new Commit("initial commit", System.getProperty("user.name"), null);

    /** Current Commit */
    public static Commit HEAD;
    /** File containing current commit SHA */
    public static File HEADF = join(GITLET_DIR, "HEAD");

    /** Current branch */
    public static String branch;
    /** File containing current branch name */
    public static File branchF = join(GITLET_DIR, "branch");

    /** Map of branch name to its most recent commit */
//    public static HashMap<String, String> branches;

    /** File containing branches */
//    public static final File branchesF = join(GITLET_DIR, "branches");


    /** Initialize a .gitlet folder (repository) */
    public static void init() {
        GitletChecker.checkValidGitlet();

        // Create commit & blob directories and write initial commit
        GITLET_DIR.mkdir();
        BLOB_DIR.mkdir();
        COMMIT_DIR.mkdir();
        String headSha = initCommit.write();

        // Set branch name and HEAD commit
        HEAD = initCommit;
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
    private static void loadCurrent() {
//        branches = (HashMap<String, String>) readObject(branchesF, (new HashMap<String, String>()).getClass());
//        System.out.println(branches);

        HEAD = Commit.read(readContentsAsString(HEADF));
        branch = readContentsAsString(branchF);
    }


    /** Adds a file's changes to the staging area */
    public static void add(String[] args) {
        GitletChecker.checkInvalidGitlet();
        GitletChecker.checkOperands(args.length, 2);

        String fileName = args[1];
        File change = join(CWD, fileName);
        GitletChecker.checkFileExists(change);

        loadCurrent();
        Stager.setupStageArea();

        Blob candidate = new Blob(readContentsAsString(change));
        String sha = candidate.write(false);

        if (HEAD.stage(fileName, sha)) {
            Stager.addFile(fileName, sha);
        }
    }
}
