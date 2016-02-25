package software.standalone.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import software.standalone.R;
import software.standalone.data.model.AppInfo;
import software.standalone.util.Logg;

/**
 * Created by mbpeele on 2/20/16.
 */
public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.AppInfoViewHolder> {

    private List<ApplicationInfo> appInfoList;
    private LayoutInflater layoutInflater;
    private Context context;

    public AppInfoAdapter(Context cxt, List<ApplicationInfo> list) {
        context = cxt;
        appInfoList = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public AppInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppInfoViewHolder(layoutInflater.inflate(R.layout.app_info_adapter_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(AppInfoViewHolder holder, int position) {
        final ApplicationInfo appInfo = appInfoList.get(position);
        holder.textView.setText(appInfo.packageName);
        Drawable drawable = context.getPackageManager().getApplicationIcon(appInfo);
        if (drawable != null) {
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initXposed(appInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appInfoList.size();
    }

    public void initXposed(ApplicationInfo appInfo) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(appInfo.packageName);
        context.startActivity(intent);
    }

    final class AppInfoViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.app_info_text)
        TextView textView;

        public AppInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
