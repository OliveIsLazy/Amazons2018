import java.awt.*;
import javax.swing.*;

//Main class that runs the app
public class runthis {

    public static void main(String[] args) {

        //args handler
        try{
            assert(args.length == 5);
        } catch (AssertionError e) {
            System.out.println("Parameters: [player type] [player type] [board width] [board height]");
            System.exit(1);
        }

        Runnable r = new Runnable() {

            @Override
            public void run() {
                try { UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() ); } catch (Exception e) { e.printStackTrace(); } //cross platform UI

                Game game = new Game(args[0], args[1], args[2], args[3], args[4]);
                ChessGUI cg = new ChessGUI(game);
                game.setImages(cg.getImages());
                
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
        };
        // Swing GUIs should be created and updated on the EDT
        // http://docs.oracle.com/javase/tutorial/uiswing/concurrency
        SwingUtilities.invokeLater(r);
    }
}