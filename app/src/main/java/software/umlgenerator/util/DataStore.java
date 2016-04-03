package software.umlgenerator.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

/**
 * Created by mbpeele on 3/22/16.
 */
public class DataStore {

    private SharedPreferences prefs;
    public static final String PACKAGE = "packageToHook";

    public DataStore(Context context) {
        prefs = context.getSharedPreferences(Common.XPOSED_PREFERENCES, Context.MODE_WORLD_READABLE);
    }

    private SharedPreferences.Editor getEditor() {
        return prefs.edit();
    }

    public void setPackageNameToHook(String packageName) {
        getEditor().putString(PACKAGE, packageName).apply();
    }
}
