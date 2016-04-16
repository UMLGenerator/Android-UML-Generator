package software.umlgenerator.data;

import android.support.annotation.Nullable;

import java.lang.ref.SoftReference;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import software.umlgenerator.ui.BaseActivity;

/**
 * Created by mbpeele on 4/3/16.
 */
public abstract class ActivitySubscriber<T> extends Subscriber<T> {

    private SoftReference<BaseActivity> softReference;

    public ActivitySubscriber(BaseActivity activity) {
        if (activity == null) {
            onError(new NullPointerException("Activity subscriber is null"));
            return;
        }

        activity.addSubscription(this);

        softReference = new SoftReference<>(activity);
    }

    @Override
    public void onCompleted() {
        removeSelf();
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            BaseActivity baseActivity = getSubscribedActivity();
            if (baseActivity != null) {
                baseActivity.handleError((HttpException) e);
            }
        } else {
            e.printStackTrace();
        }
    }

    @Override
    public void onNext(T object) {
        BaseActivity activity = getSubscribedActivity();
        if (activity != null) {
            onSafeNext(object, activity);
        }
    }

    public abstract void onSafeNext(T data, BaseActivity activity);

    private void removeSelf() {
        BaseActivity activity = softReference.get();
        if (activity != null) {
            activity.removeSubscription(this);
        }
    }

    @Nullable
    public BaseActivity getSubscribedActivity() {
        return softReference.get();
    }

}
