package software.umlgenerator.data.model.xml;

import org.simpleframework.xml.*;

/**
 * Created by Caedus on 4/8/2016.
 */

@Root(name = "packagedElement")
public class PackagedElement {

    @Attribute(name = "xmi:id")
    String xmiid;

    @Attribute
    String name;

    @Attribute
    String visibility = "public";

    @Attribute(required = false)
    String isAbstract = "false";

    @Attribute(required = false)
    String isFinalSpecification = "false";

    @Attribute(required = false)
    String isLeaf = "false";

    @Attribute(name = "xmi:type")
    String xmitype; //= "uml:Collaboration"; = "uml:Model"

    @ElementList(required = false)
    OwnedMemberList ownedMemberList;
}
