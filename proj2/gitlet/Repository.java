package gitlet;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));

    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /** The commit directory */
    public static final File COMMIT_DIR = join(GITLET_DIR, "commit");

    /** The blob directory */
    public static final File BLOB_DIR = join(GITLET_DIR, "blob");

    /** The OG commit for all repos */
    public static final Commit initCommit = new Commit("initial commit",
            System.getProperty("user.name"), null);

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
        if (inGitlet()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        } else {
            // Create commit & blob directories and write initial commit
            GITLET_DIR.mkdir();
            BLOB_DIR.mkdir();
            COMMIT_DIR.mkdir();
            initCommit.write();

            // Set branch name and HEAD commit
            HEAD = initCommit;
            String HEADSHA = HEAD.getSHA();
            branch = "master";

            // Write branch name and HEAD commit SHA
            writeContents(HEADF, HEADSHA);
            writeContents(branchF, branch);

//            // Initialize and write branch-commit map
//            branches = new HashMap<>();
//            branches.put(branch, HEADSHA); // SHA-ing branch names seems like overkill?
//            writeContents(branchesF, serialize(branches));
        }
    }


    /** Is the user in a valid gitlet directory? */
    public static boolean inGitlet() {
        return GITLET_DIR.exists();
    }


    /** Load current branch name and HEAD commit */
    private static void loadCurrent() {
//        branches = (HashMap<String, String>) readObject(branchesF, (new HashMap<String, String>()).getClass());
//        System.out.println(branches);

        String HEADSHA = readContentsAsString(HEADF);
        HEAD = readObject(join(COMMIT_DIR, HEADSHA), Commit.class);

        branch = readContentsAsString(branchF);
    }


    /** Adds a file's changes to the staging area */
    public static void add(String[] args) {
        if (!inGitlet()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }

        File change = join(CWD, args[1]);
        if (!change.exists()) {
            System.out.println("File to add does not exist.");
            System.exit(0);
        }

        loadCurrent();

        String contents = readContentsAsString(change);
        String SHA = sha1(contents);

        File blob = join(BLOB_DIR, SHA);
        if (blob.exists()) { // TODO: check blob dir or check commit map? Both. blob dir for dup blobs, commit map for tracked blobs

        }
    }
}
