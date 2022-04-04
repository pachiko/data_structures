package gitlet;

import java.io.File;
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

    /** The OG commit for all repos */
    public static final Commit initCommit = new Commit("initial commit",
            System.getProperty("user.name"), null);

    /** Current Commit */
    public static Commit HEAD;

    /** Current branch */
    public static String branch;

    /** TreeMap for branch name to its most recent commit */
    public static TreeMap<String, Commit> branches;

    /** File containing branches */
    public static final File branchF = join(GITLET_DIR, "branches");


    /** Initialize a .gitlet folder (repository) */
    public static void init() {
        if (inGitlet()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        } else {
            GITLET_DIR.mkdir();
            COMMIT_DIR.mkdir();
            initCommit.write();

//            HEAD = initCommit;
//            branch = "master";
//            branches = new TreeMap<>();
//            branches.put(branch, HEAD);

//            writeContents(branchF, serialize(branches)); // dont do this
        }
    }

    /** Is the user in a valid gitlet directory? */
    public static boolean inGitlet() {
        return GITLET_DIR.exists();
    }

    public static void createBranch(String name) {

    }

    /** Adds a file's changes to the staging area */
    public static void add(String[] args) {
        if (!inGitlet()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }

        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            return;
        }

        File change = join(CWD, args[1]);
        if (!change.exists()) {
            System.out.println("File to add does not exist!");
            return;
        }

//        String contents = readContentsAsString(change);
//        String sha1 = sha1(contents);
    }
}
