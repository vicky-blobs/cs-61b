# Gitlet Design Document

**Name**: Vicky Yu

## Classes and Data Structures
####Gitetty: represents the Gitlet object itself; contains the code for all the main commands to be executed
#####instance variables:
* stagingArea: a representation of gitletty's staging area
* headSHA: the commit head's SHA
* branchCurr: name of the current branch
* allCommits: a list of all the current commits, using SHA id to differentiate
* branches: all the current branches

####Main: where all the code will be executed

####StagingArea: the staging area of Gitlet
#####instance variables:
* forAddition: files that are staged/added (when add <file> is called)
* forRemoval: files that should be removed (when rm <file> is called); also used
to clear out the staging area after the files are successfully committed

####Commit: represents a commit
#####instance variables:
* Message - contains message of commit
* Timestamp - time at which a commit was created
* Parent - the parent commit of a commit object
* Blobber - the commit's associated blobs (is a hashmap 
that has the information of the blob's name and SHA-id)
* MergedParents - an ArrayList of parents if this commit was a merge
   

## Algorithms
#####Main Class:
######Commands:
- init(): initializes the gitlet
- main: processes the commands and executes the appropriate code
- gitlettySaver(): updates the current situation of gitlet; runs every time a command is called
- gitAdd(): processes the add command; calls gitletty's add
- gitCommit(): processes the commit command; calls gitletty's commit
- gitLog(): processes the log command; calls gitletty's log
- gitCheckout(): processes the checkout command; calls gitletty's checkout
commands based off of the program args
- gitRemove(): processes the rm command; calls gitletty's remove
- gitStatus(): processes the status command: calls gitletty's status
- gitGlobalLog(): processes the global-log command; calls gitletty's globallog
- gitFind(): processes the find command; calls gitletty's find
- gitBranch(): processes the branch command; calls gitletty's branch
- gitRmBranch(): processes the branch command; calls gitletty's rmbranch
- gitReset(): processes the reset command; calls gitletty's reset
- gitMerge(): processes the merge command; calls gitletty's merge

#####Gitletty Class (Serializable):
######Commands:
- Gitletty(): the constructor of the gitlet object; it is the initialization of a new
gitlet object
- add(file): adds the file to the staging area; calls the stagingArea's add function
- commiterSaver(sha, commit): saves the commit into a file under the commit directory
- commit(msg): commits the file from the staging area; calls the StagingArea's commit function
and updates the instance variables such as allCommits, branches, head, etc.
- getCommit(): get's the commit that corresponds to the SHA
- log(): returns the log of the commits w/ metadata
- remove(): removes files; calls staging area's remove
- status(): processes the status command
- checkoutOne(): processes the first checkout option (-- file)
- checkoutTwo(): processes the second checkout option (commitID -- file)
- checkoutThree(): processes the third checkout option (branch)
- overwriteBlob(): overwrites current blobs with the contents of a given commit & file name
- globalLog(): displays information about all commits ever made
- find(): prints all commits that matches the given commit msg
- branch(): creates new branch
- rmbranch(): removes the branch
- reset(): checks out files of given commit, resets staging area and moves
the current branch's head to given commit's node
- merge(): merges files from given branch into current branch
- merge2(): merges files from given branch into current branch; my second 
version LOL i cant get it to work oof; this function mostly checks for conditionals/failure cases
- mergemodified(): checks the conditionals of modified files since split point
- mergepresent(): checks the conditionals of present files since split point 
- mergeconflict(): overwrites the files that have conflicts;
- mergecommit(): commits the new merged situation
- commonAncestor(): returns the latest common ancestor between two commits
- extraCredit(): processes the optional half of status; maybe will implement?? 

#####Commit Class (Serializable):
######Commands:
- Commit(): constructor of the commit; takes in the commit message, timestamp of the current
date, the parent commit, and the associated blobs (file contents)
- getMessage(): gets the commit's message
- getTimestamp(): gets the commit's timestamp
- getParent(): gets the commit's parent commit
- getBlobber(): gets the commit's associated blobs (file contents)
- getBlobberSHA(): gets the commit's blob's SHA-id
- distanceFromInitial(): returns how far this commit is from initial
- setMergedParents(): sets instance variable _mergedParents
- getMergedParents(): returns the instance variable _mergedParents

#####StagingArea Class (Serializable):
######Commands:
- StagingArea(): constructor of the staging area; it is a representation of the staging area
- addFile(file): executes the add command from (main.java add <file>) 
- commitFile(msg): executes the commit command from (main.java commit <msg>); returns the current commit
so that the gitletty class can reference the commit
- blobberSaver(sha, blob): saves the blob into a new file into the blobbers directory
- setStageHead(): sets the _head instance variable to the appropriate head commit
- getForAddition: gets the _forAddition instance variable; used mostly for debugging lol
- reset(): clears the staging area (my _forAddition & _forRemoval instance variables)
- remove: does the rm command on the spec; removes files
- commitMerge(): a special commit that deals with merging


## Persistence
######Init: 
creates a .gitlet folder, inside this .gitlet folder; there are commits and blobbers 
 that represents commit and blobbers; each commit is saved by its unique sha-id
######Add: 
making a new file, unless it already exists; not written into the folder yet
######Commit: 
adds a new file (the commit object) into the commit folder, with the corresponding blobber 
