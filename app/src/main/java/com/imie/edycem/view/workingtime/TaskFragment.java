package com.imie.edycem.view.workingtime;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.imie.edycem.R;

public class TaskFragment extends Fragment {

    private static final int KEYPOSITION = 0;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        this.initComponents(view);
        return view;
    }

    public void initComponents(View view) {
    }
}
