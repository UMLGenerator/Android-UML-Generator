package software.umlgenerator.xposed.io;

import software.umlgenerator.xposed.model.parcelables.ParcelableClass;
import software.umlgenerator.xposed.model.parcelables.ParcelableMethod;
import software.umlgenerator.xposed.model.parcelables.ParcelablePackage;
import software.umlgenerator.xposed.model.xml.PackageXMLElement;

/**
 * Created by mbpeele on 4/1/16.
 */
interface IFileManager {

    void onClassCalled(ParcelableClass parcelableClass);

    void onMethodCalled(ParcelableMethod parcelableMethod);

    void onPackageCalled(ParcelablePackage parcelablePackage);

    void writeToFile(PackageXMLElement packageXMLElement);
}
