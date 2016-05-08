package software.umlgenerator.data;

import java.util.ArrayList;
import java.util.List;

import software.umlgenerator.data.model.parcelables.ParcelableClass;
import software.umlgenerator.data.model.parcelables.ParcelableMethod;
import software.umlgenerator.data.ParcelableHolder;
import software.umlgenerator.util.Logg;

/**
 * Created by shorj_000 on 4/21/2016.
 */
class LogicalParser implements Parser {

    private List<ParcelableClass> classList;
    private List<ParcelableMethod> method;
    private ParcelableClass targetClass;
    private Boolean usedMethod = false;
    private Boolean firstClass = true;
    private ArrayList<UtilityWriter> writers;
    private ArrayList<ParcelableHolder> returnList;
    private ArrayList<ParcelableClass> classOrder;

    public LogicalParser(){
        writers = new ArrayList<UtilityWriter>();
        classList = new ArrayList<ParcelableClass>();
        method = new ArrayList<ParcelableMethod>();
        returnList = new ArrayList<ParcelableHolder>();
        classOrder = new ArrayList<ParcelableClass>();
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
        classOrder.add(0, targetClass);
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
                        Logg.log("could not find the method's class in the class list, 1");
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
    public void classEnd(ParcelableClass parcelableClass) {

        for(int i = 0; i < classOrder.size(); i++){
            if(classOrder.get(i).getName().equals(parcelableClass.getName())) {
                Logg.log("removing class: " + parcelableClass.getName());
                classOrder.remove(i);
            }
        }

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
                    Logg.log("Method: " + method.get(0).getMethodName() + ", returnType: " + method.get(0).getReturnType());
                    if(!method.get(0).getReturnType().equals("void")){
                        Logg.log("Return type not void for method: " + method.get(0).getMethodName());
                        if(classOrder.size() >= 1) {
                            Logg.log("1 recording values: " + classOrder.get(0).getName() + ", " + method.get(0).getMethodName() + ", " + fromClass.getName());
                            returnList.add(new ParcelableHolder(classOrder.get(0), method.get(0), fromClass));
                            for(int i = 0; i < writers.size(); i++){
                                writers.get(i).writeValue(classOrder.get(0), method.get(0), fromClass);
                            }
                            if(classOrder.get(0) != fromClass){
                                for(int i = 0; i < writers.size(); i++){
                                    writers.get(i).writeClassStart(fromClass);
                                }
                            }
                        }
                        else{
                            Logg.log("2 recording values: " + fromClass.getName() + ", " + method.get(0).getMethodName() + ", " + fromClass.getName());
                            returnList.add(new ParcelableHolder(fromClass, method.get(0), fromClass));
                            for(int i = 0; i < writers.size(); i++){
                                writers.get(i).writeValue(fromClass, method.get(0), fromClass);
                            }
                        }
                    }
                    else {
                        for (int i = 0; i < writers.size(); i++) {
                            writers.get(i).writeValue(fromClass, method.get(0), fromClass);
                        }
                    }
                } catch (NullPointerException error) {
                    //System.out.println(fromClass.getName());
                    Logg.log("could not find the method's class in the class list, 2");
                }
            }
            usedMethod = true;
        }

        for(int i = 0; i < returnList.size(); i++){
            if(returnList.get(i).getMethod().getMethodName().equals(parcelableMethod.getMethodName())){
                ParcelableHolder holder = returnList.get(i);
                for(int y = 0; y < writers.size(); y++){
                    writers.get(y).writeReturnValue(holder.getTo(), holder.getMethod(), holder.getFrom());
                    if(holder.getTo() != holder.getFrom()) {
                        writers.get(i).writeClassEnd(holder.getTo());
                    }
                }
                returnList.remove(i);
            }
        }

        for(int i = 0; i < method.size(); i++){
            if(method.get(i).getMethodName().equals(parcelableMethod.getMethodName())) {
                Logg.log("removing method: " + parcelableMethod.getMethodName());
                method.remove(i);
            }
        }
    }
}
