package gitlet;

import java.io.File;

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


    /** Initialize a .gitlet folder (repository) */
    public static void init() {
        GitletChecker.checkValidGitlet();

        // Create commit & blob directories and write initial commit
        GITLET_DIR.mkdir();
        BLOB_DIR.mkdir();
        COMMIT_DIR.mkdir();
        BranchManager.init();
    }


    /** Adds a file's changes to the staging area */
    public static void add(String[] args) {
        GitletChecker.checkInvalidGitlet();
        GitletChecker.checkOperands(args.length, 2);

        String fileName = args[1];
        File change = join(CWD, fileName);
        GitletChecker.checkFileExists(change);

        BranchManager.loadCurrent();
        Stager.setupStageArea();

        Blob candidate = new Blob(readContentsAsString(change));
        String sha = candidate.write(false,false, null);

        if (BranchManager.HEAD.stage(fileName, sha)) {
            Stager.addFile(fileName, sha); // Stage changes and removes old untracked blob
        } else {
            Stager.removeFile(fileName); // Remove from staging area if present
        }
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
}
