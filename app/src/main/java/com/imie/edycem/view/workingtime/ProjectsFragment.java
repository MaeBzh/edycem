package com.imie.edycem.view.workingtime;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.imie.edycem.R;

public class ProjectsFragment extends Fragment {


    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_and_job, container, false);

        this.initComponents(view);
        return view;
    }

    public void initComponents(View view) {


    }
}
