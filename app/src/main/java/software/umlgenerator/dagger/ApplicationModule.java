package software.umlgenerator.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import software.umlgenerator.UMLApplication;
import software.umlgenerator.data.DataStore;
import software.umlgenerator.data.api.UMLService;

/**
 * Created by mbpeele on 3/22/16.
 */
@Module
@Singleton
public class ApplicationModule {

    private UMLApplication mApplication;

    public ApplicationModule(UMLApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public DataStore getDataStore() {
        return new DataStore(mApplication);
    }

    @Provides
    @Singleton
    public UMLService getUMLService() {
        return new UMLService(mApplication);
    }
}
