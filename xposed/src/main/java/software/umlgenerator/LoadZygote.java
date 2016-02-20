package software.umlgenerator;

import de.robv.android.xposed.IXposedHookZygoteInit;

/**
 * Created by mbpeele on 2/20/16.
 */
public class LoadZygote implements IXposedHookZygoteInit {

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
    }
}
