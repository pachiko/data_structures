package gitlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import static gitlet.Utils.*;


/** Represents a gitlet repository. Contains paths to various directories (commits, blobs etc)
 * Also responsible for running all the commands.
 *
 *  @author phill
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
    public static final Commit initCommit = new Commit("initial commit", null, null);

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


    /** Checks arguments and prepares for add */
    private static void prepareAdd(String[] args) {
        GitletChecker.checkInvalidGitlet();
        GitletChecker.checkOperands(args.length, 2);

        BranchManager.loadCurrent();
        Stager.setupStageArea();

        String fileName = args[1];
        File change = join(CWD, fileName);
        GitletChecker.checkValidAdd(fileName, change);
    }


    /** Adds a file's changes to the staging area */
    public static void add(String[] args) {
        prepareAdd(args);
        String fileName = args[1];
        actualAdd(fileName);
    }


    /** Actual add operation */
    public static void actualAdd(String fileName) {
        File change = join(CWD, fileName);
        boolean shouldUnstage = true;
        if (change.exists()) {
            Blob candidate = new Blob(readContentsAsString(change));
            String sha = candidate.write(false, false, null);
            shouldUnstage = actualAdd(fileName, sha);
        }
        if (shouldUnstage) Stager.unstageFile(fileName);
    }


    /** Actual add using Blob SHA */
    public static boolean actualAdd(String fileName, String sha) {
        if (BranchManager.HEAD.stage(fileName, sha)) { // not tracked or diff version
            Stager.addFile(fileName, sha); // Stage changes and removes old untracked blob
            return false;
        }
        return true;
    }


    /** Checks arguments and prepares for remove */
    private static void prepareRemove(String[] args) {
        GitletChecker.checkInvalidGitlet();
        GitletChecker.checkOperands(args.length, 2);

        String fileName = args[1];
        File change = join(CWD, fileName);
        GitletChecker.checkFileExists(change);

        BranchManager.loadCurrent();
        Stager.setupStageArea();
    }


    /** Remove staged file. Also stage it for removal and delete from working directory if HEAD tracks it */
    public static void remove(String[] args) {
        prepareRemove(args);
        String fileName = args[1];
        GitletChecker.checkValidRemove(fileName);
        actualRemove(fileName);
    }


    /** Actual remove operation */
    public static void actualRemove(String fileName) {
        Stager.unstageFile(fileName); // Unstage staged file and deletes untracked blob
        Stager.removeFile(fileName); // Remove from working directory and stage for removal
    }


    /** Prepare to commit */
    private static void prepareCommit(String[] args) {
        GitletChecker.checkInvalidGitlet();
        GitletChecker.checkOperands(args.length, 2);
        String message = args[1];
        GitletChecker.checkCommitMessage(message);
        BranchManager.loadCurrent();
        Stager.setupStageArea();
    }


    /** Commits staged changes to current branch */
    public static void commit(String[] args) {
        prepareCommit(args);
        String message = args[1];
        actualCommit(message, null);
    }


    /** Actual commit operation */
    public static void actualCommit(String message, Commit merge) {
        GitletChecker.checkStagedChanges();
        BranchManager.newCommit(message, merge);
        Stager.clearStageArea();
    }


    /** Prints commits from HEAD to init. Ignores merge parents */
    public static void log() {
        GitletChecker.checkInvalidGitlet();

        String sha = BranchManager.loadCurrent();
        Commit current = BranchManager.HEAD;
        while (current != null) {
            printCommit(current, sha);
            sha = current.getParent();
            if (sha == null) break;
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
        System.out.println("===");
        System.out.println("commit " + sha);
        String mp = c.getMergeParent();
        if (mp != null) {
            System.out.println("Merge: " + c.getParent().substring(0, 7) + " " + mp.substring(0, 7));
        }
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


    /** Checkout files from a commit (or from HEAD if unspecified). Can also checkout an entire branch */
    public static void checkout(String[] args) {
        int count = args.length;
        GitletChecker.checkInvalidGitlet();
        switch (count) {
            case 2 -> BranchManager.checkoutBranch(args[1]); // checkout [branch name]
            case 3 -> checkoutFile(args[2], (String) null); // checkout -- [file name]
            case 4 -> checkoutFile(args[3], args[1]); // checkout [commit id] -- [file name]
            default -> GitletChecker.checkOperands(0, 1);
        }
    }


    /** Checkout file from commit (HEAD if unspecified) */
    public static void checkoutFile(String fileName, String commitId) {
        Commit com;

        if (commitId == null) {
            BranchManager.loadCurrent();
            com = BranchManager.HEAD;
        } else {
            if (commitId.length() == 40) com = Commit.read(commitId);
            else com = findCommit(commitId);
        }

        checkoutFile(fileName, com);
    }

    /** Checkout file using Commit object */
    public static void checkoutFile(String fileName, Commit com) {
        GitletChecker.checkTrackedFile(com, fileName);

        String blobSha = com.getBlob(fileName);
        Blob b = Blob.read(blobSha);
        b.putCWD(fileName);
    }


    /** Find commit based on commitID (first 6 digits) */
    public static Commit findCommit(String commitId) {
        List<String> commits = plainFilenamesIn(COMMIT_DIR);
        int l = commitId.length();
        if (l >= 6) { // must have at least 6 chars for abbreviated commit ID
            for (String c : commits) {
                if (commitId.equals(c.substring(0, l))) {
                    return Commit.read(c);
                }
            }
        }
        System.out.println("No commit with that id exists.");
        System.exit(0);
        return null;
    }


    /** Creates new branch from current commit, but doesn't checkout */
    public static void branch(String[] args) {
        GitletChecker.checkInvalidGitlet();
        GitletChecker.checkOperands(args.length, 2);
        String branchName = args[1];
        GitletChecker.checkDuplicateBranch(branchName);
        BranchManager.newBranch(branchName);
    }


    /** Remove an existing branch, provided HEAD is not on the branch to be removed
     * Does not remove commits of the branch, even if those commits are not tracked by any branch
     * In real Git, it is the same so that reflog can recover those commits (up to 30 days) */
    public static void rmBranch(String[] args) {
        GitletChecker.checkInvalidGitlet();
        GitletChecker.checkOperands(args.length, 2);
        String branchName = args[1];
        BranchManager.loadCurrent();
        GitletChecker.checkValidBranch(branchName, true);
        BranchManager.removeBranch(branchName);
    }


    /** Reset to a commit */
    public static void reset(String[] args) {
        GitletChecker.checkInvalidGitlet();
        GitletChecker.checkOperands(args.length, 2);
        String commitId = args[1];
        Commit com = findCommit(commitId);
        BranchManager.loadCurrent();
        GitletChecker.checkUntrackedFiles();

        Stager.clearStageArea();
        com.updateCWD();
        writeContents(join(BranchManager.BRANCH_DIR, BranchManager.branch), com.sha());
    }


    /** Merge two branches */
    public static void merge(String[] args) {
        GitletChecker.checkInvalidGitlet();
        GitletChecker.checkOperands(args.length, 2);

        String mergeBranch = args[1];
        String mainSHA = BranchManager.loadCurrent();
        GitletChecker.checkValidBranch(mergeBranch, false);
        String mergeSHA = readContentsAsString(join(BranchManager.BRANCH_DIR, mergeBranch));
        Commit merge = Commit.read(mergeSHA);

        GitletChecker.checkUntrackedFiles();
        Stager.setupStageArea();
        GitletChecker.checkUncommittedChanges();

        SplitPointFinder finder = new SplitPointFinder(mainSHA, mergeSHA, BranchManager.HEAD, merge);
        Commit ancestor = finder.findSplitPoint();

        Merger merger = new Merger(ancestor, BranchManager.HEAD, merge);
        merger.merge();

        actualCommit("Merged " + mergeBranch + " into " + BranchManager.branch + ".", merge);
    }
}
