package software.umlgenerator.xposed.model.xml;

import software.umlgenerator.xposed.model.parcelables.ParcelableMethod;

/**
 * Created by mbpeele on 2/26/16.
 */
public class MethodXMLElement extends BaseXMLElement<ParcelableMethod> {

    public MethodXMLElement(ParcelableMethod parcelableMethod) {
        super(parcelableMethod.getMethodName());
        setData(parcelableMethod);
    }
}
