package software.umlgenerator.xposed.io;

import android.os.Looper;

import org.simpleframework.xml.core.Persister;

import java.io.File;

import rx.Scheduler;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
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
        packageElement = new PackageXMLElement(file.getName());
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
    public void writeToFile(final PackageXMLElement packageXMLElement) {
        final Scheduler.Worker worker = Schedulers.io().createWorker();
        worker.schedule(new Action0() {
            @Override
            public void call() {
                try {
                    persister.write(packageXMLElement, file);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    worker.unsubscribe();
                }
            }
        });
    }

    public File getXMLFile(String name) {
        File dir = new File(Common.FILE_DIR);
        dir.mkdirs();

        Logg.log("GETTING XML FILE: ", name);

        return new File(dir, name);
    }
}
