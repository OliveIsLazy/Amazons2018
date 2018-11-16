import java.awt.*;
import javax.swing.*;

//Main class that runs the app
public class runthis {

    public static void main(String[] args) {

        Runnable r = new Runnable() {

            @Override
            public void run() {
                ArgParser parser = new ArgParser();

                try {
                    assert parser.parse(args);
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); // cross platform UI
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (AssertionError ae) {
                    System.exit(1);
                }

                Game game = new Game(parser.getCommands());
                if (parser.getCommands().get("-headless").equals("false")) {
                    ChessGUI cg = new ChessGUI(game);
                    JFrame f = new JFrame("ChessChamp");
                    f.add(cg.getGui());
                    // Ensures JVM closes after frame(s) closed
                    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    // See https://stackoverflow.com/a/7143398/418556 for demo.
                    f.setLocationByPlatform(true);

                    // ensures the frame is the minimum size it needs to be
                    // in order display the components within it
                    f.pack();
                    // ensures the minimum size is enforced.
                    f.setMinimumSize(f.getSize());
                    f.setVisible(true);
                }

                // Start new game if the first player is a bot
                if (parser.getCommands().get("-player1").equals("bot"))
                    game.startNewGame();
            }
        };
        // Swing GUIs should be created and updated on the EDT
        // http://docs.oracle.com/javase/tutorial/uiswing/concurrency
        SwingUtilities.invokeLater(r);
    }
}