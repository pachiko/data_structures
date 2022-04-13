package gitlet;

import java.io.File;
import java.util.List;

import static gitlet.Utils.join;
import static gitlet.Utils.plainFilenamesIn;

/** Helper class to check for files, SHAs and arguments.
 * Also prints the correct error message before exiting JAVA.
 *
 *  @author phill
 */
public class GitletChecker {
    /** Check Valid Gitlet directory */
    public static void checkValidGitlet() {
        if (inGitlet()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
    }


    /** Check Invalid Gitlet directory */
    public static void checkInvalidGitlet() {
        if (!inGitlet()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }


    /** Is the user in a valid gitlet directory? */
    public static boolean inGitlet() {
        return Repository.GITLET_DIR.exists();
    }


    /** Check valid operands count */
    public static void checkOperands(int count, int expected) {
        if (count != expected) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }


    /** Check file existence */
    public static void checkFileExists(File f) {
        if (!f.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
    }

    /** Check if the file can be added (can unstage from removed/file exists in CWD) */
    public static void checkValidAdd(String fileName, File f) {
        if (!Stager.stageRemoves.containsKey(fileName) && !f.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
    }

    /** Check if the file has a reason to remove (ie staged/tracked) */
    public static void checkValidRemove(String fileName) {
         if (!Stager.stageAdds.containsKey(fileName) && !BranchManager.HEAD.tracked(fileName)) {
             System.out.println("No reason to remove the file.");
             System.exit(0);
         }
    }


    /** Check for staged changes before committing */
    public static void checkStagedChanges() {
        if (Stager.stageAdds.isEmpty() && Stager.stageRemoves.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
    }


    /** Check commit message (ie not empty not null) */
    public static void checkCommitMessage(String s) {
        if (s == null || s.isEmpty()) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
    }


    /** Check if file is tracked in commit */
    public static void checkTrackedFile(Commit com, String fileName) {
        if (!com.tracked(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
    }

    /** Check same branch during checkout */
    public static void checkSameBranch(String branchName) {
        if (branchName.equals(BranchManager.branch)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
    }


    /** Check if branch exists */
    public static void checkBranchExists(String branchName) {
        File f = join(BranchManager.BRANCH_DIR, branchName);
        if (!f.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
    }


    /** See if any untracked files in CWD */
    public static void checkUntrackedFiles() {
        List<String> workFileList = plainFilenamesIn(Repository.CWD);
        for (String workFile : workFileList) {
            if (!BranchManager.HEAD.tracked(workFile)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }
    }


    /** Check if branch is duplicate */
    public static void checkDuplicateBranch(String branchName) {
        File f = join(BranchManager.BRANCH_DIR, branchName);
        if (f.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
    }
}
