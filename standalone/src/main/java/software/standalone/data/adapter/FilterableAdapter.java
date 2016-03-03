package software.standalone.data.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import clojure.lang.ArraySeq;
import software.standalone.util.Logg;

/**
 * Created by mbpeele on 2/28/16.
 */
public abstract class FilterableAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    private List<T> data;

    public FilterableAdapter(List<T> models) {
        data = new ArrayList<>(models);
    }

    public List<T> getData() {
        return data;
    }

    public abstract void filter(String query, List<T> backingData);

    public void animateTo(List<T> newData) {
        applyAndAnimateRemovals(newData);
        applyAndAnimateAdditions(newData);
        applyAndAnimateMovedItems(newData);
    }

    private void applyAndAnimateRemovals(List<T> newData) {
        for (int i = data.size() - 1; i >= 0; i--) {
            final T model = data.get(i);
            if (!newData.contains(model)) {
                data.remove(model);
                notifyItemRemoved(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<T> newData) {
        for (int i = 0; i < newData.size(); i++) {
            final T model = newData.get(i);
            if (!data.contains(model)) {
                data.add(i, model);
                notifyItemInserted(i);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<T> newData) {
        for (int toPosition = newData.size() - 1; toPosition >= 0; toPosition--) {
            final T model = newData.get(toPosition);
            final int fromPosition = data.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                data.remove(fromPosition);
                data.add(toPosition, model);
                notifyItemMoved(fromPosition, toPosition);
            }
        }
    }
}
