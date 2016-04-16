package software.umlgenerator.ui;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import software.umlgenerator.UMLApplication;
import software.umlgenerator.data.DataStore;
import software.umlgenerator.data.api.UMLService;
import software.umlgenerator.util.Logg;

/**
 * Created by mbpeele on 3/23/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Inject DataStore dataStore;
    @Inject UMLService umlService;

    private CompositeSubscription compositeSubscription;
    private ViewGroup root;

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
        root = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
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

    public void handleError(HttpException httpException) {
        Logg.log(getClass().getName(), httpException);
        httpException.printStackTrace();

        Snackbar.make(root,
                "SOME SHIT WENT WRONG",
                Snackbar.LENGTH_LONG)
                .show();
    }
}
