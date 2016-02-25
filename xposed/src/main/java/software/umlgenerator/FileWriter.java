package software.umlgenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mbpeele on 2/24/16.
 */
public class FileWriter {

    public static void write(String path, String toWrite) {
        File file = new File(path);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(toWrite.getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
            Logg.log(e);
        }
        Logg.log("LISTEN TO: " + toWrite);
    }
}
