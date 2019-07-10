package com.imie.edycem.view.workingtime;


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
import com.imie.edycem.entity.Activity;
import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.Task;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.provider.utils.ProjectProviderUtils;
import com.imie.edycem.provider.utils.TaskProviderUtils;

public class TasksFragment extends Fragment {

    private RadioGroup radioGroup;
    private TaskProviderUtils taskProviderUtils;
    private WorkingTimeActivity workingTimeActivity;
    private WorkingTime workingTime;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);//

        this.initComponents(view);
        return view;
    }

    public void initComponents(final View view) {
        this.workingTimeActivity = (WorkingTimeActivity) this.getActivity();
        this.workingTime = this.workingTimeActivity.getWorkingTime();
        this.radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        this.taskProviderUtils = new TaskProviderUtils(this.getContext());
        this.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selectedButton = (RadioButton) view.findViewById(i);
                selectedButton.setBackground(ContextCompat.getDrawable(TasksFragment.this.getContext(), R.drawable.button_selector_blue));
                Task selectedTask = taskProviderUtils.queryWithName(selectedButton.getText().toString());
                TasksFragment.this.workingTime.setTask(selectedTask);
            }
        });
    }
}
