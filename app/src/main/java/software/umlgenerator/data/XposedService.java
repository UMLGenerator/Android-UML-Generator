package software.umlgenerator.data;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.RemoteViews;

import java.lang.ref.WeakReference;

import software.umlgenerator.R;
import software.umlgenerator.data.model.parcelables.ParcelableClass;
import software.umlgenerator.data.model.parcelables.ParcelableMethod;
import software.umlgenerator.ui.FileActivity;
import software.umlgenerator.util.Common;
import software.umlgenerator.util.DrawableUtils;

/**
 * Created by mbpeele on 2/24/16.
 */
public class XposedService extends Service {

    private final static int FOREGROUND_ID = 1;
    private final static int START = 2;
    private final static int GENERATE = 3;
    private final static String INT_ARG_KEY = "argumentKey";
    public final static String APPLICATION_INFO = "applicationInfo";
    public final static String SHOULD_WRITE = "hookFromStart";

    private Messenger messenger;
    private FileManager fileManager;
    private ApplicationInfo applicationInfo;
    private RemoteViews remoteViews;

    private boolean shouldWrite;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int arg = intent.getIntExtra(INT_ARG_KEY, 0);
        switch (arg) {
            case START:
                remoteViews.setViewVisibility(R.id.service_remoteview_start, View.GONE);
                shouldWrite = true;
                break;
            case GENERATE:
                try {
                    shouldWrite = false;
                    fileManager.writeEnd();
                    dismissInForeground();
                    getPendingIntentForContent().send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
                break;
            default:
                throw new IllegalArgumentException("Illegal code used to start XposedService: " + arg);
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (intent.hasExtra(APPLICATION_INFO)) {
            applicationInfo = intent.getParcelableExtra(APPLICATION_INFO);
            shouldWrite = intent.getBooleanExtra(SHOULD_WRITE, true);
            fileManager = new FileManager(applicationInfo.packageName);

            showInForeground();

            if (messenger == null) {
                synchronized (XposedService.class) {
                    if (messenger == null) {
                        messenger = new Messenger(new XposedMessageHandler(this));
                    }
                }
            }

            return messenger.getBinder();
        }

        throw new IllegalArgumentException("XposedService must be started with an " +
                "APPLICATION_INFO object");
    }

    @Override
    public void onDestroy() {
        dismissInForeground();
    }

    private RemoteViews getRemoteViews() {
        remoteViews = new RemoteViews(Common.PACKAGE_NAME, R.layout.service_remoteview_start_stop);

        Drawable drawable = getPackageManager().getApplicationIcon(applicationInfo);
        Bitmap bitmap = DrawableUtils.toBitmap(drawable);
        remoteViews.setImageViewBitmap(R.id.service_remoteview_icon, bitmap);

        String text = "Writing XML for " + applicationInfo.packageName +
                "\n Click checkmark to generate UML diagram";
        int length = text.split("\n")[0].length();

        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new RelativeSizeSpan(1.4f), 0, length, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        remoteViews.setTextViewText(R.id.service_remoteview_text, spannableString);

        remoteViews.setOnClickPendingIntent(R.id.service_remoteview_start,
                getPendingIntentForRemote(START));

        remoteViews.setOnClickPendingIntent(R.id.service_remoteview_generate,
                getPendingIntentForRemote(GENERATE));

        return remoteViews;
    }

    private PendingIntent getPendingIntentForRemote(int arg) {
        Intent intent = new Intent(this, XposedService.class);
        intent.putExtra(INT_ARG_KEY, arg);
        return PendingIntent.getService(this, arg, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getPendingIntentForContent() {
        Intent intent = new Intent(this, FileActivity.class);
        intent.putParcelableArrayListExtra(FileManager.FILE_URIS, fileManager.getFileUris());
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void showInForeground() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_mode_edit_24dp)
                .setContentTitle("Writing XML for " + applicationInfo.packageName)
                .setContentText("Click to generate UML diagram.")
                .setAutoCancel(true)
                .setOngoing(true);

        if (shouldWrite) {
            mBuilder.setContentIntent(getPendingIntentForRemote(GENERATE));
        } else {
            mBuilder.setContent(getRemoteViews());
        }

        startForeground(FOREGROUND_ID, mBuilder.build());
    }

    private void dismissInForeground() {
        stopForeground(true);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(FOREGROUND_ID);
    }

    static class XposedMessageHandler extends Handler {

        private WeakReference<XposedService> weakReference;

        public XposedMessageHandler(XposedService xposedService) {
            weakReference = new WeakReference<>(xposedService);
        }

        @Override
        public void handleMessage(Message msg) {
            XposedService xposedService = weakReference.get();
            if (xposedService != null) {
                boolean shouldWrite = xposedService.shouldWrite;
                if (shouldWrite) {
                    FileManager fileManager = xposedService.fileManager;
                    Bundle bundle = msg.getData();
                    switch (msg.what) {
                        case IXposedServiceConnection.CLASS_BEFORE_CALLED:
                            fileManager.onBeforeClassCalled((ParcelableClass) getParcelable(bundle, ParcelableClass.class));
                            break;

                        case IXposedServiceConnection.CLASS_AFTER_CALLED:
                            fileManager.onAfterClassCalled((ParcelableClass) getParcelable(bundle, ParcelableClass.class));
                            break;

                        case IXposedServiceConnection.METHOD_BEFORE_CALLED:
                            fileManager.onBeforeMethodCalled((ParcelableMethod) getParcelable(bundle, ParcelableMethod.class));
                            break;

                        case IXposedServiceConnection.METHOD_AFTER_CALLED:
                            fileManager.onAfterMethodCalled((ParcelableMethod) getParcelable(bundle, ParcelableMethod.class));
                            break;
                    }
                }
            }
        }

        private Parcelable getParcelable(Bundle bundle, Class clazz) {
            bundle.setClassLoader(clazz.getClassLoader());
            return bundle.getParcelable(IXposedServiceConnection.BUNDLE_KEY);
        }
    }
}
