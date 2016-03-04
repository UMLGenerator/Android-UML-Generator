package software.umlgenerator.io;

import android.os.Environment;
import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import software.umlgenerator.model.PackageElement;
import software.umlgenerator.util.Logg;

/**
 * Created by mbpeele on 2/24/16.
 */
public class FileManager {

    private final String filePath;
    private final File file;
    private final Serializer writer;

    private static final File FILE_DIR =
            Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS);
    private static final String FOLDER_NAME = "software.umlgenerator";
    private static final String FILE_NAME = "uml";

    public FileManager(String path) {
        filePath = path;
        file = new File(filePath);
        writer = new Persister();
    }

    public static File writeToFile(String name) {
        File dir = new File(FILE_DIR.toString() + "/" + FOLDER_NAME + "/");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, FILE_NAME);
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            out.write(name.getBytes());
            out.flush();
            out.close();

            Logg.log("WROTE: ", name, "TO: ", dir.getAbsolutePath(), file.getAbsolutePath());
        } catch (IOException e) {
            Logg.log("FAIL WRITE: ", e);
        }

        return file;
    }

    public static boolean doesFileExist(String name) {
        Logg.log("CHECKING FOR WRITTEN FILE");
        File dir = new File(FILE_DIR.toString() + "/" + FOLDER_NAME + "/");
        if (!dir.exists()) {
            Logg.log("DIR NO EXISTS");
            dir.mkdirs();
        }

        File file = new File(dir, FILE_NAME);
        if (!file.exists()) {
            Logg.log("FILE NO EXISTS");
        }

        boolean hasName = false;

        StringBuffer stringBuffer = new StringBuffer("");
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);

            int content;
            while ((content = inputStream.read()) != -1) {
                stringBuffer.append((char) content);
            }

            Logg.log("CONTENTS: ", stringBuffer, "GIVEN NAME: ", name);
            hasName = stringBuffer.equals(name);
            Logg.log("SAME? ", hasName);
        } catch (IOException e) {
            Logg.log("FAIL READ: ", e);
        }

        return hasName;
    }

    public void test(String name) {
        try {
            writer.write(new PackageElement(name), file);
        } catch (Exception e) {
            Logg.log("FAIL WRITE: ", e);
        }
    }
}
