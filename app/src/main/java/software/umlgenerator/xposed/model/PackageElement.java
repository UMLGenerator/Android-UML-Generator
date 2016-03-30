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
public class PackageElement extends BaseElement<String> {

    public PackageElement(String packageName) {
        super(packageName);
        setName(packageName);
    }

    @Override
    public void addElement(BaseElement baseElement) {
        if (baseElement instanceof ClassElement) {
            addClassElement((ClassElement) baseElement);
            return;
        }

        if (baseElement instanceof MethodElement) {
            addMethodElement((MethodElement) baseElement);
            return;
        }

        throw new IllegalArgumentException("PackageElement list can only contain ClassElement and" +
                "MethodElement objects");
    }

    public void addClassElement(ClassElement baseElement) {
        String name = baseElement.getName();
        if (!name.contains("$")) {
            getList().add(baseElement);
        }
    }

    public void addMethodElement(MethodElement methodElement) {
        String declaringClassName = methodElement.getData().getDeclaringClass().getName();
        for (BaseElement element: getList()) {
            ClassElement classElement = (ClassElement) element; // cast is ok, any direct child of this list is a ClassElement
            if (classElement.getName().equals(declaringClassName)) {
                element.addElement(methodElement);
                return;
            }
        }
    }
}
