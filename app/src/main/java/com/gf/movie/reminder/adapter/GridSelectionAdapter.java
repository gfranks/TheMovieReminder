package com.gf.movie.reminder.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;

public abstract class GridSelectionAdapter extends ArrayAdapter {

    protected SparseBooleanArray mSelectedItemsIds;

    public GridSelectionAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        mSelectedItemsIds = new SparseBooleanArray();
    }

    public GridSelectionAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
        mSelectedItemsIds = new SparseBooleanArray();
    }

    public abstract void remove(Object object);

    public boolean isSelected(int key) {
        return mSelectedItemsIds.get(key, false);
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value) {
            mSelectedItemsIds.put(position, value);
        } else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}
