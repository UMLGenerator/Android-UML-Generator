package software.umlgenerator.data.model.xml;

import org.simpleframework.xml.*;

import java.util.UUID;

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

    @Element(required = false, name = "ownedMember")
    OwnedMemberList ownedMemberList;

    @Element(name = "ownedAttribute", required = false)
    OwnedAttribute role1;

    @Element(name = "ownedAttribute ", required = false)
    OwnedAttribute role2;

    public PackagedElement(String name, String xmitype) {
        this.xmitype = xmitype;
        this.name = name;
        xmiid = UUID.randomUUID().toString();
    }

    public PackagedElement(String name, String isAbstract, String isFinalSpecification, String isLeaf, String xmitype, OwnedMemberList memberList, OwnedAttribute role1, OwnedAttribute role2) {
        this.name = name;
        this.isAbstract = isAbstract;
        this.isFinalSpecification = isFinalSpecification;
        this.isLeaf = isLeaf;
        this.xmitype = xmitype;
        ownedMemberList = memberList;
        this.role1 = role1;
        this.role2 = role2;
        xmiid = UUID.randomUUID().toString();
    }
}
