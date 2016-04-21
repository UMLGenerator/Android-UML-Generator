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
    private String legend;

    //gives the writer a file to write to
    public PlantUMLWriter(File fl){
        file = fl;
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
