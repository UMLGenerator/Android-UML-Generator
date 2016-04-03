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

    int CLASS_CALLED = 1;
    int METHOD_CALLED = 2;
    int PACKAGE_CALLED = 3;
    String BUNDLE_KEY = "IXposedServiceConnection";

    void sendPackageMessage(ApplicationInfo applicationInfo);

    void sendClassMessage(Class clazz);

    void sendMethodMessage(Method method);

    Bundle createMessageBundle(Parcelable parcelable);

    void checkIfBound(Message message);

    void sendMessage(Message message);
}
