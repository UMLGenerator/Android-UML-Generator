package software.umlgenerator.xposed.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

import software.umlgenerator.xposed.model.ClassElement;

/**
 * Created by mbpeele on 2/26/16.
 */
@Root
public class PackageElement {

    @ElementList(inline=true)
    private List<ClassElement> list;

    @Attribute
    private String name;

    public PackageElement(String packageName) {
        list = new ArrayList<>();
        setName(packageName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ClassElement> getList() {
        return list;
    }

    public void addClassElement(ClassElement baseElement) {
        String name = baseElement.getName();
        if (!name.contains("$")) {
            list.add(baseElement);
        }
    }
}
