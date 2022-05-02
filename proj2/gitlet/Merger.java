package gitlet;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Performs a merge using a split point commit and two branches
 *
 *  @author phill
 * */
public class Merger {
    /** Split point */
    Commit split;

    /** Main commit */
    Commit main;

    /** Merge commit */
    Commit merge;


    /** Constructor */
    public Merger(Commit split, Commit main, Commit merge) {
        this.split = split;
        this.main = main;
        this.merge = merge;
    }


    /** Merge. Manipulates staging area. Not responsible for making Commit */
    public void merge() {
        Map<String, String> mainFiles = main.getFileBlobs();
        Map<String, String> mergeFiles = merge.getFileBlobs();
        Map<String, String> splitFiles = split.getFileBlobs();

        boolean hasConflict = false;

        for (String file: allFiles(mainFiles, mergeFiles, splitFiles)) {
            String splitSHA = splitFiles.get(file);
            String mergeSHA = mergeFiles.get(file);
            String mainSHA = mainFiles.get(file);

            FileState mergeSplit = checkState(mergeSHA, splitSHA);
            FileState mainSplit = checkState(mainSHA, splitSHA);
            FileState mergeMain = checkState(mergeSHA, mainSHA);

            if (hasPresenceConflict(mergeSplit, mainSplit) || hasContentConflict(mergeSplit, mainSplit, mergeMain)) {
                hasConflict = true;
                createConflict(mainSHA, mergeSHA, file);
            } else if (hasMergeChanges(mergeSplit, mainSplit)) {
                mergeChanges(mergeSplit, file);
            }
        }

        if (hasConflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }


    /** Get all files from 3 commits */
    private  Set<String> allFiles(Map<String, String> mainFiles, Map<String, String> mergeFiles,
                                  Map<String, String> splitFiles) {
        Set<String> allFiles = new HashSet<>();
        allFiles.addAll(mainFiles.keySet());
        allFiles.addAll(mergeFiles.keySet());
        allFiles.addAll(splitFiles.keySet());
        return allFiles;
    }


    /** Checks for presence conflict */
    private boolean hasPresenceConflict(FileState mergeSplit, FileState mainSplit) {
        return (mergeSplit == FileState.REMOVED && mainSplit == FileState.MODIFIED)
                || (mergeSplit == FileState.MODIFIED && mainSplit == FileState.REMOVED);
    }


    /** Checks for content conflict */
    private boolean hasContentConflict(FileState mergeSplit, FileState mainSplit, FileState mergeMain) {
        return mergeMain == FileState.MODIFIED && ((mergeSplit == FileState.NEW && mainSplit == FileState.NEW)
                || (mergeSplit == FileState.MODIFIED && mainSplit == FileState.MODIFIED));
    }


    /** Create a presence or content conflict for a file */
    private void createConflict(String mainSHA, String mergeSHA, String file) {
        Blob mainBlob = (mainSHA == null)? new Blob(null) : Blob.read(mainSHA);
        Blob mergeBlob = (mergeSHA == null)? new Blob(null) : Blob.read(mergeSHA);
        Blob resBlob = mainBlob.mergeConflict(mergeBlob);
        String sha = resBlob.write(true, false, null);
        resBlob.putCWD(file);
        Repository.actualAdd(file, sha);
    }


    /** The merge branch has changes whereas the main branch does not */
    private boolean hasMergeChanges(FileState mergeSplit, FileState mainSplit) {
        return (mergeSplit == FileState.REMOVED || mergeSplit == FileState.NEW || mergeSplit == FileState.MODIFIED)
                && mainSplit == FileState.SAME;
    }


    /** Merge changes from merge branch */
    private void mergeChanges(FileState mergeSplit, String file) {
        if (mergeSplit == FileState.NEW || mergeSplit == FileState.MODIFIED) {
            Repository.checkoutFile(file, merge);
            Repository.actualAdd(file);
        } else if (mergeSplit == FileState.REMOVED) {
            Repository.actualRemove(file);
        } else {
            System.out.println("Unknown mergeChange: " + mergeSplit);
        }
    }


    /** Checks file state given two SHAs */
    private FileState checkState( String SHA, String refSHA) {
        if (refSHA != null && SHA == null) {
            return FileState.REMOVED;
        } else if (refSHA == null && SHA != null) {
            return FileState.NEW;
        } else {
            if (refSHA == null) return FileState.SAME;
            return refSHA.equals(SHA) ? FileState.SAME : FileState.MODIFIED;
        }
    }


    /** Indicates state of a file wrt to split commit. Usually used by main or merge commits */
    enum FileState {
        NEW,
        REMOVED,
        MODIFIED,
        SAME
    }
}
