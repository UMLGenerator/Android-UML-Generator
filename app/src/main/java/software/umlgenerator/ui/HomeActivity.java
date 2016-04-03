package software.umlgenerator.ui;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import software.umlgenerator.R;
import software.umlgenerator.data.adapter.AppInfoAdapter;

public class HomeActivity extends BaseActivity implements RecyclerViewClickListener {

    @Bind(R.id.activity_home_toolbar) Toolbar toolbar;
    @Bind(R.id.activity_home_recycler) RecyclerView recyclerView;
    @Bind(R.id.activity_home_fab)
    FloatingActionButton fab;

    private AppInfoAdapter appInfoAdapter;
    private List<ApplicationInfo> applicationInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        setSupportActionBar(toolbar);

        createAppInfoList();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        final MenuItem item = menu.findItem(R.id.menu_home_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint(getString(R.string.menu_home_search_hint));
        LinearLayout child = (LinearLayout) searchView.getChildAt(0);
        child.setLayoutTransition(new LayoutTransition());
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchView.onActionViewCollapsed();
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                appInfoAdapter.filter(newText.toLowerCase(), applicationInfoList);
                recyclerView.scrollToPosition(0);
                return true;
            }
        });

        return true;
    }

    @Override
    public void onItemClicked(ApplicationInfo applicationInfo) {
        String packageName = applicationInfo.packageName;

        dataStore.setPackageNameToHook(packageName);

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(launchIntent);
    }

    private void createAppInfoList() {
        applicationInfoList = new ArrayList<>();

        final PackageManager pm = getPackageManager();
        final List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo: packages) {
            String name = packageInfo.packageName;
            Intent intent = pm.getLaunchIntentForPackage(name);

            if (intent != null && !Objects.equals(name, getPackageName())) {
                applicationInfoList.add(packageInfo);
            }
        }

        appInfoAdapter = new AppInfoAdapter(this, this, applicationInfoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(appInfoAdapter);
    }
}
