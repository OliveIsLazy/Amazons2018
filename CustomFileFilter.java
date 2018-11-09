import javax.swing.filechooser.*;
import java.io.File;

public class CustomFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".amazons") || f.isDirectory();
    }

    @Override
    public String getDescription() {
        return "AMAZONS files (*.amazons)";
    }
}