package software.umlgenerator.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by mbpeele on 4/3/16.
 */
public abstract class BindingViewHolder<T> extends RecyclerView.ViewHolder {

    public BindingViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T data);
}
