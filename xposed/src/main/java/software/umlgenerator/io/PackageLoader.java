package software.umlgenerator.io;

/**
 * Created by TimFulton on 2/10/16.
 */

import android.app.Application;
import android.content.Context;
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
            findAndHookMethod("android.app.Application", lpparam.classLoader, "onCreate",
                    new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Context context = (Context) param.thisObject;

                    PackageManager packageManager = context.getPackageManager();
                    PackageInfo packageInfo = packageManager.getPackageInfo(
                            context.getPackageName(), PackageManager.GET_PERMISSIONS);

                    Logg.log("FOR: ", context.getPackageName());
                    String[] perms = packageInfo.requestedPermissions;
                    if (perms != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (String permission: packageInfo.requestedPermissions) {
                            stringBuilder.append(permission);
                            stringBuilder.append(", ");
                        }
                        Logg.log("PERMISSIONS: ", stringBuilder.toString());
                    } else {
                        Logg.log("REQUESTS NO PERMISSIONS");
                    }
                }
            });

            String name = lpparam.packageName;
            File file = FileManager.getFile(name);
            String fileContents = FileManager.readFile(file);

            if (fileContents.equals(name)) {
                new PackageHooker(lpparam, file);
            }
        }
    }
}
