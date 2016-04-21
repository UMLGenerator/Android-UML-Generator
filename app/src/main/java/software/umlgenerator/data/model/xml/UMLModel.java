package software.umlgenerator.data.model.xml;

import org.simpleframework.xml.*;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Caedus on 4/8/2016.
 */

@Root(name = "uml:Model")
public class UMLModel extends ArrayList<PackagedElement> {

    @Attribute(name = "xmi:id")
    String xmiid;

    @Attribute(name = "xmi:type")
    String xmitype = "uml:Model";

    @Attribute
    String name = "RootModel";

    public UMLModel() {
        xmiid = UUID.randomUUID().toString();
    }
}
