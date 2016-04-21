package software.umlgenerator.data;


import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

import software.umlgenerator.data.model.parcelables.ParcelableClass;
import software.umlgenerator.data.model.parcelables.ParcelableMethod;
import software.umlgenerator.data.model.parcelables.ParcelablePackage;
import software.umlgenerator.data.model.xml.PackageXMLElement;
import software.umlgenerator.util.Common;
import software.umlgenerator.util.Logg;

/**
 * Created by mbpeele on 2/24/16.
 */
public class FileManager implements IFileManager {

    private File file;
    private File plantUML;
    private PackageXMLElement packageElement;
    private ArrayList<UtilityWriter> writers;

    public FileManager(String name, String plantUMLName) {
        file = getXMLFile(name);
        plantUML = getplantUMLFile(plantUMLName);
        packageElement = new PackageXMLElement(file.getName());

        PlantUMLWriter plantWriter = new PlantUMLWriter(plantUML);

        writers = new ArrayList<>();
        writers.add(plantWriter);

        writeStart();
    }

    @Override
    public void onPackageCalled(ParcelablePackage parcelablePackage) {
        packageElement = new PackageXMLElement(parcelablePackage);
    }


    @Override
    public void writeStart() {
        for(int i = 0; i < writers.size(); i++){
            writers.get(i).writeStart();
        }
    }

    @Override
    public void onBeforeClassCalled(ParcelableClass parcelableClass) {

        for(int i = 0; i < writers.size(); i++){
            writers.get(i).classStart(parcelableClass);
        }
    }

    @Override
    public void onAfterClassCalled(ParcelableClass parcelableClass) {

        for(int i = 0; i < writers.size(); i++){
            writers.get(i).classEnd(parcelableClass);
        }
    }

    @Override
    public void onBeforeMethodCalled(ParcelableMethod parcelableMethod) {

        for (int i = 0; i < writers.size(); i++){
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
    public void writeEnd() {
        Logg.log("WRITE END");
        for(int i = 0; i < writers.size(); i++){
            writers.get(i).writeEnd();
        }
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
