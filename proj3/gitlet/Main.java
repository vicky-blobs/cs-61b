package gitlet;


import java.io.File;

import static gitlet.Utils.*;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Vicky Yu but please note
 *  I received help from other people during project parties and
 *  looking at piazza posts as well as office hours for the data
 *  structures and choices of instance variables that I chose to use,
 *  but I don't know their names to write down as citations :-(
 *  I also looked at geeksforgeeks.org to get a better understanding
 *  of HashMap methods and javatpoint.com to understand more
 *  about dates. */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String execute = args[0];
        if (execute.equals("init")) {
            init();
        } else {
            File working = new File(".gitlet/gitletty");
            if (!working.exists()) {
                System.out.println("Not in an initialized Gitlet directory.");
                return;
            }
            Gitletty workingGit = Utils.readObject(working, Gitletty.class);
            switch (execute) {
            case "add":
                gitAdd(workingGit, args);
                break;
            case "commit":
                gitCommit(workingGit, args);
                break;
            case "log":
                gitLog(workingGit, args);
                break;
            case "checkout":
                gitCheckout(workingGit, args);
                break;
            case "rm":
                gitRemove(workingGit, args);
                break;
            case "status":
                gitStatus(workingGit, args);
                break;
            case "global-log":
                gitGlobalLog(workingGit, args);
                break;
            case "find":
                gitFind(workingGit, args);
                break;
            case "branch":
                gitBranch(workingGit, args);
                break;
            case "rm-branch":
                gitRmBranch(workingGit, args);
                break;
            case "reset":
                gitReset(workingGit, args);
                break;
            case "merge":
                gitMerge(workingGit, args);
                break;
            default:
                System.out.println("No command with that name exists.");
                break;
            }
            gitlettySaver(workingGit);
        }
    }

    /** Initializes gitletty and makes all the directories. */
    public static void init() {
        File gitlettyDIR = new File(".gitlet/");
        if (!gitlettyDIR.exists()) {
            gitlettyDIR.mkdir();
            File blobberDir = new File(".gitlet/blobbers/");
            blobberDir.mkdir();
            File commitDir = new File(".gitlet/commits/");
            commitDir.mkdir();
            Gitletty gitletty = new Gitletty();
            gitlettySaver(gitletty);
        } else {
            System.out.println("A Gitlet version-control system "
                    + "already exists in the current directory.");
            return;
        }
    }

    /** Saves the current gitlet into a file.
     * @param git the working git */
    public static void gitlettySaver(Gitletty git) {
        File gitletFile = new File(".gitlet/gitletty");
        writeObject(gitletFile, git);
    }

    /** Processes the add command.
     * @param git the working git
     * @param args the program args */
    public static void gitAdd(Gitletty git, String... args) {
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            return;
        }
        git.add(args[1]);
    }

    /** Processes the commit command.
     * @param git the working git
     * @param args the program args */
    public static void gitCommit(Gitletty git, String... args) {
        if (args[1].equals("")) {
            System.out.println("Please enter a commit message.");
            return;
        }
        if (args.length >= 3) {
            System.out.println("Incorrect operands.");
            return;
        }
        git.commit(args[1]);
    }

    /** Processes the log command.
     * @param git the working git
     * @param args the program args */
    public static void gitLog(Gitletty git, String... args) {
        if (args.length != 1) {
            System.out.println("Incorrect operands.");
            return;
        }
        git.log();
    }

    /** Processes the checkout command.
     * @param git the working git
     * @param args the program args */
    public static void gitCheckout(Gitletty git, String... args) {
        if (args.length == 2) {
            git.checkoutThree(args[1]);
        } else if (args.length == 3) {
            git.checkoutOne(args[2]);
        } else if (args.length == 4) {
            if (args[2].equals("--")) {
                git.checkoutTwo(args[1], args[3]);
            } else {
                System.out.println("Incorrect operands.");
            }
        } else {
            System.out.println("Incorrect operands.");
        }
    }

    /** Processes the remove command.
     * @param git the working git
     * @param args the program args */
    public static void gitRemove(Gitletty git, String... args) {
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            return;
        }
        git.remove(args[1]);
    }

    /** Processes the status command.
     * @param git the working git
     * @param args the program args */
    public static void gitStatus(Gitletty git, String... args) {
        if (args.length != 1) {
            System.out.println("Incorrect operands.");
            return;
        }
        git.status();
    }

    /** Processes the global-log command.
     * @param git the working git
     * @param args the program args */
    public static void gitGlobalLog(Gitletty git, String... args) {
        if (args.length != 1) {
            System.out.println("Incorrect operands.");
            return;
        }
        git.globalLog();
    }

    /** Processes the find command.
     * @param git the working git
     * @param args the program args */
    public static void gitFind(Gitletty git, String... args) {
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            return;
        }
        git.find(args[1]);
    }

    /** Processes the branch command.
     * @param git the working git
     * @param args the program args */
    public static void gitBranch(Gitletty git, String... args) {
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            return;
        }
        git.branch(args[1]);
    }

    /** Processes the rm-branch command.
     * @param git the working git
     * @param args the program args */
    public static void gitRmBranch(Gitletty git, String... args) {
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            return;
        }
        git.rmBranch(args[1]);
    }

    /** Processes the reset command.
     * @param git the working git
     * @param args the program args */
    public static void gitReset(Gitletty git, String... args) {
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            return;
        }
        git.reset(args[1]);
    }

    /** Processes the merge command.
     * @param git the working git
     * @param args the program args */
    public static void gitMerge(Gitletty git, String... args) {
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            return;
        }
        git.merge(args[1]);
    }

}
