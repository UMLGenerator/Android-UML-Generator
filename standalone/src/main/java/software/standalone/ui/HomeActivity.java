package software.standalone.ui;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import software.standalone.R;
import software.standalone.data.adapter.AppInfoAdapter;
import software.standalone.data.model.AppInfo;
import software.standalone.util.Logg;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.activity_home_toolbar) Toolbar toolbar;
    @Bind(R.id.activity_home_recycler) RecyclerView recyclerView;
    @Bind(R.id.activity_home_fab) FloatingActionButton fab;

    private List<AppInfo> appInfoList;
    private AppInfoAdapter appInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        createAppInfoList();

        appInfoAdapter = new AppInfoAdapter(this, appInfoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(appInfoAdapter);
    }

    private void createAppInfoList() {
        appInfoList = new ArrayList<>();

        final PackageManager pm = getPackageManager();
        final List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo: packages) {
            String name = packageInfo.packageName;
            Intent intent = pm.getLaunchIntentForPackage(name);
            if (intent != null && !Objects.equals(name, getPackageName())) {
                try {
                    Drawable icon = getPackageManager().getApplicationIcon(name);
                    appInfoList.add(new AppInfo(icon, name, intent));
                } catch (PackageManager.NameNotFoundException e) {
                    appInfoList.add(new AppInfo(null, name, intent));
                }
            }
        }
    }
}
