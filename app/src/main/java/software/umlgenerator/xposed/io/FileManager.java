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
import software.umlgenerator.xposed.model.ClassElement;
import software.umlgenerator.xposed.model.MethodElement;
import software.umlgenerator.xposed.model.PackageElement;

/**
 * Created by mbpeele on 2/24/16.
 */
public class FileManager {

    private static final String FILE_DIR =
            Environment.getExternalStorageDirectory() + "/" + "software.umlgenerator" + "/";

    private File file;
    private Persister persister;
    private PackageElement packageElement;

    public FileManager(File file) {
        this.file = file;
        packageElement = new PackageElement(file.getName());
        persister = new Persister();
    }

    public void rewrite(PackageElement packageElement) {
        try {
            persister.write(packageElement, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeClassElement(ClassElement classElement) {
        packageElement.addClassElement(classElement);
        rewrite(packageElement);
    }

    public void writeMethodElement(MethodElement methodElement) {
        packageElement.addMethodElement(methodElement);
        rewrite(packageElement);
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
