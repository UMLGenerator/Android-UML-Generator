package software.umlgenerator.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import software.umlgenerator.UMLApplication;
import software.umlgenerator.data.DataStore;

/**
 * Created by mbpeele on 3/23/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    DataStore dataStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((UMLApplication) getApplicationContext()).getComponent().inject(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }
}
