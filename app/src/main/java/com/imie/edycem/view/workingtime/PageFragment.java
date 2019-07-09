package com.imie.edycem.view.workingtime;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.imie.edycem.R;
import com.imie.edycem.entity.Job;
import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.Task;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.provider.utils.ActivityProviderUtils;
import com.imie.edycem.provider.utils.JobProviderUtils;
import com.imie.edycem.provider.utils.ProjectProviderUtils;
import com.imie.edycem.provider.utils.TaskProviderUtils;
import com.imie.edycem.provider.utils.UserProviderUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class PageFragment extends Fragment {

    private static final String KEY_POSITION = "position";
    private User connectedUser;
    private User user;
    private WorkingTime workingTime;


    public PageFragment() {
    }

    public static PageFragment newInstance(int position) {

        PageFragment frag = new PageFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        frag.setArguments(args);

        return (frag);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        int position = getArguments().getInt(KEY_POSITION, -1);
        Intent intent = getActivity().getIntent();
        this.connectedUser = intent.getParcelableExtra(UserContract.TABLE_NAME);
        this.user = new User();
        this.workingTime = new WorkingTime();

        View view = null;
        switch (position) {
            case 0:
                view = inflater.inflate(R.layout.fragment_user_and_job, container, false);
                break;
            case 1:
                view = inflater.inflate(R.layout.fragment_projects, container, false);
                break;
            case 2:
                view = inflater.inflate(R.layout.fragment_task, container, false);
                break;
            case 3:
                view = inflater.inflate(R.layout.fragment_working_time, container, false);
                break;
            case 4:
                view = inflater.inflate(R.layout.fragment_summary, container, false);
            default:
                break;
        }

        this.initComponents(view);

        return view;
    }

    public void initComponents(View view) {
        switch (view.getId()) {
            case R.id.layout_user_and_job:
                this.initUserPage(view);
                break;
            case R.id.layout_projects:
                this.initProjectPage(view);
                break;
            case R.id.layout_tasks:
                this.initTaskPage(view);
                break;
            case R.id.layout_working_time:
                this.initWorkingTimePage(view);
                break;
            case R.id.layout_summary:
//                this.submitWorkingTime();

        }
    }

    public void initUserPage(View view) {
        UserProviderUtils userProviderUtils = new UserProviderUtils(this.getContext());
        Spinner spinnerName = (Spinner) view.findViewById(R.id.spinner_name);
        ArrayList<User> users = userProviderUtils.queryAll();
        List<String> spinnerNameArray = new ArrayList<String>();
        for (User user : users) {
            spinnerNameArray.add(String.format("%s %s", user.getFirstname(), user.getLastname()));
        }
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(
                this.getContext(), android.R.layout.simple_spinner_item, spinnerNameArray);

        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerName.setAdapter(nameAdapter);

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

        String userStr = (String) spinnerName.getSelectedItem();
        String[] splitStr = userStr.split(" ");
        String firstname = splitStr[0];
        String lastname = splitStr[1];
        User selectedUser = userProviderUtils.queryWithName(firstname, lastname);
        if (selectedUser.getFirstname().equals(this.connectedUser.getFirstname())
                && selectedUser.getLastname().equals(this.connectedUser.getLastname())) {
            this.workingTime.setUser(this.connectedUser);
        } else {
            this.workingTime.setUser(selectedUser);
        }

    }

    public void initProjectPage(final View view) {

        final ConstraintLayout hiddenProject = (ConstraintLayout) view.findViewById(R.id.layout_hidden_projects);
        final Button addButton = (Button) view.findViewById(R.id.add_button);
        final Button lessButton = (Button) view.findViewById(R.id.less_button);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group_project);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selectedButton = (RadioButton) view.findViewById(i);
                selectedButton.setBackground(ContextCompat.getDrawable(PageFragment.this.getContext(), R.drawable.button_selector_blue));
//                selectedButton.setBackgroundColor(ContextCompat.getColor(PageFragment.this.getContext(), R.color.primary_color_dark));
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hiddenProject.getVisibility() == View.GONE) {
                    hiddenProject.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.GONE);
                    lessButton.setVisibility(View.VISIBLE);
                }
            }
        });

        lessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hiddenProject.getVisibility() == View.VISIBLE) {
                    hiddenProject.setVisibility(View.GONE);
                    addButton.setVisibility(View.VISIBLE);
                    lessButton.setVisibility(View.GONE);
                }
            }
        });
        ProjectProviderUtils projectProviderUtils = new ProjectProviderUtils(this.getContext());
        Project project = new Project();
//        Project project = projectProviderUtils.query();
        this.workingTime.setProject(project);
    }

    public void initTaskPage(final View view) {
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        final TaskProviderUtils taskProviderUtils = new TaskProviderUtils(this.getContext());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selectedButton = (RadioButton) view.findViewById(i);
                selectedButton.setBackground(ContextCompat.getDrawable(PageFragment.this.getContext(), R.drawable.button_selector_blue));
                Task selectedTask = taskProviderUtils.queryWithName(selectedButton.getText().toString());
                PageFragment.this.workingTime.setTask(selectedTask);
            }
        });
    }

    public void initWorkingTimePage(View view) {
        EditText date = (EditText) view.findViewById(R.id.edit_date);
        Spinner hours = (Spinner) view.findViewById(R.id.spinner_hour);
        Spinner minutes = (Spinner) view.findViewById(R.id.spinner_min);
        EditText comment = (EditText) view.findViewById(R.id.edit_comment);

        int spentTime = (Integer.parseInt(hours.getSelectedItem().toString()) * 60 ) + Integer.parseInt(minutes.getSelectedItem().toString());
        this.workingTime.setSpentTime(spentTime);

        this.workingTime.setDate(DateTime.parse(date.getText().toString()));

        this.workingTime.setDescription(comment.getText().toString());

    }
}
