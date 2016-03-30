package software.umlgenerator.xposed.loaders;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import software.umlgenerator.util.Logg;
import software.umlgenerator.util.ReflectionUtils;
import software.umlgenerator.xposed.io.FileManager;
import software.umlgenerator.xposed.model.ClassElement;
import software.umlgenerator.xposed.model.MethodElement;
import software.umlgenerator.xposed.model.PackageElement;

/**
 * Created by mbpeele on 2/24/16.
 */
public class HookService {

    private HookService() {
    }

    private static long beforeMethod, afterMethod;

    public static void hookAll(XC_LoadPackage.LoadPackageParam loadPackageParam) throws IOException, ClassNotFoundException {
        final FileManager fileManager = new FileManager(FileManager.getFile(loadPackageParam.packageName));

        DexFile dexFile = new DexFile(loadPackageParam.appInfo.sourceDir);
        Enumeration<String> classNames = dexFile.entries();
        while (classNames.hasMoreElements()) {
            final String className = classNames.nextElement();

            if (ReflectionUtils.isClassValid(loadPackageParam.packageName, className)) {
                final Class clazz = Class.forName(className, false, loadPackageParam.classLoader);

                XposedBridge.hookAllConstructors(clazz, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Logg.log("CONSTRUCTOR: ", className);
                        fileManager.writeClassElement(new ClassElement(clazz));
                    }
                });

                for (final Method method: clazz.getDeclaredMethods()) {
                    if (ReflectionUtils.isMethodValid(method)) {
                        XposedBridge.hookMethod(method, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                Logg.log("BEFORE: ", method.getName());
                                fileManager.writeMethodElement(new MethodElement(method));
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                Logg.log("AFTER: ", method.getName());
                            }
                        });
                    }
                }
            }
        }
    }
}
