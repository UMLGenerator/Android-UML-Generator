package software.umlgenerator;

import android.util.Log;

import java.util.Arrays;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by mbpeele on 2/17/16.
 */
public class Logg {

    //

    private static final String LOG_TAG = "Miles";
    private static final StringBuilder builder = new StringBuilder();

    private static void mainLog(String string) {
        if (string != null) {
            if (!string.isEmpty()) {
                XposedBridge.log(string);
            } else {
                XposedBridge.log("printLn needs a Message");
            }
        } else {
            XposedBridge.log("Argument to Logg is null");
        }
        builder.setLength(0);
    }

    public static void log(Double... doubles) {
        for (double doubleVal: doubles) {
            builder.append(doubleVal);
            builder.append(", ");
        }
        mainLog(builder.toString());
    }

    public static void log(double[] doubles) {
        for (double doubleVal: doubles) {
            builder.append(doubleVal);
            builder.append(", ");
        }
        mainLog(builder.toString());
    }

    public static void log(float[] floats) {
        for (float val: floats) {
            builder.append(val);
            builder.append(", ");
        }
        mainLog(builder.toString());
    }

    public static void log(Double value) {
        mainLog(value.toString());
    }

    public static void log(int[] array) {
        for (int integer: array) {
            builder.append(String.valueOf(integer));
            builder.append(", ");
        }
        mainLog(builder.toString());
    }

    public static void log(Long... longs) {
        for (long longVal: longs) {
            builder.append(String.valueOf(longVal));
            builder.append(", ");
        }
        mainLog(builder.toString());
    }

    public static void log(Integer... integers) {
        for (int integer: integers) {
            builder.append(String.valueOf(integer));
            builder.append(", ");
        }
        mainLog(builder.toString());
    }

    public static void log(Float... integers) {
        for (float integer: integers) {
            builder.append(String.valueOf(integer));
            builder.append(", ");
        }
        mainLog(builder.toString());
    }

    public static void log(boolean... bools) {
        for (boolean bool: bools) {
            builder.append(String.valueOf(bool));
            builder.append(", ");
        }
        mainLog(builder.toString());
    }

    public static void log(boolean bool) {
        mainLog(String.valueOf(bool));
    }

    public static void log(float integer) {
        mainLog(String.valueOf(integer));
    }

    public static void log(int integer) {
        mainLog(String.valueOf(integer));
    }

    public static void log(String string) {
        mainLog(string);
    }

    public static void log(CharSequence... charSequences) {
        for (CharSequence charSequence: charSequences) {
            builder.append(charSequence.toString());
            builder.append(", ");
        }
        mainLog(builder.toString());
    }

    public static void log(String string, Throwable throwable) {
        mainLog(string + ", " + throwable.getLocalizedMessage());
    }

    public static void log(String... strings) {
        for (String toPrint: strings) {
            builder.append(toPrint);
            builder.append(", ");
        }
        mainLog(builder.toString());
    }

    public static void log(Throwable throwable) {
        mainLog(throwable.toString());
    }

    public static void log(String string, float... floats) {
        builder.append(string);
        builder.append(", ");
        log(floats);
    }

    public static void log(String string, double... doubles) {
        builder.append(string);
        builder.append(", ");
        log(doubles);
    }

    public static void log(String string, boolean... bools) {
        builder.append(string);
        builder.append(", ");
        log(bools);
    }
}
