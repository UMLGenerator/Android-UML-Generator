package software.umlgenerator.util;

import android.util.Log;

/**
 * Created by mbpeele on 2/17/16.
 */
public class Logg {

    public enum TAG {
        XPOSED,
        DEBUG
    }

    private static final String LOG_TAG = "Miles";
    private static final StringBuilder builder = new StringBuilder();

    private static void mainLog(TAG tag, StringBuilder stringBuilder) {
        String string = stringBuilder.toString();
        if (!string.isEmpty()) {
            Log.d(LOG_TAG, string);
        } else {
            Log.d(LOG_TAG, "printLn needs a Message");
        }
        builder.setLength(0);
    }

    private static void append(Object object) {
        builder.append(object);
        builder.append(", ");
    }

    public static void log(Object... objects) {
        for (Object object: objects) {
            append(object);
        }
        mainLog(null, builder);
    }

    public static void log(String... strings) {
        for (String string: strings) {
            append(string);
        }
        mainLog(null, builder);
    }

    public static void log(TAG tag, Object... objects) {
        for (Object object: objects) {
            append(object);
        }
        mainLog(tag, builder);
    }
}
