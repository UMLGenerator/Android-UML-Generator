package software.umlgenerator.xposed.io;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by mbpeele on 2/26/16.
 */
@Root
class PackageElement {

    @Attribute
    private String packageName;

    public PackageElement(String packageName) {
        this.packageName = packageName;
    }
}
