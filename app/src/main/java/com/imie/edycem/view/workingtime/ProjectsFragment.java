package com.imie.edycem.view.workingtime;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.imie.edycem.R;
import com.imie.edycem.data.ProjectWebServiceClientAdapter;
import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.provider.utils.ProjectProviderUtils;

import java.util.ArrayList;

public class ProjectsFragment extends Fragment {

    private ConstraintLayout hiddenProject;
    private Button addButton;
    private Button lessButton;
    private RadioGroup radioGroup;
    private ProjectProviderUtils projectProviderUtils;
    private Project project;
    private WorkingTimeActivity activity;
    private WorkingTime workingTime;
    private ArrayList<Project> projectsList = new ArrayList<>();
    private User connectedUser;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projects, container, false);//

        this.initComponents(view);
        return view;
    }

    public void initComponents(final View view) {
        this.activity = (WorkingTimeActivity) this.getActivity();
        this.workingTime = this.activity.getWorkingTime();
        this.connectedUser = this.activity.getConnectedUser();
//        this.projectsList = this.activity.getProjects();

        this.projectProviderUtils = new ProjectProviderUtils(this.getContext());
        this.project = new Project();
        this.hiddenProject = (ConstraintLayout) view.findViewById(R.id.layout_hidden_projects);
        this.addButton = (Button) view.findViewById(R.id.add_button);
        this.lessButton = (Button) view.findViewById(R.id.less_button);
        this.radioGroup = (RadioGroup) view.findViewById(R.id.radio_group_project);
        this.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selectedButton = (RadioButton) view.findViewById(i);
                selectedButton.setBackground(ContextCompat.getDrawable(ProjectsFragment.this.getContext(), R.drawable.button_selector_blue));
                ProjectsFragment.this.project.setName(selectedButton.getText().toString());
//                Project project = ProjectsFragment.this.projectProviderUtils.query();
                ProjectsFragment.this.workingTime.setProject(project);
            }
        });

        this.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hiddenProject.getVisibility() == View.GONE) {
                    hiddenProject.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.GONE);
                    lessButton.setVisibility(View.VISIBLE);
                }
            }
        });

        this.lessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hiddenProject.getVisibility() == View.VISIBLE) {
                    hiddenProject.setVisibility(View.GONE);
                    addButton.setVisibility(View.VISIBLE);
                    lessButton.setVisibility(View.GONE);
                }
            }
        });

    }
}
