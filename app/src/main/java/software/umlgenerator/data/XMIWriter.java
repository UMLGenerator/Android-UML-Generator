package software.umlgenerator.data;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import software.umlgenerator.data.model.parcelables.ParcelableClass;
import software.umlgenerator.data.model.parcelables.ParcelableFragment;
import software.umlgenerator.data.model.parcelables.ParcelableMethod;
import software.umlgenerator.data.model.xml.ClassXMLElement;
import software.umlgenerator.data.model.xml.FragmentXMLElement;
import software.umlgenerator.data.model.xml.MethodXMLElement;
import software.umlgenerator.data.model.xml.OwnedAttribute;
import software.umlgenerator.data.model.xml.OwnedMemberList;
import software.umlgenerator.data.model.xml.PackagedElement;
import software.umlgenerator.data.model.xml.UMLModel;
import software.umlgenerator.data.model.xml.XMI;
import software.umlgenerator.data.model.xml.XMIDocumentation;
import software.umlgenerator.util.Logg;

/**
 * Created by Caedus on 4/21/2016.
 */
class XMIWriter implements UtilityWriter {

    private File file;
    Serializer serializer = new Persister();

    Hashtable<String, ClassXMLElement> classDict = new Hashtable<String, ClassXMLElement>();
    ArrayList<MethodXMLElement> methodList = new ArrayList<MethodXMLElement>();
    ArrayList<FragmentXMLElement> fragList = new ArrayList<FragmentXMLElement>();

    public XMIWriter(File file) {
        this.file = file;
    }

    //start and end tags
    public void writeStart() {
        //do nothing
    }
    public void writeEnd() {
        //TO WRITE FILE:
        //populate OwnedMemberList
        OwnedMemberList memberList = new OwnedMemberList();

        for (ClassXMLElement c : classDict.values()) {
            memberList.classes.add(c);
        }

        for (MethodXMLElement m : methodList) {
            memberList.methods.add(m);
        }

        for (FragmentXMLElement f : fragList) {
            memberList.fragments.add(f);
        }

        //setup tags, etc
        PackagedElement pEl1= new PackagedElement("Model", "uml:Model");
        PackagedElement pEl2 = new PackagedElement("Collaboration1", "false", "false", "false", "uml:Collaboration", memberList, new OwnedAttribute("role1"), new OwnedAttribute("role2"));

        UMLModel umlModel = new UMLModel();
        umlModel.add(pEl1);
        umlModel.add(pEl2);

        XMI xmi = new XMI(new XMIDocumentation(), umlModel);

        try {
            serializer.write(xmi, file);
        } catch (Exception e) {
            e.printStackTrace();
            Logg.log("Failed to write XMI file");
        }
    }

    //for the beggining and ending of classes
    public void writeClassStart(ParcelableClass parcelableClass) {
        //don't need to do anything
    }

    public void writeClassEnd(ParcelableClass parcelableClass) {
        //don't need to do anything
    }

    //for addition feature (used in plantUML) early in the writing
    public void writeLegend(ParcelableClass first) {
        //nothing needed
    }

    //writing the values
    public void writeValue(ParcelableClass from, ParcelableClass to) {
        //ClassXMLElement clazz1 = checkDict(from);
        //ClassXMLElement clazz2 = checkDict(to);

        /*FragmentXMLElement frag1 = new FragmentXMLElement(new ParcelableFragment(from.getClass()));
        frag1.setCovered(clazz1.getXMIID());
        fragList.add(frag1);

        FragmentXMLElement frag2 = new FragmentXMLElement(new ParcelableFragment(to.getClass()));
        frag2.setCovered(clazz2.getXMIID());
        fragList.add(frag2);*/

        //method later
    }

    public void writeValue(ParcelableClass from, ParcelableMethod method, ParcelableClass to) {
        ClassXMLElement clazz1 = checkDict(from);
        ClassXMLElement clazz2 = checkDict(to);

        FragmentXMLElement frag1 = new FragmentXMLElement(new ParcelableFragment(from.getClass()));
        frag1.setCovered(clazz1.getXMIID());
        fragList.add(frag1);

        FragmentXMLElement frag2 = new FragmentXMLElement(new ParcelableFragment(to.getClass()));
        frag2.setCovered(clazz2.getXMIID());
        fragList.add(frag2);

        MethodXMLElement meth = new MethodXMLElement(method);
        meth.setSendEvent(frag1.getXMIID());
        meth.setReceiveEvent(frag2.getXMIID());
        methodList.add(meth);
    }

    private ClassXMLElement checkDict(ParcelableClass clazz) {
        if (!classDict.containsKey(clazz.getName())) classDict.put(clazz.getName(), new ClassXMLElement(clazz));
        return classDict.get(clazz.getName());
    }
}
