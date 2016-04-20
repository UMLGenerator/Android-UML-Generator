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

    public FileManager(String name, String plantUMLName) {
        file = getXMLFile(name);
        plantUML = getplantUMLFile(plantUMLName);
        persister = new Persister();
        packageElement = new PackageXMLElement(file.getName());
        classList = new ArrayList<ParcelableClass>();
        method = new ArrayList<ParcelableMethod>();
        try {
            writer = new FileWriter(plantUML);
            writer.append("@startuml" + '\n');
            writer.close();
        }
        catch(java.io.IOException exception){
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
        //If very first class
        if(firstClass){
            writeParsedLegend(targetClass);
            firstClass = false;
        }

        //If first class, only have to add it to the list
        if(classList.isEmpty()){
            classList.add(targetClass);
            writeParsedValue(targetClass, targetClass);
            writeParsedStart(targetClass);
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
            if(!method.isEmpty()) {
                ParcelableClass fromClass = null;
                //finds the method's declaring class as a parcelable
                for(int i = 0; i < classList.size(); i++){
                    //System.out.println("does " + classList.get(i).getName() + " equal " + method.getDeclaringClassName());
                    if(classList.get(i).getName().equals(method.get(0).getDeclaringClassName())){
                        fromClass = classList.get(i);
                    }
                }
                if(fromClass != null) {
                    try {
                        //System.out.println(fromClass.getName());
                        writeParsedValue(fromClass, method.get(0), targetClass);
                        writeParsedStart(targetClass);
                        usedMethod = true;
                    } catch (NullPointerException error) {
                        //System.out.println(fromClass.getName());
                        Logg.log("could not find the method's class in the class list");
                    }
                }

            }
            else{
                writeParsedValue(classList.get(0), targetClass);
                writeParsedStart(targetClass);
            }
        }
    }

    @Override
    public void onAfterClassCalled(ParcelableClass parcelableClass){
        writeParsedEnd(parcelableClass);
        /*
        if(classList.get(0) == parcelableClass){
            classList.remove(0);
        }
        */
    }

    @Override
    public void onBeforeMethodCalled(ParcelableMethod parcelableMethod) {
//        if(!method.isEmpty()){
//            ParcelableClass fromClass = null;
//            //finds the method's declaring class as a parcelable
//            for(int i = 0; i < classList.size(); i++){
//                if(classList.get(i).getName().equals(method.get(0).getDeclaringClassName())){
//                    fromClass = classList.get(i);
//                }
//            }
//            if(fromClass != null) {
//                try {
//                    //System.out.println(fromClass.getName());
//                    writeParsedValue(fromClass, method.get(0), targetClass);
//                } catch (NullPointerException error) {
//                    //System.out.println(fromClass.getName());
//                    System.out.println("could not find the method's class in the class list");
//                }
//            }
//        }
        usedMethod = false;
        method.add(0, parcelableMethod);
    }

    @Override
    public void onAfterMethodCalled(ParcelableMethod parcelableMethod) {
        if(!usedMethod){
            ParcelableClass fromClass = null;
            //finds the method's declaring class as a parcelable
            for(int i = 0; i < classList.size(); i++){
                if(classList.get(i).getName().equals(method.get(0).getDeclaringClassName())){
                    fromClass = classList.get(i);
                }
            }

            if(fromClass != null) {
                try {
                    //System.out.println(fromClass.getName());
                    writeParsedValue(fromClass, method.get(0), fromClass);
                } catch (NullPointerException error) {
                    //System.out.println(fromClass.getName());
                    Logg.log("could not find the method's class in the class list");
                }
            }
            usedMethod = true;
        }
        for(int i = 0; i < method.size(); i++){
            if(method.get(i).getMethodName().equals(parcelableMethod.getMethodName())) {
                Logg.log("removing method: " + parcelableMethod.getMethodName());
                method.remove(i);
            }
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
        try {
            writer = new FileWriter(plantUML, true);
            writer.append((from.getName().replaceAll(legend, "") + " -> " +
                    to.getName().replaceAll(legend, "") + ": " +
                    method.getMethodName().replaceAll(legend, "") + '\n').replace("$", "_"));
            Logg.log((from.getName().replaceAll(legend, "") + " -> "
                    + to.getName().replaceAll(legend, "") + ": "
                    + method.getMethodName().replaceAll(legend, "")).replace("$", "_"));
            writer.close();
        }
        catch(java.io.IOException exception){
            Logg.log("Couldn't write to the plantUML file");
        }
        //writer.println((from.getName() + " -> " + to.getName() + ": " + method.getMethodName()).replace("$", "_"));
        //Logg.log((from.getName() + " -> " + to.getName() + ": " + method.getMethodName()).replace("$", "_"));

        //for XMI:

        //**need to give the method the from and to classes in order to write its line in the file properly**

    }

    public void writeParsedValue(ParcelableClass to, ParcelableClass from){
        //writes the line for PlantUML when there is a class that instantiates another from a declaration

        //for PlantUML:
        try {
            writer = new FileWriter(plantUML, true);
            writer.append((from.getName().replaceAll(legend, "") + " -> " + to.getName().replaceAll(legend, "") + '\n').replace("$", "_"));
            Logg.log((from.getName().replaceAll(legend, "") + " -> " + to.getName().replaceAll(legend, "")).replace("$", "_"));
            writer.close();
        }
        catch(java.io.IOException exception){
            Logg.log("Couldn't write to the plantUML file");
        }
        //writer.println((from.getName() + " -> " + to.getName()).replace("$", "_"));
        //Logg.log((from.getName() + " -> " + to.getName()).replace("$", "_"));

        //for XMI:
        //Should not be necessary
    }

    public void writeParsedStart(ParcelableClass start){
        try {
            writer = new FileWriter(plantUML, true);
            writer.append(("activate " + start.getName().replaceAll(legend, "") + '\n').replace("$", "_"));
            Logg.log(("activate " + start.getName().replaceAll(legend, "")).replace("$", "_"));
            writer.close();
        }
        catch(java.io.IOException exception){
            Logg.log("Couldn't write to the plantUML file");
        }
        //writer.println(("activate " + start.getName()).replace("$", "_"));
        //Logg.log(("activate " + start.getName()).replace("$", "_"));
    }

    public void writeParsedEnd(ParcelableClass end){
        try {
            writer = new FileWriter(plantUML, true);
            writer.append(("deactivate " + end.getName().replaceAll(legend, "") + '\n').replace("$", "_"));
            Logg.log(("deactivate " + end.getName().replaceAll(legend, "")).replace("$", "_"));
            writer.close();
        }
        catch(java.io.IOException exception){
            Logg.log("Couldn't write to the plantUML file");
        }

        //writer.println(("deactivate " + end.getName()).replace("$", "_"));
        //Logg.log(("deactivate " + end.getName()).replace("$", "_"));
    }

    public void writeParsedLegend(ParcelableClass first){
        String fullName = first.getName();
        List<String> pieces = new ArrayList<String>();

        Logg.log("Full Name: " + fullName);

        for (String retval: fullName.split("\\.")){
            pieces.add(retval);
            Logg.log("A Piece: " + retval);
        }

        String header = "";
        for(int i = 0; i < pieces.size() - 1; i++){
            header += pieces.get(i) + ".";
        }

        Logg.log("Header: " + header);

        legend = header;

        /*
        legend right
            Short
            legend
        endlegend

         */

        try {
            writer = new FileWriter(plantUML, true);
            writer.append("legend top" + '\n');
            writer.append((legend + '\n').replace("$", "_"));
            writer.append("endlegend" + '\n');
            Logg.log("legend top");
            Logg.log((legend).replace("$", "_"));
            Logg.log("endlegend");
            writer.close();
        }
        catch(java.io.IOException exception){
            Logg.log("Couldn't write to the plantUML file");
        }
    }
}
