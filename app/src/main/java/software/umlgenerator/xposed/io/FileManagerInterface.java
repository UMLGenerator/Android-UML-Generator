package software.umlgenerator.xposed.io;

import java.lang.reflect.Member;

/**
 * Created by mbpeele on 3/4/16.
 */
interface FileManagerInterface {

    void writePackageElement(String packageName);

    void writeClassElement(Class clazz);

    void writeMethodElement(Member method);
}
