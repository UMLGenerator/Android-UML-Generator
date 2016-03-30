package software.umlgenerator.xposed.loaders;

/**
 * Created by TimFulton on 2/10/16.
 */

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import org.xmlpull.v1.XmlPullParser;

import java.util.Map;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import software.umlgenerator.util.Common;
import software.umlgenerator.util.Logg;
import software.umlgenerator.xposed.io.FileManager;

public class Loader implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    private XSharedPreferences preferences;

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        final String packageName = lpparam.packageName;
        Map<String, ?> map = preferences.getAll();
        for (String string: map.keySet()) {
            Logg.log("KEY: ", string);
        }
        if (map.containsKey(packageName)) {
            HookService.hookAll(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        preferences = new XSharedPreferences(Common.PACKAGE_NAME, Common.XPOSED_PREFERENCES);
        preferences.makeWorldReadable();
    }
}
