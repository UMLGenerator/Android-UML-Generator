package software.umlgenerator.data.model.xml;

import software.umlgenerator.data.model.parcelables.ParcelableClass;

/**
 * Created by mbpeele on 2/26/16.
 */
public class ClassXMLElement extends BaseXMLElement<ParcelableClass> {

    public ClassXMLElement(ParcelableClass parcelableClass) {
        super(parcelableClass.getName());
        setData(parcelableClass);
    }
}
