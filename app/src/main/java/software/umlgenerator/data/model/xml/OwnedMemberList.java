package software.umlgenerator.data.model.xml;

import android.app.Fragment;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Caedus on 4/8/2016.
 */

@Root(name = "ownedMember")
public class OwnedMemberList /*extends ArrayList<BaseXMLElement>*/ {

    @Attribute(name = "xmi:id")
    String xmiid;

    @Attribute
    String name = "Interaction1";

    @Attribute
    String visibility = "public";

    @Attribute
    String isReentrant = "true";

    @Attribute(name = "xmi:type")
    String xmitype = "uml:Interaction";

    @ElementList(inline=true, type = ClassXMLElement.class)
    public ArrayList<ClassXMLElement> classes;

    @ElementList(inline=true, type = MethodXMLElement.class)
    public ArrayList<MethodXMLElement> methods;

    @ElementList(inline=true, type = FragmentXMLElement.class)
    public ArrayList<FragmentXMLElement> fragments;

    public OwnedMemberList() {
        xmiid = UUID.randomUUID().toString();
        classes = new ArrayList<ClassXMLElement>();
        methods = new ArrayList<MethodXMLElement>();
        fragments = new ArrayList<FragmentXMLElement>();
    }

}
