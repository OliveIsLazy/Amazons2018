import java.util.Arrays;
import java.util.HashMap;
import javax.lang.model.util.ElementScanner6;
import java.lang.Exception;

public class ArgParser {

    // -p1 => Assumed to be human unless specified otherwise with this command
    // -p2 => Assumed to be human unless specified otherwise with this command -s or
    // -size => Assumed to be 10 x 10 unless specified otherwise with this command
    // -a or -algorithm => Assumed to be Random unless specified otherwise
    // -hd or -headless => Reserved for bot v bot. Assumed false otherwise
    // -h => shows help

    // example CL arguments:
    // before: java -ea runthis human human 10 10 Random
    // after: java -ea runthis

    // before: java -ea runthis human bot 5 5 Minimax
    // after: java -ea runthis -p2 -size 5 -a minimax

    private HashMap<String, String> commands = new HashMap<String, String>();

    private String[] player1 = new String[] { "-p1", "-player1" };
    private String[] player2 = new String[] { "-p2", "-player2" };
    private String[] size = new String[] { "-s", "-size" };
    private String[] algorithm = new String[] { "-a", "-algorithm" };
    private String[] headless = new String[] { "-hd", "-headless" };

    private String[] availableAlgorithms = new String[] { "random", "minimax", "q_learning" };

    public ArgParser() {
        // Default Args
        addToArguments(player1, "human");
        addToArguments(player2, "human");
        addToArguments(size, "10");
        addToArguments(algorithm, "random");
        addToArguments(headless, "false");
    }

    private void addToArguments(String[] keys, String value) {
        for (String key : keys)
            this.commands.put(key, value.toLowerCase());
    }

    public boolean parse(String[] args) {
        try {
            assert args.length % 2 == 0;
        } catch (AssertionError e) {
            printHelp("Too few arguments were given.");
            return false;
        }
        for (int i = 0; i < args.length - 1; i += 2) {
            switch (args[i]) {
            case "-p1":
            case "-player1":
                if (args[i + 1].toLowerCase().equals("bot") || args[i + 1].toLowerCase().equals("human"))
                    addToArguments(player1, args[i + 1]);
                else {
                    printHelp("Wrong player type entered for " + args[i]);
                    return false;
                }
                break;
            case "-p2":
            case "-player2":
                if (args[i + 1].toLowerCase().equals("bot") || args[i + 1].toLowerCase().equals("human"))
                    addToArguments(player2, args[i + 1]);
                else {
                    printHelp("Wrong player type entered for " + args[i]);
                    return false;
                }
                break;
            case "-s":
            case "-size":
                try {
                    Integer s = Integer.valueOf(args[i + 1]);
                    assert s.intValue() < 20 && s.intValue() > 7;
                } catch (Exception e) {
                    printHelp("None integer entered for size.");
                    return false;
                } catch (AssertionError e) {
                    printHelp("-size is too big.");
                    return false;
                }
                break;
            case "-a":
            case "-algorithm":
                if (Arrays.stream(availableAlgorithms).anyMatch(args[i + 1]::equals))
                    addToArguments(algorithm, args[i + 1]);
                else {
                    printHelp("Algorithm " + args[i + 1] + " does not exist or hasn't been implemented yet");
                    return false;
                }
                break;
            case "-hd":
            case "-headless":
                if (args[i + 1].toLowerCase().equals("true") || args[i + 1].toLowerCase().equals("false")) {
                    if (!(commands.get("-p1").equals("bot") && commands.get("-p2").equals("bot"))) {
                        addToArguments(player1, "bot");
                        addToArguments(player2, "bot");
                    }
                    addToArguments(headless, args[i + 1]);
                } else {
                    printHelp("Invalid argument passed to -hd or -headless");
                    return false;
                }
                break;
            case "-h":
            case "-help":
                printHelp();
                return false;
            default:
                printHelp("Invalid option(s) or argument(s) used.");
                return false;
            }
        }
        return true;
    }

    private void printHelp() {
        System.out.println("Help was requested.");
    }

    private void printHelp(String message) {
        System.out.println("Help was requested due to: " + message);
    }

    public HashMap<String, String> getCommands() {
        return this.commands;
    }

}