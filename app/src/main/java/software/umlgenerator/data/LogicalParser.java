package software.umlgenerator.data;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import software.umlgenerator.data.model.parcelables.ParcelableClass;
import software.umlgenerator.data.model.parcelables.ParcelableMethod;
import software.umlgenerator.util.Logg;

/**
 * Created by shorj_000 on 4/21/2016.
 */
public class LogicalParser implements Parser {

    private List<ParcelableClass> classList;
    private List<ParcelableMethod> method;
    private ParcelableClass targetClass;
    private Boolean usedMethod = false;
    private Boolean firstClass = true;
    private String legend;
    private ArrayList<UtilityWriter> writers;

    public LogicalParser(){
        writers = new ArrayList<UtilityWriter>();
        classList = new ArrayList<ParcelableClass>();
        method = new ArrayList<ParcelableMethod>();
    }

    public void addWriter(UtilityWriter writer){
        writers.add(writer);
    }

    //start and stop for the program
    public void start(){
        for(int i = 0; i < writers.size(); i++){
            writers.get(i).writeStart();
        }
    }
    public void stop(){
        for(int i = 0; i < writers.size(); i++){
            writers.get(i).writeEnd();
        }
    }

    //markers for when classes and methods are called and end
    public void classStart(ParcelableClass parcelableClass){
        targetClass = parcelableClass;
        //If very first class
        if(firstClass){
            for(int i = 0; i < writers.size(); i++){
                writers.get(i).writeLegend(targetClass);
            }
            firstClass = false;
        }

        //If first class, only have to add it to the list
        if(classList.isEmpty()){
            classList.add(targetClass);
            for(int i = 0; i < writers.size(); i++){
                writers.get(i).writeValue(targetClass, targetClass);
                writers.get(i).writeClassStart(targetClass);
            }
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
                        for(int i = 0; i < writers.size(); i++){
                            writers.get(i).writeValue(fromClass, method.get(0), targetClass);
                            writers.get(i).writeClassStart(targetClass);
                        }
                        usedMethod = true;
                    } catch (NullPointerException error) {
                        //System.out.println(fromClass.getName());
                        Logg.log("could not find the method's class in the class list");
                    }
                }

            }
            else{
                for(int i = 0; i < writers.size(); i++){
                    writers.get(i).writeValue(classList.get(0), targetClass);
                    writers.get(i).writeClassStart(targetClass);
                }
            }
        }
    }
    public void classEnd(ParcelableClass parcelableClass){
        for(int i = 0; i < writers.size(); i++){
            writers.get(i).writeClassEnd(parcelableClass);
        }
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
                    for(int i = 0; i < writers.size(); i++){
                        writers.get(i).writeValue(fromClass, method.get(0), fromClass);
                    }
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
}
