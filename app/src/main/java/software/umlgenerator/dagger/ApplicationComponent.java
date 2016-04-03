package software.umlgenerator.dagger;

import javax.inject.Singleton;

import dagger.Component;
import software.umlgenerator.ui.BaseActivity;
import software.umlgenerator.ui.HomeActivity;

/**
 * Created by mbpeele on 3/22/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(BaseActivity homeActivity);
}
