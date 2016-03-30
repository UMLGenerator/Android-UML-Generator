package software.umlgenerator;

import android.app.Application;
import android.content.Context;

import software.umlgenerator.dagger.ApplicationComponent;
import software.umlgenerator.dagger.ApplicationModule;
import software.umlgenerator.dagger.DaggerApplicationComponent;
import software.umlgenerator.util.Logg;

/**
 * Created by mbpeele on 3/22/16.
 */
public class UMLApplication extends Application {
    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
