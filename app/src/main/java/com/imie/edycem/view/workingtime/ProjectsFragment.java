package com.imie.edycem.view.workingtime;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.imie.edycem.R;
import com.imie.edycem.data.ProjectWebServiceClientAdapter;
import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.provider.contract.WorkingTimeContract;
import com.imie.edycem.provider.utils.ProjectProviderUtils;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;

public class ProjectsFragment extends Fragment implements View.OnClickListener {

    private ConstraintLayout hiddenProject;
    private Button addButton;
    private Button lessButton;
    private RadioGroup radioGroup;
    private ProjectProviderUtils projectProviderUtils;
    private Project project;
    private WorkingTimeActivity activity;
    private WorkingTime workingTime;
    private ArrayList<Project> projectsList = new ArrayList<>();
    private int[] projectsId;
    private Button nextButton;
    private Button previousButton;
    private User connectedUser;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projects, container, false);//

        this.initComponents(view);
        return view;
    }

    public void initComponents(final View view) {

        this.nextButton = (Button) view.findViewById(R.id.btn_next);
        this.nextButton.setOnClickListener(this);
        this.previousButton = (Button) view.findViewById(R.id.btn_previous);
        this.previousButton.setOnClickListener(this);

        Intent intent = this.getActivity().getIntent();
        this.workingTime = intent.getParcelableExtra(WorkingTimeContract.TABLE_NAME);
        this.connectedUser = intent.getParcelableExtra(UserContract.TABLE_NAME);
//        this.projectsList = this.activity.getProjects();
        this.projectsId = intent.getIntArrayExtra("projects_id");

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
//                selectedButton.setBackground(ContextCompat.getDrawable(ProjectsFragment.this.getContext(), R.drawable.button_selector_blue));
                ProjectsFragment.this.project.setName(selectedButton.getText().toString());
//                Project project = ProjectsFragment.this.projectProviderUtils.query();
                ProjectsFragment.this.workingTime.setProject(project);
            }
        });

        this.addButton.setOnClickListener(this);

        this.lessButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_button:
                if (hiddenProject.getVisibility() == View.GONE) {
                    hiddenProject.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.GONE);
                    lessButton.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.less_button:
                if (hiddenProject.getVisibility() == View.VISIBLE) {
                    hiddenProject.setVisibility(View.GONE);
                    addButton.setVisibility(View.VISIBLE);
                    lessButton.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_next:
                if (this.workingTime.getProject() != null) {
                    Intent intent = new Intent(this.getContext(), TaskActivity.class);
                    intent.putExtra(UserContract.TABLE_NAME, (Parcelable) this.connectedUser);
                    intent.putExtra(WorkingTimeContract.TABLE_NAME, (Parcelable) this.workingTime);
                    startActivity(intent);
                } else {
                    Toast.makeText(this.getContext(), getString(R.string.need_project), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_previous:
                this.getActivity().onBackPressed();
        }
    }
}
