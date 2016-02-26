package software.umlgenerator;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mbpeele on 2/24/16.
 */
public class FileManager {

    public static void write(String path, String toWrite) {
        File file = new File(path);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            out.write(toWrite.getBytes());
            out.flush();
            out.close();
            Logg.log("WROTE: " + toWrite);
        } catch (IOException e) {
            Logg.log("FAIL WRITE: ", e);
        }
    }

    public static boolean doesFileExist(String path) {
        return new File(path).exists();
    }
}
