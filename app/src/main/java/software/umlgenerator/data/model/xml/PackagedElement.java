package software.umlgenerator.data.model.xml;

import org.simpleframework.xml.*;

/**
 * Created by Caedus on 4/8/2016.
 */

@Root(name = "packagedElement")
public class PackagedElement /*extends BaseXMLElement*/ { //add extending

    @Attribute(name = "xmi:id")
    String xmiid;

    @Attribute
    String name;

    @Attribute
    String visibility = "public";

    @Attribute(required = false)
    String isAbstract;

    @Attribute(required = false)
    String isFinalSpecification;

    @Attribute(required = false)
    String isLeaf;

    @Attribute(name = "xmi:type")
    String xmitype; //= "uml:Collaboration"; = "uml:Model"

    @ElementList(required = false)
    OwnedMemberList ownedMemberList;

    public PackagedElement(String xmitype, String name) {
        this.xmitype = xmitype;
        this.name = name;
    }

    public PackagedElement(String name, String isAbstract, String isFinalSpecification, String isLeaf, String xmitype, OwnedMemberList memberList) {
        this.name = name;
        this.isAbstract = isAbstract;
        this.isFinalSpecification = isFinalSpecification;
        this.isLeaf = isLeaf;
        this.xmitype = xmitype;
        ownedMemberList = memberList;
    }
}
