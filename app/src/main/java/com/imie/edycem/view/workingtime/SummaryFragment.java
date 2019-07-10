package com.imie.edycem.view.workingtime;


import android.content.Context;
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
import android.widget.TextView;

import com.imie.edycem.R;
import com.imie.edycem.data.WorkingTimeWebServiceClientAdapter;
import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.provider.utils.ProjectProviderUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

public class SummaryFragment extends Fragment {

    private WorkingTimeActivity activity;
    private WorkingTime workingTime;
    private TextView user;
    private TextView job;
    private TextView project;
    private TextView task;
    private TextView date;
    private TextView time;
    private TextView comment;
    private Button submit;
    private Button edit;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);//

        this.initComponents(view);
        return view;
    }

    public void initComponents(final View view) {

        this.activity = (WorkingTimeActivity) this.getActivity();
        this.workingTime = this.activity.getWorkingTime();

        this.user = (TextView) view.findViewById(R.id.input_user);
        this.user.setText(String.format("%s %s", this.workingTime.getUser().getFirstname(), this.workingTime.getUser().getLastname()));

        this.job = (TextView) view.findViewById(R.id.input_job);
        this.job.setText(this.workingTime.getUser().getJob().getName());

        this.project = (TextView) view.findViewById(R.id.input_project);
//        this.project.setText(this.workingTime.getProject().getName());

//        this.task = (TextView) view.findViewById(R.id.input_task);
//        this.task.setText(this.workingTime.getTask().getName());

        this.date = (TextView) view.findViewById(R.id.input_date);
        DateTimeFormatter formatter = DateTimeFormat.forPattern(getString(R.string.date_pattern));
        String dateStr = formatter.print(this.workingTime.getDate());
        this.date.setText(dateStr);

        this.time = (TextView) view.findViewById(R.id.input_spent_time);
        this.time.setText(String.format("%s", this.workingTime.getSpentTime()));
//        time.setText(String.format("%sh %smin", (this.workingTime.getSpentTime() / 60), (this.workingTime.getSpentTime() % 60)));

        this.comment = (TextView) view.findViewById(R.id.input_comment);
        this.comment.setText(this.workingTime.getDescription());

        this.submit = (Button) view.findViewById(R.id.btn_submit);
        this.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WorkingTimeTask(SummaryFragment.this.getContext(), SummaryFragment.this.workingTime.getUser(), SummaryFragment.this.workingTime).execute();
                System.out.println("working time : " + SummaryFragment.this.workingTime.toString());
            }
        });

        this.edit = (Button) view.findViewById(R.id.btn_edit);
        this.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    /**
     * Create a thread to use webservices for sending the date of rgpd acceptation.
     */
    public class WorkingTimeTask extends AsyncTask<Void, Void, Void> {

        /**
         * Context from the fragment.
         */
        private Context currentContext;
        private User user;
        private WorkingTime workingTime;

        /**
         * Constructor.
         *
         * @param context Context
         */
        protected WorkingTimeTask(Context context, User user, WorkingTime workingTime) {

            this.currentContext = context;
            this.user = user;
            this.workingTime = workingTime;

        }


        @Override
        protected Void doInBackground(Void... params) {
            WorkingTimeWebServiceClientAdapter workingTimeWS = new WorkingTimeWebServiceClientAdapter(this.currentContext);
            JSONObject workingTime = new JSONObject();
            try {
//                workingTime.put("user_id", this.user.getIdServer());
//                workingTime.put("project_id", this.workingTime.getProject().getIdServer());
//                workingTime.put("task_id", this.workingTime.getTask().getId());
//                workingTime.put("date", this.workingTime.getDate());
//                workingTime.put("spent_time", this.workingTime.getSpentTime());
//                workingTime.put("description", this.workingTime.getDescription());

                workingTime.put("user_id", "1");
                workingTime.put("project_id", "1");
                workingTime.put("task_id", "3");
                workingTime.put("date", DateTime.now());
                workingTime.put("spent_time", 60);
                workingTime.put("description", "azertyuiop");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            workingTimeWS.insertWorkingTime(this.user, workingTime);

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
//            todo : toast
        }

    }
}
