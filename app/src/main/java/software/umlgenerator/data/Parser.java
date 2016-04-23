package software.umlgenerator.data;

import software.umlgenerator.data.model.parcelables.ParcelableClass;
import software.umlgenerator.data.model.parcelables.ParcelableMethod;

/**
 * Created by shorj_000 on 4/21/2016.
 */
interface Parser {

    //adds writers that the parser will give its data to
    public void addWriter(UtilityWriter writer);

    //start and stop for the program
    public void start();
    public void stop();

    //markers for when classes and methods are called and end
    public void classStart(ParcelableClass parcelableClass);
    public void classEnd(ParcelableClass parcelableClass);
    public void methodStart(ParcelableMethod parcelableMethod);
    public void methodEnd(ParcelableMethod parcelableMethod);
}
