package software.umlgenerator.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by mbpeele on 3/29/16.
 */
public class ReflectionUtils {

    private ReflectionUtils() {

    }

    public static boolean isClassValid(String packageName, String className) {
        return className.startsWith(packageName) // Only listen to package classes
                && !className.contains("BuildConfig") // Android class that isn't actually used
                && !className.startsWith(packageName + ".R$") // ^ same here
                && !className.equals(packageName + ".R");
    }

    public static boolean isMethodValid(Method method) {
        return !Modifier.isAbstract(method.getModifiers());
    }
}
