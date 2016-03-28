package software.umlgenerator.xposed.io;

import android.os.Environment;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Member;

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
            Environment.getExternalStorageDirectory() + "/" +
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
            e.printStackTrace();
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
            e.printStackTrace();
        }

        return file;
    }

    public static File getFile() {
        File dir = new File(FILE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, FILE_NAME);

        return file;
    }

    public static String readFile(String packageName, File file) {
        StringBuilder stringBuffer = new StringBuilder("");
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);

            int content;
            while ((content = inputStream.read()) != -1) {
                stringBuffer.append((char) content);
            }
            inputStream.close();
        } catch (IOException e) {
            Logg.log("FAIL: ", packageName);
        }

        return stringBuffer.toString();
    }
}
