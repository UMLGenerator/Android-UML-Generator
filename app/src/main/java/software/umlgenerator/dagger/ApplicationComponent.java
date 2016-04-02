package software.umlgenerator.dagger;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by mbpeele on 3/22/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

}
