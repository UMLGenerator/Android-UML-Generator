package software.umlgenerator.io;

import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * Created by mbpeele on 3/4/16.
 */
public interface FileManagerInterface {

    void writePackageElement(String packageName);

    void writeClassElement(Class clazz);

    void writeMethodElement(Member method);
}
