package software.umlgenerator.xposed.loaders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;

import javax.inject.Inject;

import software.umlgenerator.UMLApplication;
import software.umlgenerator.util.DataStore;
import software.umlgenerator.util.Logg;
import software.umlgenerator.xposed.io.FileManager;

/**
 * Created by mbpeele on 3/22/16.
 */
public class ApplicationInfoReceiver extends BroadcastReceiver {

    public final static String INTENT_DATA_KEY = "key";

    @Override
    public void onReceive(Context context, Intent intent) {
        ApplicationInfo applicationInfo = intent.getParcelableExtra(INTENT_DATA_KEY);
        String name = applicationInfo.packageName;
        FileManager.writeToFile(name);

        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(name);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launchIntent);
    }
}
