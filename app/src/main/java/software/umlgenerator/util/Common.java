package software.umlgenerator.util;

import android.os.Environment;

/**
 * Created by mbpeele on 3/22/16.
 */
public class Common {

    public final static String PACKAGE_NAME = "software.umlgenerator";
    public final static String FILE_DIR =
            Environment.getExternalStorageDirectory() + "/software.umlgenerator/";
    public final static String XPOSED_PREFERENCES = "UMLGenerator";
    public final static String SERVICE_CLASS = "software.umlgenerator.xposed.loaders.XposedService";
}
