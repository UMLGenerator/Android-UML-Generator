package software.umlgenerator.data;

import software.umlgenerator.data.model.parcelables.ParcelableClass;
import software.umlgenerator.data.model.parcelables.ParcelableMethod;

/**
 * Created by shorj_000 on 4/20/2016.
 */
interface UtilityWriter {

    //start and end tags
    public void writeStart();
    public void writeEnd();

    //for the beggining and ending of classes
    public void writeClassStart(ParcelableClass parcelableClass);
    public void writeClassEnd(ParcelableClass parcelableClass);

    //for addition feature (used in plantUML) early in the writing
    public void writeLegend(ParcelableClass first);

    //writing the values
    public void writeValue(ParcelableClass from, ParcelableClass to);
    public void writeValue(ParcelableClass from, ParcelableMethod method, ParcelableClass to);
    public void writeReturnValue(ParcelableClass from, ParcelableMethod method, ParcelableClass to);
}
