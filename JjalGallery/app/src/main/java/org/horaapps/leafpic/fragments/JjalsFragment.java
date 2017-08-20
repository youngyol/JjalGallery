package org.horaapps.leafpic.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.horaapps.leafpic.R;

import horaapps.org.liz.ThemeHelper;

/**
 * Created by nasos on 2017-08-20.
 */

public class JjalsFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          }

    @Override
    public boolean editMode() {
        return false;
    }

    @Override
    public void clearSelected() {

    }

    @Override
    public void refreshTheme(ThemeHelper themeHelper) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_jjals, null);

        return v;
    }
}
