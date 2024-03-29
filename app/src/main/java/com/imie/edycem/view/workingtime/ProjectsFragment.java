package com.imie.edycem.view.workingtime;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.imie.edycem.R;
import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.provider.contract.WorkingTimeContract;
import com.imie.edycem.provider.utils.ProjectProviderUtils;


public class ProjectsFragment extends Fragment implements View.OnClickListener {

    private ConstraintLayout hiddenProject;
    private Button addButton;
    private Button lessButton;
    private RadioGroup radioGroup;
    private ProjectProviderUtils projectProviderUtils;
    private Project project;
    private UserAndJobActivity activity;
    private WorkingTime workingTime;
    private Object projectsList;
    private Button nextButton;
    private Button previousButton;
    private User connectedUser;
    private Button mostUsedProject;
    private Button lastUsedProject;
    private RadioButton hidden1;
    private RadioButton hidden2;
    private RadioButton hidden3;
    private RadioButton hidden4;
    private RadioButton hidden5;

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
        Bundle data = intent.getBundleExtra("data");
        this.workingTime = data.getParcelable(WorkingTimeContract.TABLE_NAME);
        this.connectedUser = data.getParcelable(UserContract.TABLE_NAME);
        this.projectsList = data.getParcelableArrayList("projects");

        this.projectProviderUtils = new ProjectProviderUtils(this.getContext());
        this.project = new Project();

        this.hidden1 = (RadioButton) view.findViewById(R.id.btn_project_hidden_1);
        this.hidden2 = (RadioButton) view.findViewById(R.id.btn_project_hidden_2);
        this.hidden3 = (RadioButton) view.findViewById(R.id.btn_project_hidden_3);
        this.hidden4 = (RadioButton) view.findViewById(R.id.btn_project_hidden_4);
        this.hidden5 = (RadioButton) view.findViewById(R.id.btn_project_hidden_5);

        this.addButton = (Button) view.findViewById(R.id.add_button);
        this.lessButton = (Button) view.findViewById(R.id.less_button);
        this.radioGroup = (RadioGroup) view.findViewById(R.id.radio_group_project);
        this.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selectedButton = (RadioButton) view.findViewById(i);
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
                if (hidden1.getVisibility() == View.GONE) {
                    hidden1.setVisibility(View.VISIBLE);
                    hidden2.setVisibility(View.VISIBLE);
                    hidden3.setVisibility(View.VISIBLE);
                    hidden4.setVisibility(View.VISIBLE);
                    hidden5.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.GONE);
                    lessButton.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.less_button:
                if (hidden1.getVisibility() == View.VISIBLE) {
                    hidden1.setVisibility(View.GONE);
                    hidden2.setVisibility(View.GONE);
                    hidden3.setVisibility(View.GONE);
                    hidden4.setVisibility(View.GONE);
                    hidden5.setVisibility(View.GONE);
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
