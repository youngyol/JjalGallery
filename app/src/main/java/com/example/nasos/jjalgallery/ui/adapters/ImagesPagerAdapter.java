package com.example.nasos.jjalgallery.ui.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.example.nasos.jjalgallery.model.Images;
import com.example.nasos.jjalgallery.ui.fragments.ImageFragment;

import java.util.ArrayList;

/**
 * Created by nasos on 2017-08-09.
 */

public class ImagesPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Images> images;

    public ImagesPagerAdapter(FragmentManager fm, ArrayList<Images> images) {
        super(fm);
        this.images = images;
        Log.d("setImgCount", images.size() + "");
    }

    @Override
    public ImageFragment getItem(int position) {
        return ImageFragment.newInstance(images.get(position).getImgPath());
    }

    @Override
    public int getCount() {
        return images != null ? images.size() : 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
