package software.standalone.ui;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import software.standalone.R;
import software.standalone.data.adapter.AppInfoAdapter;
import software.standalone.util.Logg;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.activity_home_toolbar) Toolbar toolbar;
    @Bind(R.id.activity_home_recycler) RecyclerView recyclerView;
    @Bind(R.id.activity_home_fab) FloatingActionButton fab;

    private AppInfoAdapter appInfoAdapter;
    private List<ApplicationInfo> applicationInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        createAppInfoList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        final MenuItem item = menu.findItem(R.id.menu_home_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint(getString(R.string.menu_home_search_hint));
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                searchView.onActionViewCollapsed();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final String query = newText.toLowerCase();

                appInfoAdapter.filter(query, applicationInfoList);
                recyclerView.scrollToPosition(0);
                return true;
            }
        });

        return true;
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

        appInfoAdapter = new AppInfoAdapter(this, applicationInfoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(appInfoAdapter);
    }
}
