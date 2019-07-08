package com.imie.edycem.view.login;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.imie.edycem.R;
import com.imie.edycem.criterias.base.CriteriaExpression;
import com.imie.edycem.data.ActivityWebServiceClientAdapter;
import com.imie.edycem.data.JobWebServiceClientAdapter;
import com.imie.edycem.data.ProjectWebServiceClientAdapter;
import com.imie.edycem.data.TaskWebServiceClientAdapter;
import com.imie.edycem.data.UserWebServiceClientAdapter;
import com.imie.edycem.entity.Activity;
import com.imie.edycem.entity.Job;
import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.Task;
import com.imie.edycem.entity.User;
import com.imie.edycem.provider.contract.ActivityContract;
import com.imie.edycem.provider.contract.JobContract;
import com.imie.edycem.provider.contract.ProjectContract;
import com.imie.edycem.provider.contract.TaskContract;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.provider.utils.ActivityProviderUtils;
import com.imie.edycem.provider.utils.JobProviderUtils;
import com.imie.edycem.provider.utils.ProjectProviderUtils;
import com.imie.edycem.provider.utils.TaskProviderUtils;
import com.imie.edycem.provider.utils.UserProviderUtils;
import com.imie.edycem.view.workingtime.WorkingTimeActivity;

import java.util.ArrayList;
import java.util.List;

public class RgpdDialogFragment extends DialogFragment {

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rgpd, container, false);

        this.initComponents(view);
        return view;
    }

    public void initComponents(View view) {

    }
}
