package software.umlgenerator.data;

import android.net.Uri;
import android.os.Parcel;

import org.simpleframework.xml.core.Persister;

import java.io.File;

import software.umlgenerator.data.model.parcelables.ParcelableClass;
import software.umlgenerator.data.model.parcelables.ParcelableMethod;
import software.umlgenerator.data.model.parcelables.ParcelablePackage;
import software.umlgenerator.data.model.xml.ClassXMLElement;
import software.umlgenerator.data.model.xml.PackageXMLElement;
import software.umlgenerator.util.Common;
import software.umlgenerator.util.Logg;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Dictionary;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by mbpeele on 2/24/16.
 */
public class FileManager implements IFileManager {

    private File file;
    private File plantUML;
    private FileWriter writer;
    private Persister persister;
    private PackageXMLElement packageElement;
    private List<ParcelableClass> classList;
    private List<ParcelableMethod> method;
    private ParcelableClass targetClass;
    private Boolean usedMethod = false;
    private Boolean firstClass = true;
    private String legend;
    private ArrayList<UtilityWriter> writers;

    public FileManager(String name, String plantUMLName) {
        file = getXMLFile(name);
        plantUML = getplantUMLFile(plantUMLName);
        persister = new Persister();
        packageElement = new PackageXMLElement(file.getName());

        PlantUMLWriter plantWriter = new PlantUMLWriter(plantUML);

        writers = new ArrayList<UtilityWriter>();
        writers.add(plantWriter);

        for(int i = 0; i < writers.size(); i++){
            writers.get(i).writeStart();
        }
    }

    public FileManager(String name) {
        file = getXMLFile(name);
        persister = new Persister();
        packageElement = new PackageXMLElement(file.getName());
        classList = new ArrayList<ParcelableClass>();
        method = new ArrayList<ParcelableMethod>();
    }

    @Override
    public void onPackageCalled(ParcelablePackage parcelablePackage) {
        packageElement = new PackageXMLElement(parcelablePackage);

        writeToFile(packageElement);
    }


    @Override
    public void onBeforeClassCalled(ParcelableClass parcelableClass){

        for(int i = 0; i < writers.size(); i++){
            writers.get(i).classStart(parcelableClass);
        }
    }

    @Override
    public void onAfterClassCalled(ParcelableClass parcelableClass){

        for(int i = 0; i < writers.size(); i++){
            writers.get(i).classEnd(parcelableClass);
        }
    }

    @Override
    public void onBeforeMethodCalled(ParcelableMethod parcelableMethod) {

        for(int i = 0; i < writers.size(); i++){
            writers.get(i).methodStart(parcelableMethod);
        }
    }

    @Override
    public void onAfterMethodCalled(ParcelableMethod parcelableMethod) {

        for(int i = 0; i < writers.size(); i++){
            writers.get(i).methodEnd(parcelableMethod);
        }
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

    public File getplantUMLFile(String name) {
        File dir = new File(Common.FILE_DIR);

        Logg.log("GETTING PLANTUML FILE: ", name);

        return new File(dir, name);
    }

    public Uri getFileUri() {
        return Uri.fromFile(file);
    }
}
