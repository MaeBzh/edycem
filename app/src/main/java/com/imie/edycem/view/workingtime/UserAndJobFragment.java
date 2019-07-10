package com.imie.edycem.view.workingtime;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.imie.edycem.R;
import com.imie.edycem.entity.Job;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.provider.contract.WorkingTimeContract;
import com.imie.edycem.provider.utils.JobProviderUtils;
import com.imie.edycem.provider.utils.UserProviderUtils;

import java.util.ArrayList;
import java.util.List;

public class UserAndJobFragment extends Fragment implements View.OnClickListener {

    private UserProviderUtils userProviderUtils;
    private Spinner spinnerName;
    private Spinner spinnerJob;
    private ArrayList<User> users = new ArrayList<>();
    private WorkingTime workingTime;
    private User connectedUser;
    private Button nextButton;
    private ArrayList<Integer> projectsId;
    private UserAndJobActivity activity;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_and_job, container, false);//

        this.initComponents(view);
        return view;
    }

    public void initComponents(View view) {
        this.nextButton = (Button) view.findViewById(R.id.btn_next);
        this.nextButton.setOnClickListener(this);
        this.activity = (UserAndJobActivity) this.getActivity();
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            this.workingTime = intent.getParcelableExtra(WorkingTimeContract.TABLE_NAME);
        } else {
            this.workingTime = new WorkingTime();
        }
        this.workingTime = new WorkingTime();
        this.connectedUser = this.activity.getConnectedUser();
        this.projectsId = this.activity.getProjects();

        this.userProviderUtils = new UserProviderUtils(this.getContext());
        this.spinnerName = (Spinner) view.findViewById(R.id.spinner_name);

        this.users.addAll(userProviderUtils.queryAll());

        List<String> spinnerNameArray = new ArrayList<String>();
        for (User user : users) {
            spinnerNameArray.add(String.format("%s %s", user.getFirstname(), user.getLastname()));
        }
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(
                this.getContext(), android.R.layout.simple_spinner_item, spinnerNameArray);

        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerName.setAdapter(nameAdapter);
        this.spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String userStr = (String) spinnerName.getSelectedItem();
                String[] splitStr = userStr.split(" ");
                String firstname = splitStr[0];
                String lastname = splitStr[1];
                User selectedUser = userProviderUtils.queryWithName(firstname, lastname);
                UserAndJobFragment.this.workingTime.setUser(selectedUser);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        this.spinnerJob = (Spinner) view.findViewById(R.id.spinner_job);

        final JobProviderUtils jobProviderUtils = new JobProviderUtils(this.getContext());
        ArrayList<Job> jobs = jobProviderUtils.queryAll();
        List<String> spinnerJobArray = new ArrayList<String>();
        for (Job job : jobs) {
            spinnerJobArray.add(job.getName());
        }
        ArrayAdapter<String> jobAdapter = new ArrayAdapter<String>(
                this.getContext(), android.R.layout.simple_spinner_item, spinnerJobArray);

        jobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerJob.setAdapter(jobAdapter);
        this.spinnerJob.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (UserAndJobFragment.this.workingTime.getUser().getJob().getName() != spinnerJob.getSelectedItem().toString()) {
                    Job job = jobProviderUtils.queryWithName(spinnerJob.getSelectedItem().toString());
                    UserAndJobFragment.this.workingTime.getUser().setJob(job);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                UserAndJobFragment.this.workingTime.getUser().setJob(UserAndJobFragment.this.connectedUser.getJob());
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_next){
            Intent intent = new Intent(this.getContext(), ProjectsActivity.class);
            intent.putExtra(UserContract.TABLE_NAME, (Parcelable) this.connectedUser);
            intent.putExtra(WorkingTimeContract.TABLE_NAME, (Parcelable) this.workingTime);
            intent.putExtra("projects_id", this.projectsId);
            startActivity(intent);
        }

    }
}
