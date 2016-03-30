package software.umlgenerator.xposed.io;

import android.os.Environment;

import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import software.umlgenerator.util.Logg;
import software.umlgenerator.xposed.model.PackageElement;

/**
 * Created by mbpeele on 2/24/16.
 */
public class FileManager {

    private static final String FILE_DIR =
            Environment.getExternalStorageDirectory() + "/" + "software.umlgenerator" + "/";

    private File file;
    private Persister persister;

    public FileManager(File file1) {
        file = file1;
        persister = new Persister();
    }

    public static File getFile(String name) {
        File dir = new File(FILE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, name);
        if (file.exists()) {
            try {
                new PrintWriter(file.getName()).close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return file;
    }
}
