package software.umlgenerator.io;

import java.io.File;
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

    public PackageHooker(XC_LoadPackage.LoadPackageParam param, File file) {
        loadPackageParam = param;
        fileManager = new FileManager(file);

        try {
            hookAll();
        } catch (IOException | ClassNotFoundException e) {
            Logg.log(e);
        }
    }

    private void hookAll() throws IOException, ClassNotFoundException {
        DexFile dexFile = new DexFile(loadPackageParam.appInfo.sourceDir);
        fileManager.test(loadPackageParam.packageName);
        Enumeration<String> classNames = dexFile.entries();
        while (classNames.hasMoreElements()) {
            String className = classNames.nextElement();

            if (isClassValid(className)) {
                Class clazz = Class.forName(className, false, loadPackageParam.classLoader);

                for (Method method: clazz.getDeclaredMethods()) {
                    if (isMethodValid(method)) {

                        XposedBridge.hookMethod(method, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                super.beforeHookedMethod(param);
                                Logg.log("BEFORE: " + param.method.getName());
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                Logg.log("AFTER: " + param.method.getName());
                            }
                        });
                    }
                }
            }
        }
    }

    private boolean isClassValid(String className) {
        return className.startsWith(loadPackageParam.packageName) // Only listen to package classes
                && !className.contains("BuildConfig") // Android class that isn't actually used
                && !className.equals(loadPackageParam.packageName + ".R"); // ^ same here
    }

    private boolean isMethodValid(Method method) {
        return !Modifier.isAbstract(method.getModifiers());
    }
}
