package com.example.nasos.jjalgallery.ui.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by nasos on 2017-08-07.
 */

public  abstract  class BaseViewHolder extends RecyclerView.ViewHolder {
    private Context ctx;

    public BaseViewHolder(Context context, View itemView) {
        super(itemView);
        ctx = context;
        ButterKnife.bind(this, itemView);
    }
}
