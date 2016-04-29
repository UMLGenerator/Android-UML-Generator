package software.umlgenerator.data;


import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

import software.umlgenerator.data.model.parcelables.ParcelableClass;
import software.umlgenerator.data.model.parcelables.ParcelableMethod;
import software.umlgenerator.util.Common;
import software.umlgenerator.util.Logg;

/**
 * Created by mbpeele on 2/24/16.
 */
public class FileManager implements IFileManager {

    public static final String FILE_URIS = "fileUris";

    private File xmiFile;
    private File plantUMLFile;
    private ArrayList<Parser> parsers;

    public FileManager(String name) {
        xmiFile = getXMLFile(name + "-XMI");
        plantUMLFile = getPlantUMLFile(name + "-plantUML");

        LogicalParser parser = new LogicalParser();
        PlantUMLWriter plantWriter = new PlantUMLWriter(plantUMLFile);
        XMIWriter xmiwriter = new XMIWriter(xmiFile);

        parsers = new ArrayList<>();
        parsers.add(parser);
        for (int i = 0; i < parsers.size(); i++) {
            parsers.get(i).addWriter(plantWriter);
            parsers.get(i).addWriter(xmiwriter);
        }

        writeStart();
    }

    @Override
    public void writeStart() {
        for(int i = 0; i < parsers.size(); i++){
            parsers.get(i).start();
        }
    }

    @Override
    public void onBeforeClassCalled(ParcelableClass parcelableClass) {

        for(int i = 0; i < parsers.size(); i++){
            parsers.get(i).classStart(parcelableClass);
        }
    }

    @Override
    public void onAfterClassCalled(ParcelableClass parcelableClass) {

        for(int i = 0; i < parsers.size(); i++){
            parsers.get(i).classEnd(parcelableClass);
        }
    }

    @Override
    public void onBeforeMethodCalled(ParcelableMethod parcelableMethod) {

        for (int i = 0; i < parsers.size(); i++){
            parsers.get(i).methodStart(parcelableMethod);
        }
    }

    @Override
    public void onAfterMethodCalled(ParcelableMethod parcelableMethod) {

        for(int i = 0; i < parsers.size(); i++){
            parsers.get(i).methodEnd(parcelableMethod);
        }
    }

    @Override
    public void writeEnd() {
        Logg.log("WRITE END");
        for(int i = 0; i < parsers.size(); i++){
            parsers.get(i).stop();
        }
    }

    public File getXMLFile(String name) {
        File dir = new File(Common.FILE_DIR);
        dir.mkdirs();

        Logg.log("GETTING XML FILE: ", name);

        return new File(dir, name);
    }

    public File getPlantUMLFile(String name) {
        File dir = new File(Common.FILE_DIR);

        Logg.log("GETTING PLANTUML FILE: ", name);

        return new File(dir, name);
    }

    public ArrayList<Uri> getFileUris() {
        ArrayList<Uri> uris = new ArrayList<>();
        uris.add(Uri.fromFile(xmiFile));
        uris.add(Uri.fromFile(plantUMLFile));
        return uris;
    }
}
