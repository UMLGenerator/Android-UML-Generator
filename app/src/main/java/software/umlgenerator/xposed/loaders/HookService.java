package software.umlgenerator.xposed.loaders;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import dalvik.system.DexFile;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import software.umlgenerator.util.Logg;
import software.umlgenerator.util.ReflectionUtils;
import software.umlgenerator.xposed.io.FileManager;

/**
 * Created by mbpeele on 2/24/16.
 */
public class HookService {

    public static void hookAll(XC_LoadPackage.LoadPackageParam loadPackageParam) throws IOException, ClassNotFoundException {
        FileManager fileManager = new FileManager(FileManager.getFile(loadPackageParam.packageName));

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
                    }
                });

                for (Method method: clazz.getDeclaredMethods()) {
                    if (ReflectionUtils.isMethodValid(method)) {

                        XposedBridge.hookMethod(method, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
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
}
