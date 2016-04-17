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
import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by mbpeele on 2/24/16.
 */
public class FileManager implements IFileManager {

    private File file;
    private File plantUML;
    private PrintWriter writer;
    private Persister persister;
    private PackageXMLElement packageElement;
    private List<ParcelableClass> classList;
    private ParcelableMethod method;
    private ParcelableClass targetClass;

    public FileManager(String name, String plantUMLName) {
        file = getXMLFile(name);
        plantUML = getplantUMLFile(plantUMLName);
        persister = new Persister();
        packageElement = new PackageXMLElement(file.getName());
        classList = new ArrayList<ParcelableClass>();
        try {
            writer = new PrintWriter(plantUML);
            writer.println("@startuml");
        }
        catch(FileNotFoundException exception){
            Logg.log("Couldn't create plantUML file writer");
        }
    }

    public FileManager(String name) {
        file = getXMLFile(name);
        persister = new Persister();
        packageElement = new PackageXMLElement(file.getName());
        classList = new ArrayList<ParcelableClass>();
    }

    @Override
    public void onPackageCalled(ParcelablePackage parcelablePackage) {
        packageElement = new PackageXMLElement(parcelablePackage);

        writeToFile(packageElement);
    }


    @Override
    public void onBeforeClassCalled(ParcelableClass parcelableClass){
        targetClass = parcelableClass;
        //If first class, only have to add it to the list
        if(classList.isEmpty()){
            classList.add(targetClass);
        }
        //If its not the first class, more checks
        else {
            //if this class isn't already in the classList, put it in there
            boolean isInList = false;
            for(int i = 0; i < classList.size(); i++){ //check if it's in the list
                String className = classList.get(i).getName();
                if(className.equals(targetClass.getName())){
                    isInList = true;
                }
            }
            if(!isInList){ //not in the list
                classList.add(0, targetClass);
            }

            //if there is a method, can write that method from previous class to target
            if(method != null) {
                ParcelableClass fromClass = null;
                //finds the method's declaring class as a parcelable
                for(int i = 0; i < classList.size(); i++){
                    if(classList.get(i).getName().equals(method.getDeclaringClassName())){
                        fromClass = classList.get(i);
                    }
                }
                try {
                    writeParsedValue(fromClass, method, targetClass);
                }
                catch (NullPointerException error){
                    System.out.println("could not find the method's class in the class list");
                }
                method = null;
            }
            else{
                writeParsedValue(classList.get(0), targetClass);
            }
        }
    }

    @Override
    public void onAfterClassCalled(ParcelableClass parcelableClass){
        /*
        if(classList.get(0) == parcelableClass){
            classList.remove(0);
        }
        */
    }

    @Override
    public void onBeforeMethodCalled(ParcelableMethod parcelableMethod) {
        if(method != null){
            ParcelableClass fromClass = null;
            //finds the method's declaring class as a parcelable
            for(int i = 0; i < classList.size(); i++){
                if(classList.get(i).getName().equals(method.getDeclaringClassName())){
                    fromClass = classList.get(i);
                }
            }
            try {
                writeParsedValue(fromClass, method, targetClass);
            }
            catch (NullPointerException error){
                System.out.println("could not find the method's class in the class list");
            }
        }
        method = parcelableMethod;
    }

    @Override
    public void onAfterMethodCalled(ParcelableMethod parcelableMethod) {
        if(method == parcelableMethod){
            method = null;
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

    public void writeParsedValue(ParcelableClass from, ParcelableMethod method, ParcelableClass to){
        //Constructs the line needing to be written for the method, using the from and to classes

        //for PlantUML:
        writer.println(from.getName() + " -> " + to.getName() + ": " + method.getMethodName());

        //for XMI:

        //**need to give the method the from and to classes in order to write its line in the file properly**

    }

    public void writeParsedValue(ParcelableClass to, ParcelableClass from){
        //writes the line for PlantUML when there is a class that instantiates another from a declaration

        //for PlantUML:
        writer.println(from.getName() + " -> " + to.getName());

        //for XMI:
        //Should not be necessary
    }
}
