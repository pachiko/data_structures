package gitlet;

import java.io.File;

/** Helper class to check for files, SHAs and arguments.
 * Also prints the correct error message before exiting JAVA.
 *
 *  @author TODO
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
}
