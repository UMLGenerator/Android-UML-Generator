package software.umlgenerator.data.model.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 * Created by Caedus on 4/8/2016.
 */

@Root(name = "ownedMember")
public class OwnedMemberList extends ArrayList<BaseXMLElement> {

    @Attribute(name = "xmi:id")
    String xmiid;

    @Attribute
    String name;

    @Attribute
    String visibility = "public";

    @Attribute
    String isReentrant = "true";

    @Attribute(name = "xmi:type")
    String xmitype = "uml:Interaction";

}
