package software.umlgenerator.data.model.xml;

import software.umlgenerator.data.model.parcelables.ParcelablePackage;

/**
 * Created by mbpeele on 2/26/16.
 */
public class PackageXMLElement extends BaseXMLElement<ParcelablePackage> {

    public PackageXMLElement(String name) {
        super(name);
    }

    public PackageXMLElement(ParcelablePackage parcelablePackage) {
        super(parcelablePackage.getPackageName());
        setData(parcelablePackage);
    }

    @Override
    public void addElement(BaseXMLElement baseXMLElement) {
        if (baseXMLElement instanceof ClassXMLElement) {
            addClassElement((ClassXMLElement) baseXMLElement);
            return;
        }

        if (baseXMLElement instanceof MethodXMLElement) {
            addMethodElement((MethodXMLElement) baseXMLElement);
            return;
        }

        throw new IllegalArgumentException("PackageXMLElement list can only contain ClassXMLElement and" +
                "MethodXMLElement objects");
    }

    public void addClassElement(ClassXMLElement baseElement) {
        String name = baseElement.getName();
        if (!name.contains("$")) {
            getList().add(baseElement);
        }
    }

    public void addMethodElement(MethodXMLElement methodElement) {
        String declaringClassName = methodElement.getData().getDeclaringClassName();
        for (BaseXMLElement element: getList()) {
            ClassXMLElement classElement = (ClassXMLElement) element; // cast is ok, any direct child of this list is a ClassXMLElement
            if (classElement.getName().equals(declaringClassName)) {
                element.addElement(methodElement);
                return;
            }
        }
    }
}
