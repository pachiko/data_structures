package gitlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

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
    public static final Commit initCommit = new Commit("initial commit", null);

    /** Date formatter */
    private static final String pattern = "E MMM d HH:mm:ss yyyy Z";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);


    /** Initialize a .gitlet folder (repository) */
    public static void init() {
        GitletChecker.checkValidGitlet();

        // Create commit & blob directories and write initial commit
        GITLET_DIR.mkdir();
        BLOB_DIR.mkdir();
        COMMIT_DIR.mkdir();
        BranchManager.init();
    }


    /** Checks arguments and prepares for staging (add/remove) */
    private static void prepareStage(String[] args) {
        GitletChecker.checkInvalidGitlet();
        GitletChecker.checkOperands(args.length, 2);

        String fileName = args[1];
        File change = join(CWD, fileName);
        GitletChecker.checkFileExists(change);

        BranchManager.loadCurrent();
        Stager.setupStageArea();
    }


    /** Adds a file's changes to the staging area */
    public static void add(String[] args) {
        prepareStage(args);
        String fileName = args[1];
        File change = join(CWD, fileName);

        Blob candidate = new Blob(readContentsAsString(change));
        String sha = candidate.write(false,false, null);

        if (BranchManager.HEAD.stage(fileName, sha)) {
            Stager.addFile(fileName, sha); // Stage changes and removes old untracked blob
        } else { // HEAD tracks the same file as staged (edit, add, revert, add)
            Stager.unstageFile(fileName); // Unstage from staging area if present
        }
    }


    /** Remove staged file. Also stage it for removal and delete from working directory if HEAD tracks it */
    public static void remove(String[] args) {
        prepareStage(args);
        String fileName = args[1];

        Stager.unstageFile(fileName); // Unstage staged file and deletes untracked blob
        Stager.removeFile(fileName); // Remove from working directory and stage for removal
    }


    /** Commits staged changes to current branch */
    public static void commit(String[] args) {
        GitletChecker.checkInvalidGitlet();
        GitletChecker.checkOperands(args.length, 2);
        String message = args[1];
        GitletChecker.checkCommitMessage(message);

        BranchManager.loadCurrent();
        Stager.setupStageArea();
        GitletChecker.checkStagedChanges();

        BranchManager.newCommit(message);
        Stager.clearStageArea();
    }


    /** Prints commits from HEAD to init */
    public static void log() {
        GitletChecker.checkInvalidGitlet();

        String sha = BranchManager.loadCurrent();
        Commit current = BranchManager.HEAD;
        while (current != null) {
            printCommit(current, sha);
            sha = current.getParent();
            if (sha.isEmpty()) break;
            current = Commit.read(sha);
        }
    }


    /** Print all commits */
    public static void globalLog() {
        GitletChecker.checkInvalidGitlet();

        List<String> commits = plainFilenamesIn(COMMIT_DIR);
        for (String sha: commits) {
            Commit c = readObject(join(COMMIT_DIR, sha), Commit.class);
            printCommit(c, sha);
        }
    }


    /** Prints commit during logging */
    private static void printCommit(Commit c, String sha) {
        // TODO: commits have 2 parents!
        System.out.println("===");
        System.out.println("commit " + sha);
        System.out.println("Date: " + simpleDateFormat.format(c.getCommitDate()));
        System.out.println(c.getMessage());
        System.out.println();
    }


    /** Find all commits with a given message and print them */
    public static void find(String[] args) {
        GitletChecker.checkInvalidGitlet();
        GitletChecker.checkOperands(args.length, 2);
        String message = args[1];
        GitletChecker.checkCommitMessage(message);

        boolean found = false;
        List<String> commits = plainFilenamesIn(COMMIT_DIR);
        for (String sha: commits) {
            Commit c = readObject(join(COMMIT_DIR, sha), Commit.class);
            if (message.equals(c.getMessage())) {
                System.out.println(sha);
                found = true;
            }
        }

        if (!found) System.out.println("Found no commit with that message.");
    }


    /** Displays what branches currently exist, and marks the current branch with a *.
     *  Also displays what files have been staged for addition or removal. */
    public static void status() {
        GitletChecker.checkInvalidGitlet();
        BranchManager.loadAll();
        BranchManager.printBranches();
        Stager.setupStageArea();
        Stager.printStageArea();
        Stager.printUnstagedUntracked();
    }
}
