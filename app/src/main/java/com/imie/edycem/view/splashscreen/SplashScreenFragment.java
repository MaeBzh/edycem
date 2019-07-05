package com.imie.edycem.view.splashscreen;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imie.edycem.R;

public class SplashScreenFragment extends Fragment {
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);

        this.initComponents(view);
        return view;
    }

    public void initComponents(View view) {

    }
}
