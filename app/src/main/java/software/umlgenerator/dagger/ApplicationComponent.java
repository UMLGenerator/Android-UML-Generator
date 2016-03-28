package software.umlgenerator.dagger;

import javax.inject.Singleton;

import dagger.Component;
import software.umlgenerator.xposed.loaders.ApplicationInfoReceiver;
import software.umlgenerator.xposed.loaders.Loader;

/**
 * Created by mbpeele on 3/22/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(ApplicationInfoReceiver applicationInfoReceiver);
}
