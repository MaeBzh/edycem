package com.imie.edycem.view.workingtime;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.imie.edycem.R;
import com.imie.edycem.entity.Job;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.provider.utils.JobProviderUtils;
import com.imie.edycem.provider.utils.UserProviderUtils;

import java.util.ArrayList;
import java.util.List;

public class UserAndJobFragment extends Fragment {

    private UserProviderUtils userProviderUtils;
    private Spinner spinnerName;
    private ArrayList<User> users = new ArrayList<>();
    private WorkingTimeActivity activity;
    private WorkingTime workingTime;
    private User connectedUser;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_and_job, container, false);//

        this.initComponents(view);
        return view;
    }

    public void initComponents(View view) {
        this.activity = (WorkingTimeActivity) this.getActivity();
        this.workingTime = this.activity.getWorkingTime();
        this.connectedUser = this.activity.getConnectedUser();

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

        Spinner spinnerJob = (Spinner) view.findViewById(R.id.spinner_job);

        JobProviderUtils jobProviderUtils = new JobProviderUtils(this.getContext());
        ArrayList<Job> jobs = jobProviderUtils.queryAll();
        List<String> spinnerJobArray = new ArrayList<String>();
        for (Job job : jobs) {
            spinnerJobArray.add(job.getName());
        }
        ArrayAdapter<String> jobAdapter = new ArrayAdapter<String>(
                this.getContext(), android.R.layout.simple_spinner_item, spinnerJobArray);

        jobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJob.setAdapter(jobAdapter);

    }

    public interface WorkingTimeListener {
        void onWorkingTimeUpdating(WorkingTime workingTime);
    }
}
