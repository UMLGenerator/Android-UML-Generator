package software.umlgenerator.io;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;

import dalvik.system.DexFile;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import software.umlgenerator.model.ClassElement;
import software.umlgenerator.util.Logg;

/**
 * Created by mbpeele on 2/24/16.
 */
public class PackageHooker {

    private final XC_LoadPackage.LoadPackageParam loadPackageParam;
    private FileManager fileManager;

    public PackageHooker(XC_LoadPackage.LoadPackageParam param, String filePath) {
        loadPackageParam = param;
        fileManager = new FileManager(filePath);

        try {
            hookAll();
        } catch (IOException | ClassNotFoundException e) {
            Logg.log(e);
        }
    }

    public void hookAll() throws IOException, ClassNotFoundException {
        DexFile dexFile = new DexFile(loadPackageParam.appInfo.sourceDir);
        Enumeration<String> classNames = dexFile.entries();
        while (classNames.hasMoreElements()) {
            String className = classNames.nextElement();

            if (isClassValid(className)) {
                Class clazz = Class.forName(className, false, loadPackageParam.classLoader);

                final ClassElement classElement = new ClassElement(clazz);

                for (Method method: clazz.getDeclaredMethods()) {
                    if (isMethodValid(method)) {

                        XposedBridge.hookMethod(method, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                Logg.log("HOOKED: " + param.method.getName());
                                classElement.addMethod(method);
                            }
                        });
                    }
                }
            }
        }
    }

    public boolean isClassValid(String className) {
        return className.startsWith(loadPackageParam.packageName) // Only listen to package classes
                && !className.contains("BuildConfig") // Android class that isn't actually used
                && !className.equals(loadPackageParam.packageName + ".R"); // ^ same here
    }

    public boolean isMethodValid(Method method) {
        return !Modifier.isAbstract(method.getModifiers());
    }
}
