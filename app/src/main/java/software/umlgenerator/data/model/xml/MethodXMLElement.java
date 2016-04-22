package software.umlgenerator.data.model.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.util.UUID;

import software.umlgenerator.data.model.parcelables.ParcelableMethod;

/**
 * Created by mbpeele on 2/26/16.
 */

@Root(name = "message")
public class MethodXMLElement /*extends BaseXMLElement<ParcelableMethod>*/ {

    @Attribute(name = "xmi:id")
    String xmiid;

    @Attribute
    String name;

    @Attribute
    String visibility = "public";

    @Attribute(name = "xmi:type")
    String xmitype = "uml:Message";

    @Attribute
    String messageSort;     //synchCall by default?

    @Attribute
    String messageKind = "complete";

    @Attribute
    String receiveEvent;

    @Attribute
    String sendEvent;

    public MethodXMLElement(ParcelableMethod parcelableMethod) {
        //super(parcelableMethod.getMethodName());
        //setData(parcelableMethod);
        name = parcelableMethod.getMethodName();
        xmiid = UUID.randomUUID().toString();
        messageSort = "sycnhCall";
    }

    public void setSendEvent(String sendid) {
        sendEvent = sendid;
    }

    public void setReceiveEvent(String receiveid) {
        receiveEvent = receiveid;
    }
}
