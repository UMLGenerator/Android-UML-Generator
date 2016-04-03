package software.umlgenerator.data;

import software.umlgenerator.data.model.parcelables.ParcelableClass;
import software.umlgenerator.data.model.parcelables.ParcelableMethod;
import software.umlgenerator.data.model.parcelables.ParcelablePackage;
import software.umlgenerator.data.model.xml.PackageXMLElement;

/**
 * Created by mbpeele on 4/1/16.
 */
interface IFileManager {

    void onClassCalled(ParcelableClass parcelableClass);

    void onMethodCalled(ParcelableMethod parcelableMethod);

    void onPackageCalled(ParcelablePackage parcelablePackage);

    void writeToFile(PackageXMLElement packageXMLElement);
}
