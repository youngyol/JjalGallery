package com.example.nasos.jjalgallery.ui.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nasos on 2017-08-07.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected ArrayList<T> items;
    protected Context context;
    protected View view;

    public BaseAdapter(Context context, ArrayList<T> items) {
        this.context = context;
        this.items = items;
    }

    public void add(T item) {
        int position = getItemCount();
        items.add(item);
        notifyItemInserted(position);
    }

    public void add(T item, int index) {
        items.add(index, item);
        notifyItemInserted(index);
    }

    public void add(List<T> items) {
        int position = getItemCount();
        for (T item : items) {
            items.add(item);
        }
        notifyItemRangeInserted(position, items.size());
    }

    public void remove(int position) {
        if (position < getItemCount()) {
            items.remove(position);
            notifyDataSetChanged();
        }
    }

    public void remove(T item) {
        int position = items.indexOf(item);
        remove(position);
    }

    public void clear() {
        int count = items.size();
        if (count > 0) {
            items.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}