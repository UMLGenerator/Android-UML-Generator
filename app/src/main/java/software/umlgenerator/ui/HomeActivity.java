package software.umlgenerator.ui;

import android.animation.LayoutTransition;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import software.umlgenerator.R;
import software.umlgenerator.data.ActivitySubscriber;
import software.umlgenerator.data.adapter.AppInfoAdapter;
import software.umlgenerator.util.Logg;

public class HomeActivity extends BaseActivity implements RecyclerViewClickListener {

    @Bind(R.id.activity_home_toolbar) Toolbar toolbar;
    @Bind(R.id.activity_home_recycler) RecyclerView recyclerView;

    private AppInfoAdapter appInfoAdapter;
    private List<ApplicationInfo> applicationInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        setSupportActionBar(toolbar);

        createAppInfoList();

        final ImageView test = (ImageView) findViewById(R.id.test);

        File file = new File("/sdcard/software.umlgenerator/plantUMLTest.txt");
        umlService.uploadFileToServer(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ActivitySubscriber<ResponseBody>(this) {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Logg.log("RESPONSE FROM UPLOAD");
                        try {
                            Glide.with(HomeActivity.this)
                                    .fromBytes()
                                    .load(responseBody.bytes())
                                    .into(test);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        umlService.testConnection()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ActivitySubscriber<ResponseBody>(this) {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Logg.log("HAS CONNECTION");
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
    public void onItemClicked(final ApplicationInfo applicationInfo) {
        final String packageName = applicationInfo.packageName;

        new android.app.AlertDialog.Builder(this)
                .setTitle(R.string.xposed_hook_options_dialog_title)
                .setMessage(R.string.xposed_hook_options_dialog_body)
                .setPositiveButton(R.string.xpsoed_hook_options_dialog_positive,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataStore.setPackageNameToHook(packageName, true);
                        launchApplication(applicationInfo);
                    }
                })
                .setNegativeButton(R.string.xposed_hook_options_dialog_negative,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataStore.setPackageNameToHook(packageName, false);
                        launchApplication(applicationInfo);
                    }
                })
                .create()
                .show();
    }

    private void launchApplication(ApplicationInfo applicationInfo) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(applicationInfo.packageName);
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
