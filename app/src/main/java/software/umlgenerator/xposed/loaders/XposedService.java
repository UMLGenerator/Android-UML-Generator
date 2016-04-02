package software.umlgenerator.xposed.loaders;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import software.umlgenerator.R;
import software.umlgenerator.ui.FileActivity;
import software.umlgenerator.util.Logg;
import software.umlgenerator.xposed.io.FileManager;
import software.umlgenerator.xposed.model.parcelables.ParcelableClass;
import software.umlgenerator.xposed.model.parcelables.ParcelableMethod;
import software.umlgenerator.xposed.model.parcelables.ParcelablePackage;

/**
 * Created by mbpeele on 2/24/16.
 */
public class XposedService extends Service {

    private final static int FOREGROUND_ID = 1;
    public final static String PACKAGE_NAME = "packageName";

    private Messenger messenger;
    private FileManager fileManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logg.log("ON BIND");
        String name = intent.getStringExtra(PACKAGE_NAME);
        fileManager = new FileManager(name);

        showInForeground(name);

        if (messenger == null) {
            synchronized (XposedService.class) {
                if (messenger == null) {
                    messenger = new Messenger(new XposedMessageHandler());
                }
            }
        }

        return messenger.getBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        Logg.log("ON REBIND");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logg.log("ON UNBIND");
        return true;
    }

    @Override
    public void onDestroy() {
        dismissInForeground();
    }

    private void showInForeground(String packageName) {
        Intent intent = new Intent(this, FileActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_create_new_folder_black_24dp)
                        .setContentTitle("Writing XML for " + packageName)
                        .setContentText("Click to generate UML diagram.")
                        .setAutoCancel(true)
                        .setOngoing(true)
                        .setContentIntent(pendingIntent);

        startForeground(FOREGROUND_ID, mBuilder.build());
    }

    private void dismissInForeground() {
        stopForeground(true);
    }

    private class XposedMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            switch (msg.what) {
                case IXposedServiceConnection.CLASS_CALLED:
                    bundle.setClassLoader(ParcelableClass.class.getClassLoader());
                    ParcelableClass parcelable = bundle.getParcelable(IXposedServiceConnection.BUNDLE_KEY);
                    fileManager.onClassCalled(parcelable);
                    break;

                case IXposedServiceConnection.METHOD_CALLED:
                    bundle.setClassLoader(ParcelableMethod.class.getClassLoader());
                    ParcelableMethod parcelableMethod = bundle.getParcelable(IXposedServiceConnection.BUNDLE_KEY);
                    fileManager.onMethodCalled(parcelableMethod);
                    break;

                case IXposedServiceConnection.PACKAGE_CALLED:
                    bundle.setClassLoader(ParcelablePackage.class.getClassLoader());
                    ParcelablePackage parcelablePackage = bundle.getParcelable(IXposedServiceConnection.BUNDLE_KEY);
                    fileManager.onPackageCalled(parcelablePackage);
                    break;
            }
        }
    }
}
