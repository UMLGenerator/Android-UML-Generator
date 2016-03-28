package software.umlgenerator.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mbpeele on 3/22/16.
 */
public class DataStore {

    public final static String SHARED_PREFS_KEY = "prefs";
    public final static String PACKAGE = "package";

    private SharedPreferences prefs;

    public DataStore(Context context) {
        prefs = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        prefs.edit().commit();
    }

    public void setPackage(String name) {
        prefs.edit().putString(PACKAGE, name).commit();
    }

    public String getPackage() {
        return prefs.getString(PACKAGE, "");
    }
}
