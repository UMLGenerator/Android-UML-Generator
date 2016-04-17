package software.umlgenerator.data.model.xml;

import org.simpleframework.xml.*;

/**
 * Created by Caedus on 4/15/2016.
 */

@Root(name = "xmi:XMI")
public class XMI {

    @Attribute(name = "xmi:version")
    String xmiversion = "2.1";

    @Attribute(name = "xmlns:uml")
    String xmlnsuml = "http://schema.omg.org/spec/UML/2.0";

    @Attribute(name = "xmlns:xmi")
    String xmlnsxmi = "http://schema.omg.org/spec/XMI/2.1";

    @Element
    XMIDocumentation documentation;

    @ElementList
    UMLModel model;

    public XMI(XMIDocumentation xmidoc, UMLModel umlModel) {
        documentation = xmidoc;
        model = umlModel;
    }

}
