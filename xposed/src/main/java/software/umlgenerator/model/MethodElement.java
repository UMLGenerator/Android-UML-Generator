package software.umlgenerator.model;
import java.lang.reflect.*;
import org.simpleframework.xml.*;

/**
 * Created by mbpeele on 2/26/16.
 */

@Root
public class MethodElement {

    @Attribute
    private String methodName;

    public MethodElement(Member method) {
        methodName = method.getName();
    }
}
