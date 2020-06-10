package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/** Commit class for Gitlet, the tiny stupid version-control system.
 *  This class represents a commit object, please look at the constructor
 *  below to understand what my commit object is like.
 *  @author Vicky Yu but please note
 *  I received help from other people during project parties and
 *  looking at piazza posts as well as office hours for the data
 *  structures and choices of instance variables that I chose to use,
 *  but I don't know their names to write down as citations :-(
 *  I also looked at geeksforgeeks.org to get a better understanding
 *  of HashMap methods and javatpoint.com to understand more
 *  about dates. */
public class Commit implements Serializable {

    /** Commit message. */
    private String _message;

    /** Timestamp of commit. */
    private Date _timestamp;

    /** This commit's parent, represented as a SHA. */
    private String _parent;

    /** This commit's associated blobs. The hashmap's key is the
     * blob's file name. The hashmap's value is the SHA-ID of the blob. */
    private HashMap<String, String> _blobber;

    /** This commit's merged parents, if it has any. The string is the
     * first seven characters of their SHA-id*/
    private ArrayList<String> _mergedParents = new ArrayList<>();

    /** A commit object's constructor.
     * @param message the commit msg
     * @param timestamp commit timestamp
     * @param parent commit's parent
     * @param blobber commit's associated blob */
    public Commit(String message, Date timestamp, String parent,
                  HashMap<String, String> blobber) {
        this._message = message;
        this._timestamp = timestamp;
        this._parent = parent;
        this._blobber = new HashMap<String, String>();
        for (String ind: blobber.keySet()) {
            this._blobber.put(ind, blobber.get(ind));
        }
    }

    /** Gets the current commit's message.
     * @return string */
    public String getMessage() {
        return this._message;
    }

    /** Gets the current commit's timestamp.
     * @return Timestamp */
    public Date getTimestamp() {
        return this._timestamp;
    }

    /** Gets the current commit's parent represented as SHA.
     * @return String the parent's string. */
    public String getParent() {
        return this._parent;
    }

    /** Gets the current commit's associated blobs.
     * @return Blobber */
    public HashMap<String, String> getBlobber() {
        return this._blobber;
    }

    /** Gets the SHA-id of the files at this commit, which
     * looks at the commit's blob's file name and returns
     * the sha-id of the appropriate blob.
     * @param file which is the name of the file.
     * @return String of the blob's SHA */
    public String getBlobberSHA(String file) {
        return _blobber.get(file);
    }

    /** Gets the distance of this commit from the
     * initial commit.
     * @return the distance from intial */
    public int distancefromInitial() {
        if (_parent == null) {
            return 0;
        } else {
            Commit parent = Gitletty.getCommit(_parent);
            return parent.distancefromInitial() + 1;
        }
    }

    /** Set parents of merge.
     * @param mom first parent
     * @param dad second parent */
    public void setMergedParents(String mom, String dad) {
        _mergedParents.add(mom);
        _mergedParents.add(dad);
    }

    /** Get merged parents.
     * @return ArrayList of parents */
    public ArrayList<String> getMergedParents() {
        return this._mergedParents;
    }

}
