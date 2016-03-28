package software.umlgenerator.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import software.umlgenerator.R;
import software.umlgenerator.xposed.loaders.ApplicationInfoReceiver;
import software.umlgenerator.xposed.loaders.Loader;

/**
 * Created by mbpeele on 2/20/16.
 */
public class AppInfoAdapter extends FilterableAdapter<ApplicationInfo, AppInfoAdapter.AppInfoViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;

    public AppInfoAdapter(Context cxt, List<ApplicationInfo> list) {
        super(list);
        context = cxt;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public AppInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppInfoViewHolder(layoutInflater.inflate(R.layout.app_info_adapter_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(AppInfoViewHolder holder, int position) {
        holder.bind(getData().get(position));
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    public void initXposed(ApplicationInfo appInfo) {
        Intent broadcast = new Intent(context, ApplicationInfoReceiver.class);
        broadcast.putExtra(ApplicationInfoReceiver.INTENT_DATA_KEY, appInfo);
        context.sendBroadcast(broadcast);
    }

    @Override
    public void filter(String query, List<ApplicationInfo> backingData) {
        final List<ApplicationInfo> filteredModelList = new ArrayList<>();
        for (ApplicationInfo model: backingData) {
            final String text = model.packageName.toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }

        animateTo(filteredModelList);
    }

    final class AppInfoViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.app_info_text)
        TextView textView;

        public AppInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final ApplicationInfo appInfo) {
            textView.setText(appInfo.packageName);
            Drawable drawable = context.getPackageManager().getApplicationIcon(appInfo);
            if (drawable != null) {
                textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initXposed(appInfo);
                }
            });
        }
    }
}
