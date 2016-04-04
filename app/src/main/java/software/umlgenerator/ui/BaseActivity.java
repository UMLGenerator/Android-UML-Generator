package software.umlgenerator.ui;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import software.umlgenerator.UMLApplication;
import software.umlgenerator.data.DataStore;
import software.umlgenerator.data.api.UMLService;

/**
 * Created by mbpeele on 3/23/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Inject DataStore dataStore;
    @Inject UMLService umlService;

    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((UMLApplication) getApplicationContext()).getComponent().inject(this);
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    public void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }

    public void removeSubscription(Subscription subscription) {
        compositeSubscription.remove(subscription);
    }

    public boolean hasConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
