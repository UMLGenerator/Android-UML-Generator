package software.umlgenerator;

/**
 * Created by TimFulton on 2/10/16.
 */

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class LoadPackage implements IXposedHookLoadPackage {

    final String PACKAGE_NAME = "miles.xposedtest";

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals(PACKAGE_NAME))
            return;

        DexFile dexFile = new DexFile(lpparam.appInfo.sourceDir);
        Enumeration<String> classNames = dexFile.entries();
        while (classNames.hasMoreElements()) {
            String className = classNames.nextElement();

            if (isClassNameValid(className)) {
                Class clazz = Class.forName(className, false, lpparam.classLoader);

                for (Method method: clazz.getDeclaredMethods()) {

                    XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Logg.log("HOOKED: " + param.method.getName());
                        }
                    });
                }
            }
        }
    }

    public boolean isClassNameValid(String className) {
        return className.startsWith(PACKAGE_NAME) && !className.contains("$") &&
                !className.contains("BuildConfig") && !className.equals(PACKAGE_NAME + ".R");
    }
}
