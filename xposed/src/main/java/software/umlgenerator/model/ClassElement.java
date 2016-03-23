package software.umlgenerator.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import org.simpleframework.xml.*;

/**
 * Created by mbpeele on 2/26/16.
 */
@Root
public class ClassElement {

    @Attribute
    private String className;

    public ClassElement(Class clazz) {
        className = clazz.getName();
    }
}
