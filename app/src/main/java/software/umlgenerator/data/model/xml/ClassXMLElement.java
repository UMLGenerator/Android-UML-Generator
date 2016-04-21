package software.umlgenerator.data.model.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import software.umlgenerator.data.model.parcelables.ParcelableClass;

import java.util.*;

/**
 * Created by mbpeele on 2/26/16.
 */

@Root(name = "lifeline")
public class ClassXMLElement extends BaseXMLElement<ParcelableClass> {

    @Attribute(name = "xmi:id")
    String xmiid;

    @Attribute
    String name;

    @Attribute
    String visibility = "public";

    @Attribute(name = "xmi:type")
    String xmitype = "uml:Lifeline";

    @Attribute
    String represents;

    public ClassXMLElement(ParcelableClass parcelableClass) {
        super(parcelableClass.getName());
        setData(parcelableClass);
        name = parcelableClass.getName();
        xmiid = UUID.randomUUID().toString();
    }

    public String getXMIID() {
        return xmiid;
    }
}
