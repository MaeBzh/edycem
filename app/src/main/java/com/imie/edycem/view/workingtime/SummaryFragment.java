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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.imie.edycem.R;
import com.imie.edycem.data.WorkingTimeWebServiceClientAdapter;
import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.provider.contract.WorkingTimeContract;
import com.imie.edycem.provider.utils.ProjectProviderUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

public class SummaryFragment extends Fragment implements View.OnClickListener {

    private WorkingTimeActivity activity;
    private WorkingTime workingTime;
    private TextView user;
    private TextView job;
    private TextView project;
    private TextView task;
    private TextView date;
    private TextView time;
    private TextView comment;
    private TextView contributorView;
    private Button submit;
    private Button edit;
    private Button previous;
    private User contributor;
    private User connectedUser;
    private ProgressBar progressBar;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);//

        this.initComponents(view);
        return view;
    }

    public void initComponents(final View view) {

        Intent intent = getActivity().getIntent();
        this.workingTime = intent.getParcelableExtra(WorkingTimeContract.TABLE_NAME);
        this.contributor = intent.getParcelableExtra("contributor");
        this.connectedUser = intent.getParcelableExtra(UserContract.TABLE_NAME);

        this.user = (TextView) view.findViewById(R.id.input_user);
        this.user.setText(String.format("%s %s", this.workingTime.getUser().getFirstname(), this.workingTime.getUser().getLastname()));

        this.job = (TextView) view.findViewById(R.id.input_job);
        this.job.setText(this.workingTime.getUser().getJob().getName());

        this.project = (TextView) view.findViewById(R.id.input_project);
        this.project.setText(this.workingTime.getProject().getName());

        this.task = (TextView) view.findViewById(R.id.input_task);
        this.task.setText(this.workingTime.getTask().getName());

        this.date = (TextView) view.findViewById(R.id.input_date);
        DateTimeFormatter formatter = DateTimeFormat.forPattern(getString(R.string.date_pattern));
        String dateStr = formatter.print(this.workingTime.getDate());
        this.date.setText(dateStr);

        this.time = (TextView) view.findViewById(R.id.input_spent_time);
        this.time.setText(String.format("%sh %smin", (this.workingTime.getSpentTime() / 60), (this.workingTime.getSpentTime() % 60)));

        this.comment = (TextView) view.findViewById(R.id.input_comment);
        this.comment.setText(this.workingTime.getDescription());

        this.contributorView = (TextView) view.findViewById(R.id.input_contributors);
        this.contributorView.setText(String.format("%s %s", this.contributor.getFirstname(), this.contributor.getLastname()));

        this.submit = (Button) view.findViewById(R.id.btn_submit);
        this.submit.setOnClickListener(this);

//        this.edit = (Button) view.findViewById(R.id.btn_edit);
//        this.edit.setOnClickListener(this);

        this.previous = (Button) view.findViewById(R.id.btn_previous);
        this.previous.setOnClickListener(this);

        this.progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit) {
            this.progressBar.setVisibility(View.VISIBLE);
            new WorkingTimeTask(SummaryFragment.this.getContext(), SummaryFragment.this.workingTime).execute();

//        } else if (view.getId() == R.id.btn_edit) {
//            Intent intent = new Intent(this.getActivity(), UserAndJobActivity.class);
//            intent.putExtra(WorkingTimeContract.TABLE_NAME, (Parcelable) this.workingTime);
//            startActivity(intent);

        } else if (view.getId() == R.id.btn_previous) {
            this.getActivity().onBackPressed();
        }
    }

    /**
     * Create a thread to use webservices for sending the date of rgpd acceptation.
     */
    public class WorkingTimeTask extends AsyncTask<Void, Void, Integer> {

        /**
         * Context from the fragment.
         */
        private Context currentContext;
        private WorkingTime workingTime;

        /**
         * Constructor.
         *
         * @param context Context
         */
        protected WorkingTimeTask(Context context, WorkingTime workingTime) {

            this.currentContext = context;
            this.workingTime = workingTime;

        }

        @Override
        protected Integer doInBackground(Void... params) {
            WorkingTimeWebServiceClientAdapter workingTimeWS = new WorkingTimeWebServiceClientAdapter(this.currentContext);
            JSONObject jsonWorkingTime = new JSONObject();
            try {
                jsonWorkingTime.put("user_id", this.workingTime.getUser().getIdServer());
//                jsonWorkingTime.put("project_id", this.workingTime.getProject().getIdServer());
                jsonWorkingTime.put("project_id", "4");
                jsonWorkingTime.put("task_id", this.workingTime.getTask().getIdServer());
                jsonWorkingTime.put("date", this.workingTime.getDate());
                jsonWorkingTime.put("spent_time", this.workingTime.getSpentTime());
                jsonWorkingTime.put("description", this.workingTime.getDescription());


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return workingTimeWS.insertWorkingTime(SummaryFragment.this.workingTime.getUser(), jsonWorkingTime);

        }

        @Override
        protected void onPostExecute(final Integer result) {
            if (result == 0) {
                Toast.makeText(this.currentContext, getString(R.string.valid_entry), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this.currentContext, UserAndJobActivity.class);
                intent.putExtra(UserContract.TABLE_NAME, (Parcelable) SummaryFragment.this.connectedUser);
                startActivity(intent);
            } else {
                Toast.makeText(this.currentContext, getString(R.string.error_entry), Toast.LENGTH_LONG).show();
            }
            SummaryFragment.this.progressBar.setVisibility(View.GONE);
        }
    }
}
