package software.umlgenerator.io;

/**
 * Created by TimFulton on 2/10/16.
 */

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import software.umlgenerator.util.Logg;

public class PackageLoader implements IXposedHookLoadPackage {

    private static final String STANDALONE_PACKAGE = "software.standalone";

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals(STANDALONE_PACKAGE)) {
            Class clazz = Class.forName("software.standalone.data.adapter.AppInfoAdapter",
                    false, lpparam.classLoader);
            Method method = clazz.getMethod("initXposed", ApplicationInfo.class);

            XposedBridge.hookMethod(method, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    ApplicationInfo arg = (ApplicationInfo) param.args[0];
                    String packageName = arg.packageName;
                    FileManager.writeToFile(packageName);
                }
            });
        } else {
            String name = lpparam.packageName;
            File file = FileManager.getFile(name);
            String fileContents = FileManager.readFile(file);

            if (fileContents.equals(name)) {
                new PackageHooker(lpparam, file);
            }
        }
    }
}
