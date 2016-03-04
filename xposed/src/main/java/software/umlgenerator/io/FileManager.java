package software.umlgenerator.io;

import android.os.Environment;
import android.util.Log;

import com.google.common.io.Files;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Member;
import java.util.EnumSet;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import software.umlgenerator.model.PackageElement;
import software.umlgenerator.util.Logg;

/**
 * Created by mbpeele on 2/24/16.
 */
public class FileManager implements FileManagerInterface {

    private final File file;
    private final Serializer writer;

    private static final String FOLDER_NAME = "software.umlgenerator";
    private static final String FILE_NAME = "uml";
    private static final String FILE_DIR =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" +
                    FOLDER_NAME + "/";

    public FileManager(File file1) {
        file = file1;
        writer = new Persister();
    }

    @Override
    public void writePackageElement(String packageName) {
        try {
            writer.write(new PackageElement(packageName), file);
        } catch (Exception e) {
            Logg.log("FAILED WRITING PACKAGE: ", e);
        }
    }

    @Override
    public void writeClassElement(Class clazz) {

    }

    @Override
    public void writeMethodElement(Member method) {

    }

    public static File writeToFile(String name) {
        File dir = new File(FILE_DIR);
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
        } catch (IOException e) {
            Logg.log("FAIL WRITE: ", e);
        }

        return file;
    }

    public static File getFile(String name) {
        File dir = new File(FILE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, FILE_NAME);
        if (!file.exists()) {
            file = new File(file.getAbsolutePath());
        }

        return file;
    }

    public static String readFile(File file) {
        StringBuilder stringBuffer = new StringBuilder("");
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);

            int content;
            while ((content = inputStream.read()) != -1) {
                stringBuffer.append((char) content);
            }
        } catch (IOException e) {
            Logg.log("FAIL READ: ", e);
        }

        return stringBuffer.toString();
    }
}
