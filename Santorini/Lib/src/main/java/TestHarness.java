import java.io.IOException;

/**
 * Main Test harness for the entire application. You may run this
 * with the following commands:
 * -all -> denotes running all the tests
 * -board -> running the board test harness
 * -rules -> running the rule test harness
 * -strategy -> running the strategy test harness
 */
public class TestHarness {
    private static final String HELP_MESSAGE = "Potential Commands include:\n" +
            "-all -> denotes running all the tests\n" +
            "-board -> running the board test harness\n" +
            "-rules -> running the rule test harness\n" +
            "-strategy -> running the strategy test harness";

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println(HELP_MESSAGE);
        }

        switch (args[0]) {
            case "-all":
                XBoard.main(args);
                XRules.main(args);
                XStrategy.main(args);
                break;
            case "-board":
                XBoard.main(args);
                break;
            case "-rules":
                XRules.main(args);
                break;
            case "-strategy":
                XStrategy.main(args);
                break;
            default:
                System.out.println("Unrecognized command please use an actual command.");
                System.out.println(HELP_MESSAGE);
        }
    }
}
