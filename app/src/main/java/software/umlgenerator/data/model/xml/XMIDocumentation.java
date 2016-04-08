package software.umlgenerator.data.model.xml;

import org.simpleframework.xml.*;

/**
 * Created by Caedus on 4/8/2016.
 */

@Root(name = "xmi:Documentaton")
public class XMIDocumentation {

    @Attribute
    String exporter = "StarUML";

    @Attribute
    String exporterVersion = "2.0";
}
