package software.umlgenerator.xposed.loaders;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import software.umlgenerator.util.Common;
import software.umlgenerator.util.Logg;

import static de.robv.android.xposed.XposedHelpers.*;

/**
 * Created by mbpeele on 3/23/16.
 */
class PermissionsManager {

    private XSharedPreferences prefs;

    public PermissionsManager() {
        prefs = new XSharedPreferences(Loader.class.getPackage().getName());
        prefs.makeWorldReadable();
    }
}
