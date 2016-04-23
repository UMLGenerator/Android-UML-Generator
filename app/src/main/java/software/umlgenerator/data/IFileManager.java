package software.umlgenerator.data;

import software.umlgenerator.data.model.parcelables.ParcelableClass;
import software.umlgenerator.data.model.parcelables.ParcelableMethod;

/**
 * Created by mbpeele on 4/1/16.
 */
interface IFileManager {

    void writeStart();

    void onBeforeClassCalled(ParcelableClass parcelableClass);

    void onAfterClassCalled(ParcelableClass parcelableClass);

    void onBeforeMethodCalled(ParcelableMethod parcelableMethod);

    void onAfterMethodCalled(ParcelableMethod parcelableMethod);

    void writeEnd();
}
