package software.umlgenerator.dagger;

import javax.inject.Singleton;

import dagger.Component;
import software.umlgenerator.data.adapter.AppInfoAdapter;
import software.umlgenerator.ui.FileActivity;
import software.umlgenerator.xposed.loaders.Loader;

/**
 * Created by mbpeele on 3/22/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

}
