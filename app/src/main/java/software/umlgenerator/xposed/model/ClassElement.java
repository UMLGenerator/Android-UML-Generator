package software.umlgenerator.xposed.model;

import org.simpleframework.xml.Attribute;

/**
 * Created by mbpeele on 2/26/16.
 */
public class ClassElement extends BaseElement {

    @Attribute
    private String name;

    public ClassElement(Class clazz) {
        setName(clazz.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
