package software.umlgenerator.data;

import java.io.File;
import java.util.List;

import software.umlgenerator.data.model.parcelables.ParcelableClass;
import software.umlgenerator.data.model.parcelables.ParcelableMethod;

/**
 * Created by shorj_000 on 4/20/2016.
 */
public interface UtilityWriter {

    //markers for when classes and methods are called and end
    public void classStart(ParcelableClass parcelableClass);
    public void classEnd(ParcelableClass parcelableClass);
    public void methodStart(ParcelableMethod parcelableMethod);
    public void methodEnd(ParcelableMethod parcelableMethod);

    //start and end tags
    public void writeStart();
    public void writeEnd();

    //for addition feature (used in plantUML) early in the writing
    public void writeLegend(ParcelableClass first);

    //writing the values
    public void writeValue(ParcelableClass from, ParcelableClass to);
    public void writeValue(ParcelableClass from, ParcelableMethod method, ParcelableClass to);
}
