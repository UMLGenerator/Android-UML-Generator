package software.umlgenerator.xposed.loaders;

/**
 * Created by TimFulton on 2/10/16.
 */

import android.Manifest;

import java.io.File;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import software.umlgenerator.util.Logg;
import software.umlgenerator.xposed.io.FileManager;

public class Loader implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        String name = lpparam.packageName;
        File file = FileManager.getFile();
        String fileContents = FileManager.readFile(name, file);

        if (fileContents.equals(name)) {
            new PackageHooker(lpparam);
        }
    }
}
