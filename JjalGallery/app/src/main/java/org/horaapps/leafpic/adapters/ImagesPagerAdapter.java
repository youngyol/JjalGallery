package org.horaapps.leafpic.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import org.horaapps.leafpic.data.bookmark.Bookmark;
import org.horaapps.leafpic.fragments.GifFragment;

import java.util.ArrayList;

/**
 * Created by nasos on 2017-08-20.
 */

public class ImagesPagerAdapter extends FragmentStatePagerAdapter {


    private final String TAG = "asd";
    private ArrayList<Bookmark> media;
    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public ImagesPagerAdapter(FragmentManager fm, ArrayList<Bookmark> media) {
        super(fm);
        this.media = media;
    }

    @Override public Fragment getItem(int pos) {
//        Media media = this.media.get(pos);
//        if (media.isVideo()) return VideoFragment.newInstance(media);
//        if (media.isGif()) return GifFragment.newInstance(media);
//        else return ImageFragment.newInstance(media);
        return GifFragment.newInstance(media.get(pos));
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    public void swapDataSet(ArrayList<Bookmark> media) {
        this.media = media;
        notifyDataSetChanged();
    }

    @Override public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override public int getCount() {
        return media.size();
    }
}
