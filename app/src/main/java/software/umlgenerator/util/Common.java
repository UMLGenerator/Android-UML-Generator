package software.umlgenerator.util;

/**
 * Created by mbpeele on 3/22/16.
 */
public class Common {

    public final static String PACKAGE_NAME = "software.umlgenerator";
    public final static String XPOSED_PREFERENCES = "UMLGenerator";

    public final static String ADAPTER_CLASS = "software.standalone.data.adapter.AppInfoAdapter";
    public final static String ADAPTER_METHOD = "initXposed";

    public final static String SERVICE_CLASS = "software.umlgenerator.xposed.loaders.HookService";
    public final static String SERVICE_METHOD = "hookAll";
}
