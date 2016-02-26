package software.umlgenerator.model;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by mbpeele on 2/26/16.
 */
public class ClassElement {

    final String className;
    final ArrayList<Method> classMethods;

    public ClassElement(Class clazz) {
        className = clazz.getName();
        classMethods = new ArrayList<>();
    }

    public void addMethod(Method method) {
        classMethods.add(method);
    }
}
