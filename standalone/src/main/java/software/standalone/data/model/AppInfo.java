package software.standalone.data.model;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import software.standalone.util.Logg;

/**
 * Created by mbpeele on 2/20/16.
 */
public class AppInfo {

    public Drawable appDrawable;
    public String appName;
    public Intent appIntent;

    public AppInfo(Drawable drawable, String name, Intent intent) {
        appDrawable = drawable;
        appName = name;
        appIntent = intent;
    }
}
