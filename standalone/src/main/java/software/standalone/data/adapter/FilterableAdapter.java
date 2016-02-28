package software.standalone.data.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mbpeele on 2/28/16.
 */
public abstract class FilterableAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    private List<T> data;

    public FilterableAdapter(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public void animateTo(List<T> models) {
//        applyAndAnimateRemovals(models);
//        applyAndAnimateAdditions(models);
//        applyAndAnimateMovedItems(models);
        data = models;
        notifyDataSetChanged();
    }

    private void applyAndAnimateRemovals(List<T> newModels) {
        for (int i = data.size() - 1; i >= 0; i--) {
            final T model = data.get(i);
            if (!newModels.contains(model)) {
                data.remove(model);
                notifyItemRemoved(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<T> newModels) {
        for (int i = 0; i < newModels.size(); i++) {
            final T model = newModels.get(i);
            if (!data.contains(model)) {
                data.add(i, model);
                notifyItemInserted(i);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<T> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final T model = newModels.get(toPosition);
            final int fromPosition = data.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                data.remove(fromPosition);
                data.add(toPosition, model);
                notifyItemMoved(fromPosition, toPosition);
            }
        }
    }
}
