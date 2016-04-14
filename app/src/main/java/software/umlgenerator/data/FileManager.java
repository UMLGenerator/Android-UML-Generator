package software.umlgenerator.data;

import android.net.Uri;

import org.simpleframework.xml.core.Persister;

import java.io.File;

import software.umlgenerator.data.model.parcelables.ParcelableClass;
import software.umlgenerator.data.model.parcelables.ParcelableMethod;
import software.umlgenerator.data.model.parcelables.ParcelablePackage;
import software.umlgenerator.data.model.xml.ClassXMLElement;
import software.umlgenerator.data.model.xml.PackageXMLElement;
import software.umlgenerator.util.Common;
import software.umlgenerator.util.Logg;
import java.util.List;

/**
 * Created by mbpeele on 2/24/16.
 */
public class FileManager implements IFileManager {

    private File file;
    private Persister persister;
    private PackageXMLElement packageElement;
    private List<ParcelableClass> classList;
    private ParcelableMethod method;
    private ParcelableClass targetClass;

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
        targetClass = parcelableClass;
        //If first class, only have to add it to the list
        if(classList.isEmpty()){
            classList.add(parcelableClass);
        }
        //If its not the first class, more checks
        else {
            //if there is a method, can write that method from previous class to target
            if(method != null) {
                writeParsedValue(classList.get(0), method, targetClass);
            }
            //otherwise nothing to write
        }

//        packageElement.addClassElement(new ClassXMLElement(parcelableClass));
//
//        writeToFile(packageElement);
    }

    @Override
    public void onBeforeMethodCalled(ParcelableMethod parcelableMethod) {

    }

    @Override
    public void onAfterMethodCalled(ParcelableMethod parcelableMethod) {

    }

    @Override
    public void writeToFile(final PackageXMLElement packageXMLElement) {
//        final Scheduler.Worker worker = Schedulers.io().createWorker();
//        worker.schedule(new Action0() {
//            @Override
//            public void call() {
//                try {
//                    persister.write(packageXMLElement, file);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    worker.unsubscribe();
//                }
//            }
//        });
    }

    public File getXMLFile(String name) {
        File dir = new File(Common.FILE_DIR);
        dir.mkdirs();

        Logg.log("GETTING XML FILE: ", name);

        return new File(dir, name);
    }

    public Uri getFileUri() {
        return Uri.fromFile(file);
    }

    public void writeParsedValue(ParcelableClass from, ParcelableMethod method, ParcelableClass to){
        //Constructs the line needing to be written for the method, using the from and to classes

        //for PlantUML:
        //writeToFile(from.getName() + " -> " + to.getName() + ": " + method.getName());
        //This might require a new line character at the end as well

        //for XMI:
        //**need to give the method the from and to classes in order to write its line in the file properly**

    }
}
