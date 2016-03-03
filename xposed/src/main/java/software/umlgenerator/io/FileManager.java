package software.umlgenerator.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import software.umlgenerator.util.Logg;

/**
 * Created by mbpeele on 2/24/16.
 */
public class FileManager {

    private final String filePath;
    private final File file;

    public FileManager(String path) {
        filePath = path;
        file = new File(filePath);
    }

    public static void writePackageNameFile(String path, String toWrite) {
        File file = new File(path);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            out.write(toWrite.getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
            Logg.log("FAIL WRITE: ", e);
        }
    }

    public static boolean doesFileExist(String path) {
        return new File(path).exists();
    }
}
