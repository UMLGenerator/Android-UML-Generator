package software.umlgenerator.xposed.model.xml;

import software.umlgenerator.xposed.model.parcelables.ParcelableClass;

/**
 * Created by mbpeele on 2/26/16.
 */
public class ClassXMLElement extends BaseXMLElement<ParcelableClass> {

    public ClassXMLElement(ParcelableClass parcelableClass) {
        super(parcelableClass.getName());
        setData(parcelableClass);
    }
}
