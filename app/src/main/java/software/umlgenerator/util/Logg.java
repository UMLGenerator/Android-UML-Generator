package software.umlgenerator.util;

import android.util.Log;

/**
 * Created by mbpeele on 2/17/16.
 */
public class Logg {

    private static final String LOG_TAG = "Miles";
    private static final StringBuilder builder = new StringBuilder();

    private static void mainLog(StringBuilder stringBuilder) {
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
        mainLog(builder);
    }

    public static void log(String... strings) {
        for (String string: strings) {
            append(string);
        }
        mainLog(builder);
    }
}
