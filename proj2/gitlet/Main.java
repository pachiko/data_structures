package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author phill
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }

        String firstArg = args[0];
        switch (firstArg) {
            case "init" -> Repository.init();
            case "add" -> Repository.add(args);
            case "commit" -> Repository.commit(args);
            case "rm" -> Repository.remove(args);
            case "log" -> Repository.log();
            case "global-log" -> Repository.globalLog();
            case "find" -> Repository.find(args);
            case "status" -> Repository.status();
            case "checkout" -> Repository.checkout(args);
            case "branch" -> Repository.branch(args);
            default -> System.out.println("No command with that name exists.");
        }
    }
}
