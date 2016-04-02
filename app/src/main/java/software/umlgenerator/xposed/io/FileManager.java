package software.umlgenerator.xposed.io;

import android.os.Looper;

import org.simpleframework.xml.core.Persister;

import java.io.File;

import software.umlgenerator.util.Common;
import software.umlgenerator.util.Logg;
import software.umlgenerator.xposed.model.parcelables.ParcelableClass;
import software.umlgenerator.xposed.model.parcelables.ParcelableMethod;
import software.umlgenerator.xposed.model.parcelables.ParcelablePackage;
import software.umlgenerator.xposed.model.xml.ClassXMLElement;
import software.umlgenerator.xposed.model.xml.MethodXMLElement;
import software.umlgenerator.xposed.model.xml.PackageXMLElement;

/**
 * Created by mbpeele on 2/24/16.
 */
public class FileManager implements IFileManager {

    private File file;
    private Persister persister;
    private PackageXMLElement packageElement;

    public FileManager(String name) {
        file = getXMLFile(name);
        persister = new Persister();
    }

    @Override
    public void onPackageCalled(ParcelablePackage parcelablePackage) {
        packageElement = new PackageXMLElement(parcelablePackage);

        writeToFile(packageElement);
    }

    @Override
    public void onClassCalled(ParcelableClass parcelableClass) {
        packageElement.addClassElement(new ClassXMLElement(parcelableClass));

        writeToFile(packageElement);
    }

    @Override
    public void onMethodCalled(ParcelableMethod parcelableMethod) {
        packageElement.addMethodElement(new MethodXMLElement(parcelableMethod));

        writeToFile(packageElement);
    }

    @Override
    public void writeToFile(PackageXMLElement packageXMLElement) {
        try {
            persister.write(packageXMLElement, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getXMLFile(String name) {
        File dir = new File(Common.FILE_DIR);
        dir.mkdirs();

        Logg.log("GETTING XML FILE: ", name);

        return new File(dir, name);
    }
}
