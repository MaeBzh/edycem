package com.imie.edycem.view.workingtime;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.imie.edycem.R;

public class ProjectsFragment extends Fragment implements View.OnClickListener {


    private Button addButton;
    private boolean seeMore = false;
    private RadioButton project6;
    private RadioButton project7;
    private RadioButton project8;
    private RadioButton project9;
    private RadioButton project10;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_and_job, container, false);

        this.initComponents(view);
        return view;
    }

    public void initComponents(View view) {
        this.project6 = (RadioButton) view.findViewById(R.id.btn_project6);
        this.project7 = (RadioButton) view.findViewById(R.id.btn_project7);
        this.project8 = (RadioButton) view.findViewById(R.id.btn_project8);
        this.project9 = (RadioButton) view.findViewById(R.id.btn_project9);
        this.project10 = (RadioButton) view.findViewById(R.id.btn_project10);
        this.addButton = (Button) view.findViewById(R.id.add_button);
        this.addButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_button) {
            System.out.println("toto");
            if (!seeMore) {
                seeMore = true;
                project6.setVisibility(View.VISIBLE);
                project7.setVisibility(View.VISIBLE);
                project8.setVisibility(View.VISIBLE);
                project9.setVisibility(View.VISIBLE);
                project10.setVisibility(View.VISIBLE);
            } else {
                System.out.println("titi");
                seeMore = false;
                project6.setVisibility(View.GONE);
                project7.setVisibility(View.GONE);
                project8.setVisibility(View.GONE);
                project9.setVisibility(View.GONE);
                project10.setVisibility(View.GONE);
            }

        }
    }
}
