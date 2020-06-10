package gitlet;

import java.io.Serializable;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;

/** Gitletty object class for Gitlet, the tiny stupid version-control system.
 *  This class represents the gitlet object itself.
 *  @author Vicky Yu but please note
 *  I received help from other people during project parties and
 *  looking at piazza posts as well as office hours for the data
 *  structures and choices of instance variables that I chose to use,
 *  but I don't know their names to write down as citations :-(
 *  I also looked at geeksforgeeks.org to get a better understanding
 *  of HashMap methods and javatpoint.com to understand more
 *  about dates. */
public class Gitletty implements Serializable {

    /** The staging area of gitletty. */
    private StagingArea _stagingarea;

    /** The current branch's name. */
    private String _branchCurr;

    /** The head commit's SHA-id. */
    private String _headSHA;

    /** A list of all the current commits, using the
     * SHA-id to differentiate each commit. */
    private ArrayList<String> _allCommits;

    /** The branch's representation. The hashmap's key is the
     * branch's name. The value is the commit's SHA-id. */
    private HashMap<String, String> _branches;

    /** Initializes the Gitliet. */
    public Gitletty() {
        Commit initGit = new Commit("initial commit", new Date(0),
                null, new HashMap<String, String>());
        _stagingarea = new StagingArea();
        _stagingarea.setStageHead(initGit);
        byte[] serialized = Utils.serialize(initGit);
        String shaID = Utils.sha1((Object) serialized);
        _headSHA = shaID;
        _allCommits = new ArrayList<String>();
        _allCommits.add(shaID);
        _branches = new HashMap<String, String>();
        _branches.put("master", shaID);
        _branchCurr = "master";
        commiterSaver(shaID, initGit);
    }

    /** Adds the file to the staging area.
     * @param file which is the name of the file. */
    public void add(String file) {
        _stagingarea.addFile(file);
    }

    /** Save the commit into the commit directory.
     * @param sha the sha id of the commit
     * @param com the commit. */
    public void commiterSaver(String sha, Commit com) {
        File commitFile = new File(".gitlet/commits/" + sha);
        Utils.writeObject(commitFile, com);
    }

    /** Gets the commit that corresponds to the SHA.
     * @param shaID the commit's SHA-id
     * @return the commit */
    public static Commit getCommit(String shaID) {
        File commit = new File(".gitlet/commits/" + shaID);
        Commit toRet = Utils.readObject(commit, Commit.class);
        return toRet;
    }

    /** Commits the file from the staging area.
     * @param msg which is the commit's message */
    public void commit(String msg) {
        Commit toAdd = _stagingarea.commitFile(msg);
        if (toAdd == null) {
            return;
        }
        Object commitSerial = Utils.serialize(toAdd);
        String commitSHA = Utils.sha1(commitSerial);
        commiterSaver(commitSHA, toAdd);
        _headSHA = commitSHA;
        _allCommits.add(commitSHA);
        _branches.put(_branchCurr, commitSHA);
    }

    /** Gets the latest common ancestor of this commit
     * with the given commit.
     * @param given the given commit
     * @param curr the current commit
     * @return Commit their latest common ancestor */
    public static Commit commonAncestor(Commit curr, Commit given) {
        Commit currentCom = curr;
        Commit givenCom = given;
        HashMap<Commit, Integer> currAncs = new HashMap<Commit, Integer>();
        ArrayList<Commit> givAncest = new ArrayList<Commit>();
        HashMap<Commit, Integer> commonAnc = new HashMap<Commit, Integer>();
        int count = 0;
        currAncs.put(currentCom, count);
        givAncest.add(givenCom);
        while (currentCom.getParent() != null) {
            count++;
            if (!currentCom.getMergedParents().isEmpty()) {
                for (String each : currentCom.getMergedParents()) {
                    if (!currAncs.containsKey(getCommit(each))) {
                        currAncs.put(getCommit(each), count);
                    }
                }
                count--;
            }
            count++;
            currentCom = getCommit(currentCom.getParent());
            currAncs.put(currentCom, count);
        }
        while (givenCom.getParent() != null) {
            if (!givenCom.getMergedParents().isEmpty()) {
                for (String each : givenCom.getMergedParents()) {
                    if (!givAncest.contains(getCommit(each))) {
                        givAncest.add(getCommit(each));
                    }
                }
            }
            givenCom = getCommit(givenCom.getParent());
            givAncest.add(givenCom);
        }
        for (Commit eachAnc : currAncs.keySet()) {
            HashMap<String, String> blob = eachAnc.getBlobber();
            int value = currAncs.get(eachAnc);
            for (Commit givAnc : givAncest) {
                HashMap<String, String> blob2 = givAnc.getBlobber();
                if (blob.equals(blob2)) {
                    commonAnc.put(eachAnc, value);
                }
            }
        }
        Commit toret = null;
        int i = 0;
        int[] nums = new int[commonAnc.size()];
        for (Commit each : commonAnc.keySet()) {
            int distance = commonAnc.get(each);
            nums[i] = distance;
            i++;
        }
        Arrays.sort(nums);
        for (Commit each : commonAnc.keySet()) {
            if (commonAnc.get(each) == nums[0]) {
                toret = each;
            }
        }
        return toret;
    }

    /** Merges files from the given branch into the
     * current branch.
     * @param branchName the branch's name */
    public void merge(String branchName) {
        if (!_stagingarea.getforAddition().isEmpty()
            || !_stagingarea.getforRemoval().isEmpty()) {
            System.out.println("You have uncomitted changes");
            return;
        }
        if (!_branches.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (_branchCurr.equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        String currBranch = _branchCurr;
        String headOrigin = _headSHA;
        String branchOrigin = _branches.get(branchName);
        Commit currCom = getCommit(_headSHA);
        HashMap<String, String> curblobs = currCom.getBlobber();
        Commit givenCom = getCommit(_branches.get(branchName));
        HashMap<String, String> bblobs = givenCom.getBlobber();
        List<String> currFiles = Utils.plainFilenamesIn("./");
        for (String file : bblobs.keySet()) {
            if (curblobs.get(file) == null
                && currFiles.contains(file)) {
                System.out.println("There is an untracked file in the"
                        + " way; delete it, or add and commit it first.");
                return;
            }
        }
        Commit splitPoint = commonAncestor(currCom, givenCom);
        if (splitPoint.getBlobber().equals(givenCom.getBlobber())) {
            System.out.println("Given branch is an ancestor of "
                    + "the current branch.");
            return;
        }
        if (splitPoint.getBlobber().equals(currCom.getBlobber())) {
            checkoutThree(branchName);
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        boolean conflict = mergeModified(currCom, givenCom, splitPoint);
        mergeCommit(conflict, branchOrigin, headOrigin, branchName, currBranch);
    }

    /** Checks the contents of all the commits, and compares
     * contents and deals with the conditions for modifications.
     * @param cur the current commit (head commit)
     * @param giv the given commit (branch commit)
     * @param split the split point (latest common ancestor)
     * @return whether or not there's conflict*/
    public boolean mergeModified(Commit cur, Commit giv, Commit split)  {
        HashMap<String, String> curBlobber = cur.getBlobber();
        HashMap<String, String> givBlobber = giv.getBlobber();
        HashMap<String, String> splitBlobber = split.getBlobber();
        ArrayList<String> important = new ArrayList<>();
        for (String file : splitBlobber.keySet()) {
            if (!important.contains(file)) {
                important.add(file);
            }
        }
        for (String file : givBlobber.keySet()) {
            if (!important.contains(file)) {
                important.add(file);
            }
        }
        for (String file : curBlobber.keySet()) {
            if (!important.contains(file)) {
                important.add(file);
            }
        }
        List<String> cwdFiles = Utils.plainFilenamesIn("./");
        important = mergePresent(cur, giv, split, important);
        if (important.isEmpty()) {
            return false;
        } else {
            for (String fileName : important) {
                String curContents = curBlobber.get(fileName);
                String givContents = givBlobber.get(fileName);
                String splitContents = splitBlobber.get(fileName);
                if (splitContents != null
                        && !splitContents.equals(givContents)
                        && splitContents.equals(curContents)) {
                    overwriteBlob(giv, fileName);
                    add(fileName);
                    important.remove(fileName);
                }
                if (splitContents != null
                        && splitContents.equals(givContents)
                        && !splitContents.equals(curContents)) {
                    important.remove(fileName);
                }
                if (splitContents != null
                        && curContents.equals(givContents)) {
                    if (cwdFiles.contains(fileName)) {
                        important.remove(fileName);
                    }
                }
                if (!curContents.equals(givContents)) {
                    mergeConflict(fileName, curContents, givContents);
                    return true;
                }
            }
        }
        return false;
    }

    /** Checks the contents of all the commits, and compares
     * contents and deals with the conditions for presence.
     * @param cur the current commit (head commit)
     * @param giv the given commit (branch commit)
     * @param split the split point (latest common ancestor)
     * @param files that we still need to check for
     * @return list of remaining files we need to traverse */
    public ArrayList<String> mergePresent(Commit cur, Commit giv, Commit split,
                                          ArrayList<String> files) {
        HashMap<String, String> curBlobber = cur.getBlobber();
        HashMap<String, String> givBlobber = giv.getBlobber();
        HashMap<String, String> splitBlobber = split.getBlobber();
        ArrayList<String> toRemov = new ArrayList<>();
        for (String fileName : files) {
            String curContents = curBlobber.get(fileName);
            String givContents = givBlobber.get(fileName);
            String splitContents = splitBlobber.get(fileName);
            if (splitContents == null && givContents == null
                && curContents != null) {
                toRemov.add(fileName);
            }
            if (splitContents == null && curContents == null
                && givContents != null) {
                String branch = "";
                for (String branchName : _branches.keySet()) {
                    Commit compare = getCommit(_branches.get(branchName));
                    if (compare.getBlobber().equals(giv.getBlobber())) {
                        branch = branchName;
                    }
                }
                overwriteBlob(giv, fileName);
                add(fileName);
                toRemov.add(fileName);
            }
            if (splitContents != null && splitContents.equals(curContents)
                && givContents == null) {
                remove(fileName);
                toRemov.add(fileName);
            }
            if (splitContents != null && splitContents.equals(givContents)
                && curContents == null) {
                toRemov.add(fileName);
            }
        }
        for (String fileName : toRemov) {
            files.remove(fileName);
        }
        return files;
    }

    /** Checks the contents of all the commits, and compares
     * contents and deals with the conditions for presence.
     * @param cur the current commit's contents (SHA) (head commit)
     * @param giv the given commit's contents (SHA) (branch commit)
     * @param file that is in conflict*/
    public void mergeConflict(String file, String cur, String giv) {
        String curCon = "";
        String givenCon = "";
        if (cur != null) {
            File curF = new File(".gitlet/blobbers/" + cur);
            curCon = Utils.readContentsAsString(curF);
        }
        if (giv != null) {
            File givenF = new File(".gitlet/blobbers/" + giv);
            givenCon = Utils.readContentsAsString(givenF);
        }
        File towrite = new File(file);
        String contents = "<<<<<<< HEAD\n" + curCon + "=======\n"
                + givenCon + ">>>>>>>\n";
        Utils.writeContents(towrite, contents);
        add(file);
    }

    /** Special commit for merges.
     * @param conflict whether or not there was conflict
     * @param ogBranch the original branch sha
     * @param ogHead the original head sha
     * @param branchName the given branch name
     * @param currBranch current branch name */
    public void mergeCommit(boolean conflict, String ogBranch, String ogHead,
                            String branchName, String currBranch) {
        String msg = "Merged " + branchName + " into " + currBranch + ".";
        Commit merged = _stagingarea.commitMerge(msg, ogBranch, ogHead);
        if (merged == null) {
            return;
        }
        String mergedSHA = Utils.sha1((Object) Utils.serialize(merged));
        _branches.put(_branchCurr, mergedSHA);
        _allCommits.add(mergedSHA);
        commiterSaver(mergedSHA, merged);
        _headSHA = mergedSHA;
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    /** Prints the logs of the previous commits. */
    public void log() {
        String ref = _headSHA;
        while (ref != null) {
            Commit com = getCommit(ref);
            System.out.println("===");
            System.out.println("commit " + ref);
            String topr = com.getTimestamp().toString();
            Date time = com.getTimestamp();
            int pstBefore = 16 + 3;
            int pstAfter = 10 + 10 + 4;
            int yearAfter = 10 + 10 + 8;
            String uno = topr.substring(0, pstBefore);
            String dos = " " + topr.substring(pstAfter, yearAfter);
            ArrayList<String> mergedP = com.getMergedParents();
            if (!mergedP.isEmpty()) {
                String short1 = mergedP.get(0).substring(0, 7);
                String short2 = mergedP.get(1).substring(0, 7);
                System.out.println("Merge: " + short1
                        + " " + short2);
            }
            String tres = " " + String.format("%1$tz", time);
            System.out.println("Date: " + uno + dos + tres);
            System.out.println(com.getMessage());
            System.out.println();
            ref = com.getParent();
        }
    }

    /** Prints information of all the commits ever made. */
    public void globalLog() {
        for (String eachComSHA : _allCommits) {
            Commit commit = getCommit(eachComSHA);
            System.out.println("===");
            System.out.println("commit " + eachComSHA);
            String topr = commit.getTimestamp().toString();
            Date time = commit.getTimestamp();
            int pstBefore = 16 + 3;
            int pstAfter = 10 + 10 + 4;
            int yearAfter = 10 + 10 + 8;
            ArrayList<String> mergedP = commit.getMergedParents();
            if (!mergedP.isEmpty()) {
                String short1 = mergedP.get(0).substring(0, 7);
                String short2 = mergedP.get(1).substring(0, 7);
                System.out.println("Merge: " + short1
                        + " " + short2);
            }
            String uno = topr.substring(0, pstBefore);
            String dos = " " + topr.substring(pstAfter, yearAfter);
            String tres = " " + String.format("%1$tz", time);
            System.out.println("Date: " + uno + dos + tres);
            System.out.println(commit.getMessage());
            System.out.println();
        }
    }

    /** Prints out the ids of all commits that have the given
     * commit message, one per line. If there are multiple such
     * commits, it prints the ids out on separate lines. The
     * commit message is a single operand; to indicate a multiword
     * message, put the operand in quotation marks, as for the commit
     * command below.
     * @param msg the commit msg */
    public void find(String msg) {
        ArrayList<String> toPrint = new ArrayList<>();
        for (String eachComSHA : _allCommits) {
            Commit comToCheck = getCommit(eachComSHA);
            if (comToCheck.getMessage().equals(msg)) {
                toPrint.add(eachComSHA);
            }
        }
        if (toPrint.isEmpty()) {
            System.out.println("Found no commit with that message.");
            return;
        } else {
            for (String each : toPrint) {
                System.out.println(each);
            }
        }
    }

    /** Unstages the file if staged for addition. If
     * tracked in current commit, stage it for removal
     * and remove the file from the working
     * directory; (remove only if it is tracked
     * in the current commit).
     * @param file which is the file's name */
    public void remove(String file) {
        _stagingarea.remove(file);
    }

    /** Prints the status of the gitletty. */
    public void status() {
        System.out.println("=== Branches ===");
        ArrayList<String> branch = new ArrayList<String>(_branches.keySet());
        Collections.sort(branch);
        for (String eachB : branch) {
            if (eachB.equals(_branchCurr)) {
                System.out.println("*" + eachB);
            } else {
                System.out.println(eachB);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        ArrayList<String> staged = new ArrayList<String>(_stagingarea
                .getforAddition().keySet());
        for (String stagedFile : staged) {
            System.out.println(stagedFile);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        ArrayList<String> removed = new ArrayList<String>(_stagingarea
                .getforRemoval());
        for (String removedFile : removed) {
            System.out.println(removedFile);
        }
        System.out.println();
        extraCredit();
    }

    /** Takes the version of the file as it exists in the head commit,
     * the front of the current branch, and puts it in the working
     * directory, overwriting the version of the file that's already
     * there if there is one. The new version of the file is not staged.
     * @param file which is the file's name */
    public void checkoutOne(String file) {
        checkoutTwo(_headSHA, file);
    }

    /** Takes the version of the file as it exists in the commit with
     * the given id, and puts it in the working directory, overwriting
     * the version of the file that's already there if there is one.
     * The new version of the file is not staged.
     * @param commitID the commit's sha
     * @param file the file's name */
    public void checkoutTwo(String commitID, String file) {
        String actual = "";
        for (String any : _allCommits) {
            if (any.startsWith(commitID)) {
                actual = any;
            }
        }
        if (actual.equals("")) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit commit = getCommit(actual);
        if (commit.getBlobberSHA(file) == null) {
            System.out.println("File does not exist in that commit.");
            return;
        } else {
            overwriteBlob(commit, file);
        }
    }

    /** Finds existing blobbers in blobDIR associated with given commit
     * and file then overwrites the current contents with the contents
     * of the given commit.
     * @param commitRef the commit that contents should be overwritten
     * @param file the file name */
    public void overwriteBlob(Commit commitRef, String file) {
        String blobsha = commitRef.getBlobberSHA(file);
        File retrieveBlob = new File(".gitlet/blobbers/" + blobsha);
        byte[] contents = Utils.readContents(retrieveBlob);
        File checkout = new File(file);
        Utils.writeContents(checkout, (Object) contents);
    }

    /** Takes all files in the commit at the head of the given
     * branch, and puts them in the working directory, overwriting the
     * versions of the files that are already there if they exist. Also,
     * at the end of this command, the given branch will now be considered
     * the current branch (HEAD). Any files that are tracked in the current
     * branch but are not present in the checked-out branch are deleted.
     * The staging area is cleared, unless the checked-out branch is the
     * current branch.
     * @param branch the branch name */
    public void checkoutThree(String branch) {
        if (_branches.get(branch) == null) {
            System.out.println("No such branch exists.");
            return;
        }
        if (_branchCurr.equals(branch)) {
            System.out.println("No need to checkout the current branch.");
            return;
        } else {
            Commit branchCom = getCommit(_branches.get(branch));
            Commit curCom = getCommit(_headSHA);
            HashMap<String, String> bblobs = branchCom.getBlobber();
            HashMap<String, String> hblobs = curCom.getBlobber();
            List<String> currFiles = Utils.plainFilenamesIn("./");
            for (String file : bblobs.keySet()) {
                if (hblobs.get(file) == null
                        && (currFiles.contains(file))) {
                    System.out.println("There is an untracked file in the"
                            + " way; delete it, or add and commit it first.");
                    return;
                }
            }
            for (String fileName : bblobs.keySet()) {
                overwriteBlob(branchCom, fileName);
            }
            for (String file : hblobs.keySet()) {
                if (!bblobs.containsKey(file)) {
                    Utils.restrictedDelete(file);
                }
            }
            _branchCurr = branch;
            _headSHA = _branches.get(branch);
            _stagingarea.reset();
            _stagingarea.setStageHead(branchCom);
        }
    }

    /** Creates a new branch with the given name, and points it at
     * the current head node. A branch is nothing more than a name for a
     * reference (a SHA-1 identifier) to a commit node. This command
     * does NOT immediately switch to the newly created branch (just as
     * in real Git). Before you ever call branch, your code should be
     * running with a default branch called "master".
     * @param branchName the branch's name */
    public void branch(String branchName) {
        for (String each : _branches.keySet()) {
            if (each.equals(branchName)) {
                System.out.println(("A branch with that name already exists."));
                return;
            }
        }
        _branches.put(branchName, _headSHA);
    }

    /** Deletes the branch with the given name. This only means to
     * delete the pointer associated with the branch; it does not mean
     * to delete all commits that were created under the branch, or
     * anything like that.
     * @param branchName the branch's name */
    public void rmBranch(String branchName) {
        if (!_branches.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
        } else if (branchName.equals(_branchCurr)) {
            System.out.println("Cannot remove the current branch.");
        } else {
            _branches.remove(branchName);
        }
    }

    /**Checks out all the files tracked by the given commit. Removes
     * tracked files that are not present in that commit. Also moves
     * the current branch's head to that commit node. See the intro for
     * an example of what happens to the head pointer after using reset.
     * The [commit id] may be abbreviated as for checkout. The staging area
     * is cleared. The command is essentially checkout of an arbitrary commit
     * that also changes the current branch head.
     * @param commitSHA the commit's id */
    public void reset(String commitSHA) {
        String actual = "";
        for (String each : _allCommits) {
            if (each.startsWith(commitSHA)) {
                actual = each;
            }
        }
        if (actual.equals("")) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit currCom = getCommit(_headSHA);
        HashMap<String, String> hblobs = currCom.getBlobber();
        List<String> currFiles = Utils.plainFilenamesIn("./");
        Commit want = getCommit(actual);
        HashMap<String, String> wblobs = want.getBlobber();
        for (String file : wblobs.keySet()) {
            if (hblobs.get(file) == null && currFiles.contains(file)) {
                System.out.println("There is an untracked file in the"
                        + " way; delete it, or add and commit it first.");
                return;
            }
        }
        for (String file : wblobs.keySet()) {
            overwriteBlob(want, file);
        }
        for (String file : hblobs.keySet()) {
            if (wblobs.get(file) == null) {
                Utils.restrictedDelete(file);
            }
        }
        _branches.put(_branchCurr, actual);
        _stagingarea.reset();
        _headSHA = actual;
        _stagingarea.setStageHead(want);
    }

    /** Extra credit for the rest of status. Maybe try later?? */
    public void extraCredit() {
        System.out.println("=== Modifications Not Staged For Commit ===");
        Commit head = getCommit(_headSHA);
        HashMap<String, String> headBlobs = head.getBlobber();
        ArrayList<String> mod = new ArrayList<>();
        ArrayList<String> untracked = new ArrayList<>();
        List<String> currFiles = Utils.plainFilenamesIn("./");
        for (String fileName : headBlobs.keySet()) {
            String headSha = headBlobs.get(fileName);
            File toread = new File(fileName);
            if (!toread.isFile()
                    && _stagingarea.getforRemoval().contains(fileName)) {
                break;
            }
            if (!toread.isFile()) {
                mod.add(fileName + "(deleted)");
                break;
            }
            byte[] curr = Utils.readContents(toread);
            String curCont = Utils.sha1((Object) curr);
            if (_stagingarea.getforRemoval().contains(fileName)
                && !currFiles.contains(fileName)) {
                mod.add(fileName + "(deleted)");
            } else if (_stagingarea.getforAddition().containsKey(fileName)
                    && currFiles.contains(fileName)
                    && !curCont.equals(headSha)) {
                mod.add(fileName + "(modified)");
            } else if (headSha != null && !headSha.equals(curCont)
                    && !_stagingarea.getforAddition().containsKey(fileName)) {
                mod.add(fileName + "(modified)");
            }
        }
        Collections.sort(mod);
        for (String each : mod) {
            System.out.println(each);
        }
        System.out.println();
        System.out.println("=== Untracked Files ===");
        for (String fileName : currFiles) {
            if (!headBlobs.containsKey(fileName)
                    && !_stagingarea.getforRemoval().contains(fileName)
                    && !_stagingarea.getforAddition().containsKey(fileName)) {
                untracked.add(fileName);
            }
        }
        Collections.sort(untracked);
        for (String each : untracked) {
            System.out.println(each);
        }
    }
}
