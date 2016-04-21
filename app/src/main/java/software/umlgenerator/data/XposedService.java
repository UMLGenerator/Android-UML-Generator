package software.umlgenerator.data;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.widget.RemoteViews;

import software.umlgenerator.R;
import software.umlgenerator.ui.FileActivity;
import software.umlgenerator.util.Common;
import software.umlgenerator.data.model.parcelables.ParcelableClass;
import software.umlgenerator.data.model.parcelables.ParcelableMethod;
import software.umlgenerator.data.model.parcelables.ParcelablePackage;

/**
 * Created by mbpeele on 2/24/16.
 */
public class XposedService extends Service {

    private final static int FOREGROUND_ID = 1;
    private final static int START = 2;
    private final static int STOP = 3;
    private final static int GENERATE = 4;
    private final static String INT_ARG_KEY = "argumentKey";

    public final static String APPLICATION_INFO = "applicationInfo";
    public final static String SHOULD_WRITE = "hookFromStart";

    private Messenger messenger;
    private FileManager fileManager;
    private ApplicationInfo applicationInfo;

    private boolean shouldWrite;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int arg = intent.getIntExtra(INT_ARG_KEY, 0);
        switch (arg) {
            case START:
                shouldWrite = true;
                break;
            case STOP:
                shouldWrite = false;
                break;
            case GENERATE:
                try {
                    dismissInForeground();
                    getPendingIntentForContent().send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
                break;
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        applicationInfo = intent.getParcelableExtra(APPLICATION_INFO);
        shouldWrite = intent.getBooleanExtra(SHOULD_WRITE, true);
        fileManager = new FileManager(applicationInfo.packageName);

        showInForeground();

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
    public void onDestroy() {
        dismissInForeground();
    }

    private RemoteViews getRemoteViews() {
        RemoteViews notificationView =
                new RemoteViews(Common.PACKAGE_NAME, R.layout.service_remoteview_start_stop);

        Drawable drawable = getPackageManager().getApplicationIcon(applicationInfo);
        Bitmap bitmap = convertDrawableToBitmap(drawable);
        notificationView.setImageViewBitmap(R.id.service_remoteview_icon, bitmap);

        String text = "Writing XML for " + applicationInfo.packageName +
                "\n Click checkmark to generate UML diagram";
        int length = text.split("\n")[0].length();

        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new RelativeSizeSpan(1.4f), 0, length, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        notificationView.setTextViewText(R.id.service_remoteview_text, spannableString);

        notificationView.setOnClickPendingIntent(R.id.service_remoteview_start,
                getPendingIntentForRemote(START));

        notificationView.setOnClickPendingIntent(R.id.service_remoteview_stop,
                getPendingIntentForRemote(STOP));

        notificationView.setOnClickPendingIntent(R.id.service_remoteview_generate,
                getPendingIntentForRemote(GENERATE));

        return notificationView;
    }

    private PendingIntent getPendingIntentForRemote(int arg) {
        Intent intent = new Intent(this, XposedService.class);
        intent.putExtra(INT_ARG_KEY, arg);
        return PendingIntent.getService(this, arg, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getPendingIntentForContent() {
        Intent intent = new Intent(this, FileActivity.class);
        intent.setData(fileManager.getFileUri());
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Bitmap convertDrawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
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

    private class XposedMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            switch (msg.what) {
                case IXposedServiceConnection.CLASS_BEFORE_CALLED:
                    if (shouldWrite) {
                        bundle.setClassLoader(ParcelableClass.class.getClassLoader());
                        ParcelableClass parcelable = bundle.getParcelable(IXposedServiceConnection.BUNDLE_KEY);
                        fileManager.onBeforeClassCalled(parcelable);
                    }
                    break;

                case IXposedServiceConnection.CLASS_AFTER_CALLED:
                    if (shouldWrite) {
                        bundle.setClassLoader(ParcelableClass.class.getClassLoader());
                        ParcelableClass parcelable = bundle.getParcelable(IXposedServiceConnection.BUNDLE_KEY);
                        fileManager.onAfterClassCalled(parcelable);
                    }
                    break;

                case IXposedServiceConnection.METHOD_BEFORE_CALLED:
                    if (shouldWrite) {
                        bundle.setClassLoader(ParcelableMethod.class.getClassLoader());
                        ParcelableMethod parcelableMethod = bundle.getParcelable(IXposedServiceConnection.BUNDLE_KEY);
                        fileManager.onBeforeMethodCalled(parcelableMethod);
                    }
                    break;

                case IXposedServiceConnection.METHOD_AFTER_CALLED:
                    if (shouldWrite) {
                        bundle.setClassLoader(ParcelableMethod.class.getClassLoader());
                        ParcelableMethod parcelableMethod = bundle.getParcelable(IXposedServiceConnection.BUNDLE_KEY);
                        fileManager.onAfterMethodCalled(parcelableMethod);
                    }
                    break;

                case IXposedServiceConnection.PACKAGE_CALLED:
                    if (shouldWrite) {
                        bundle.setClassLoader(ParcelablePackage.class.getClassLoader());
                        ParcelablePackage parcelablePackage = bundle.getParcelable(IXposedServiceConnection.BUNDLE_KEY);
                        fileManager.onPackageCalled(parcelablePackage);
                    }
                    break;
            }
        }
    }
}
