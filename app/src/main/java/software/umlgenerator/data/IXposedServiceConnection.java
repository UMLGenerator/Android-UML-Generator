package software.umlgenerator.data;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;

import java.lang.reflect.Method;

/**
 * Created by mbpeele on 4/1/16.
 */
interface IXposedServiceConnection {

    int CLASS_BEFORE_CALLED = 1;
    int CLASS_AFTER_CALLED = 2;
    int METHOD_BEFORE_CALLED = 3;
    int METHOD_AFTER_CALLED = 4;
    String BUNDLE_KEY = "IXposedServiceConnection";

    void sendBeforeClassMessage(Class clazz);

    void sendAfterClassMessage(Class clazz);

    void sendBeforeMethodMessage(Method method);

    void sendAfterMethodMessage(Method method);

    Bundle createMessageBundle(Parcelable parcelable);

    void checkIfBound(Message message);

    void sendMessage(Message message);
}
