package software.standalone.data.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import software.standalone.R;
import software.standalone.data.model.AppInfo;

/**
 * Created by mbpeele on 2/20/16.
 */
public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.AppInfoViewHolder> {

    private List<AppInfo> appInfoList;
    private LayoutInflater layoutInflater;
    private Context context;

    public AppInfoAdapter(Context cxt, List<AppInfo> list) {
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
        AppInfo appInfo = appInfoList.get(position);
        holder.textView.setText(appInfo.appName);
        Drawable drawable = appInfo.appDrawable;
        if (appInfo.appDrawable != null) {
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(appInfo.appIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appInfoList.size();
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
