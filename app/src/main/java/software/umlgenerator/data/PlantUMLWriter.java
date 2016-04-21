package software.umlgenerator.data;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import software.umlgenerator.data.model.parcelables.ParcelableClass;
import software.umlgenerator.data.model.parcelables.ParcelableMethod;
import software.umlgenerator.util.Logg;

/**
 * Created by shorj_000 on 4/20/2016.
 */
public class PlantUMLWriter implements UtilityWriter {

    private FileWriter writer;
    private File file;
    private List<ParcelableClass> classList;
    private List<ParcelableMethod> method;
    private ParcelableClass targetClass;
    private Boolean usedMethod = false;
    private Boolean firstClass = true;
    private String legend;

    //gives the writer a file to write to
    public PlantUMLWriter(File fl){
        file = fl;
        classList = new ArrayList<ParcelableClass>();
        method = new ArrayList<ParcelableMethod>();

    }

    //markers for when classes and methods are called and end
    public void classStart(ParcelableClass parcelableClass){
        targetClass = parcelableClass;
        //If very first class
        if(firstClass){
            writeLegend(targetClass);
            firstClass = false;
        }

        //If first class, only have to add it to the list
        if(classList.isEmpty()){
            classList.add(targetClass);
            writeValue(targetClass, targetClass);
            writeClassStart(targetClass);
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
                        writeValue(fromClass, method.get(0), targetClass);
                        writeClassStart(targetClass);
                        usedMethod = true;
                    } catch (NullPointerException error) {
                        //System.out.println(fromClass.getName());
                        Logg.log("could not find the method's class in the class list");
                    }
                }

            }
            else{
                writeValue(classList.get(0), targetClass);
                writeClassStart(targetClass);
            }
        }
    }
    public void classEnd(ParcelableClass parcelableClass){
        writeClassEnd(parcelableClass);
    }
    public void methodStart(ParcelableMethod parcelableMethod){
        usedMethod = false;
        method.add(0, parcelableMethod);
    }
    public void methodEnd(ParcelableMethod parcelableMethod){
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
                    writeValue(fromClass, method.get(0), fromClass);
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

    //start and end tags
    public void writeStart(){
        writeLine("@startuml", false);
    }
    public void writeEnd(){
        writeLine("@enduml");
    }

    //start and end markers for classes
    public void writeClassStart(ParcelableClass start){

        String myString = ("activate " + start.getName().replaceAll(legend, "")).replace("$", "_");

        writeLine(myString);
    }
    public void writeClassEnd(ParcelableClass end){

        String myString = ("deactivate " + end.getName().replaceAll(legend, "")).replace("$", "_");

        writeLine(myString);
    }

    //for addition feature (used in plantUML) early in the writing
    public void writeLegend(ParcelableClass first){
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

        writeLine("legend top");
        writeLine(legend.replace("$", "_"));
        writeLine("endlegend");
    }

    //writing the values
    public void writeValue(ParcelableClass from, ParcelableClass to){
        //writes the line for PlantUML when there is a class that instantiates another from a declaration

        String myString = (from.getName().replaceAll(legend, "") + " -> "
                + to.getName().replaceAll(legend, "")).replace("$", "_");

        writeLine(myString);
    }
    public void writeValue(ParcelableClass from, ParcelableMethod method, ParcelableClass to){
        //Constructs the line needing to be written for the method, using the from and to classes

        String myString = (from.getName().replaceAll(legend, "") + " -> " +
                to.getName().replaceAll(legend, "") + ": " +
                method.getMethodName().replaceAll(legend, "")).replace("$", "_");

        writeLine(myString);
    }

    //actual writer
    public void writeLine(String write){
        try{
            writer = new FileWriter(file, true);
            writer.append(write + '\n');
            writer.close();
            Logg.log(write);
        }
        catch(java.io.IOException exception){
            Logg.log("Couldn't write to the plantUML file");
        }
    }
    public void writeLine(String write, Boolean append){
        try{
            writer = new FileWriter(file, append);
            writer.append(write + '\n');
            writer.close();
            Logg.log(write);
        }
        catch(java.io.IOException exception){
            Logg.log("Couldn't write to the plantUML file");
        }
    }
}
