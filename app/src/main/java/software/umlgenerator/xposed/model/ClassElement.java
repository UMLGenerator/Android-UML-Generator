package software.umlgenerator.xposed.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbpeele on 2/26/16.
 */
public class ClassElement extends BaseElement<Class> {

    public ClassElement(Class clazz) {
        super(clazz);
        setName(clazz.getName());
    }
}
