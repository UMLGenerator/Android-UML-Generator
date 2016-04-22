package software.umlgenerator.data.model.xml;

import org.simpleframework.xml.*;

import java.util.UUID;

/**
 * Created by Caedus on 4/22/2016.
 */
@Root(name = "ownedAttribute")
public class OwnedAttribute {

    @Attribute
    String xmiid;

    @Attribute
    String name;

    @Attribute
    String visibility = "public";

    @Attribute
    String isStatic = "false";

    @Attribute
    String isLeaf = "false";

    @Attribute
    String isReadOnly = "false";

    @Attribute
    String isOrdered = "false";

    @Attribute
    String isUnique = "false";

    @Attribute(name = "xmi:type")
    String xmitype = "uml:Propety";

    @Attribute
    String aggregation = "none";

    @Attribute
    String isDervied = "false";

    @Attribute
    String isId = "false";

    public OwnedAttribute(String name) {
        this.name = name;
        xmiid = UUID.randomUUID().toString();
    }

}
