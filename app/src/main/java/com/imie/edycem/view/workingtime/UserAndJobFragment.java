package com.imie.edycem.view.workingtime;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.imie.edycem.R;

public class WorkingTimeFragment extends Fragment {

    private EditText editEmail;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        this.initComponents(view);
        return view;
    }

    public void initComponents(View view) {
        this.editEmail = (EditText) view.findViewById(R.id.edit_email);
    }
}
