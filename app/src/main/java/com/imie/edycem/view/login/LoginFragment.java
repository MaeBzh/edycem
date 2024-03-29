package com.imie.edycem.view.login;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.imie.edycem.view.workingtime.UserAndJobActivity;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment {

    private EditText editEmail;
    private String email;
    private Button submitButton;
    private ProgressBar progressBar;
    private UserProviderUtils userProviderUtils;
    private User user;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        this.initComponents(view);
        return view;
    }

    public void initComponents(View view) {
        LoginFragment.this.userProviderUtils = new UserProviderUtils(this.getContext());
        this.editEmail = (EditText) view.findViewById(R.id.edit_email);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progress);
        this.submitButton = (Button) view.findViewById(R.id.button_submit);
        this.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginFragment.this.user = new User();
                LoginFragment.this.email = LoginFragment.this.editEmail.getText().toString();
                user.setEmail(String.valueOf(LoginFragment.this.editEmail.getText()));
                LoginFragment.this.progressBar.setVisibility(View.VISIBLE);
                ConnectivityManager cm = (ConnectivityManager) LoginFragment.this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                    new LoginTask(LoginFragment.this.getContext()).execute(user);
                } else {
                    LoginFragment.this.user = LoginFragment.this.userProviderUtils.queryWithEmail(user.getEmail());
                    if (LoginFragment.this.user != null) {
                        startMainActivity(LoginFragment.this.user);
                    } else {
                        LoginFragment.this.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), getString(R.string.authentication_fail), Toast.LENGTH_LONG).show();
                    }
                }

//                startMainActivity(LoginFragment.this.user);

            }
        });
    }

    /**
     * Start new main activity with the connected user id's.
     */
    public void startMainActivity(User connectedUser) {
        Intent intent = new Intent(this.getContext(), UserAndJobActivity.class);
        intent.putExtra(UserContract.TABLE_NAME, (Parcelable) connectedUser);
        startActivity(intent);
    }

    /**
     * Create a thread to use webservices retrieving the project, jobs, activities, tasks and users.
     */
    public class LoginTask extends AsyncTask<User, Void, User> {

        /**
         * Context from the fragment.
         */
        private Context currentContext;

        /**
         * Constructor.
         *
         * @param context Context
         */
        protected LoginTask(Context context) {

            this.currentContext = context;

        }

        /**
         * Call webservices for updating the database.
         */
        private void getDatas(User connectedUser) {

            ArrayList<Activity> activities = new ArrayList<>();
            ArrayList<Task> tasks = new ArrayList<>();
            ArrayList<Job> jobs = new ArrayList<>();
            ArrayList<Project> projects = new ArrayList<>();
            ArrayList<User> users = new ArrayList<>();

            ActivityWebServiceClientAdapter activityWS = new ActivityWebServiceClientAdapter(this.currentContext);
            ActivityProviderUtils activityProviderUtils = new ActivityProviderUtils(this.currentContext);
            activities = activityWS.getAllActivities(connectedUser);

            if (activities != null) {
                for (Activity activity : activities) {
                    CriteriaExpression criteriaActivity = new CriteriaExpression(CriteriaExpression.GroupType.AND);
                    criteriaActivity.add(ActivityContract.COL_IDSERVER, String.valueOf(activity.getIdServer()));
                    List<Activity> query = activityProviderUtils.query(criteriaActivity);

                    if (query.isEmpty()) {
                        activityProviderUtils.insert(activity);
                    }
                }
            }

            TaskWebServiceClientAdapter taskWS = new TaskWebServiceClientAdapter(this.currentContext);
            TaskProviderUtils taskProviderUtils = new TaskProviderUtils(this.currentContext);
            tasks = taskWS.getAllTasks(connectedUser);

            if (tasks != null) {
                for (Task task : tasks) {
                    CriteriaExpression criteriaTask = new CriteriaExpression(CriteriaExpression.GroupType.AND);
                    criteriaTask.add(TaskContract.COL_IDSERVER, String.valueOf(task.getIdServer()));
                    List<Task> queryTasks = taskProviderUtils.query(criteriaTask);

                    if (queryTasks.isEmpty()) {
                        taskProviderUtils.insert(task);
                    }
                }
            }

            JobWebServiceClientAdapter jobWS = new JobWebServiceClientAdapter(this.currentContext);
            JobProviderUtils jobProviderUtils = new JobProviderUtils(this.currentContext);
            jobs = jobWS.getAllJobs(connectedUser);

            if (jobs != null) {
                for (Job job : jobs) {
                    CriteriaExpression criteriaJob = new CriteriaExpression(CriteriaExpression.GroupType.AND);
                    criteriaJob.add(JobContract.COL_IDSERVER, String.valueOf(job.getIdServer()));
                    List<Job> queryJobs = jobProviderUtils.query(criteriaJob);

                    if (queryJobs.isEmpty()) {
                        jobProviderUtils.insert(job);
                    }
                }
            }

            UserWebServiceClientAdapter userWS = new UserWebServiceClientAdapter(this.currentContext);
            users = userWS.getAllUsers(connectedUser);

            if (users != null) {
                for (User user : users) {
                    CriteriaExpression criteriaUser = new CriteriaExpression(CriteriaExpression.GroupType.AND);
                    criteriaUser.add(UserContract.COL_IDSERVER, String.valueOf(user.getIdServer()));
                    List<User> queryUsers = LoginFragment.this.userProviderUtils.query(criteriaUser);

                    if (queryUsers.isEmpty()) {
                        LoginFragment.this.userProviderUtils.insert(user);
                    }
                }
            }

            ProjectWebServiceClientAdapter projectWS = new ProjectWebServiceClientAdapter(this.currentContext);
            ProjectProviderUtils projectProviderUtils = new ProjectProviderUtils(this.currentContext);
            projects = projectWS.getAllProjects(connectedUser);

            if (projects != null) {
                for (Project project : projects) {
                    CriteriaExpression criteriaProject = new CriteriaExpression(CriteriaExpression.GroupType.AND);
                    criteriaProject.add(ProjectContract.COL_IDSERVER, String.valueOf(project.getIdServer()));
                    List<Project> queryProjects = projectProviderUtils.query(criteriaProject);

                    if (queryProjects.isEmpty()) {
                        projectProviderUtils.insert(project);
                    }
                }
            }
        }

        @Override
        protected User doInBackground(User... users) {
            User user = users[0];
            User result;
            UserWebServiceClientAdapter userWS =
                    new UserWebServiceClientAdapter(this.currentContext);
            result = userWS.getMatchingUser(user);
            if (result != null) {
                LoginTask.this.getDatas(result);
            }

            return result;
        }

        /**
         * Show the wrongLogin text view if the user has no authorization.
         *
         * @param result the result
         */
        @Override
        protected void onPostExecute(final User result) {
            super.onPostExecute(result);

            if (result != null) {
                LoginFragment.this.userProviderUtils = new UserProviderUtils(currentContext);
                if (LoginFragment.this.userProviderUtils.queryWithEmail(result.getEmail()) == null) {
                    LoginFragment.this.userProviderUtils.insert(result);
                } else {
                    LoginFragment.this.userProviderUtils.update(result);
                }

                LayoutInflater inflater = LoginFragment.this.getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.fragment_rgpd, null);

                if (result.getDateRgpd().getYear() != DateTime.now().getYear()) {
                    new AlertDialog.Builder(LoginFragment.this.getContext())
                            .setView(dialogView)
                            .setTitle("RGPD")
                            .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(LoginFragment.this.getContext(),
                                            LoginFragment.this.getContext().getString(R.string.authentication_ok),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    result.setDateRgpd(DateTime.now());
                                    userProviderUtils.update(result);
                                    LoginFragment.this.startMainActivity(result);
                                }
                            })

                            .setNegativeButton(getString(R.string.deny), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    LoginFragment.this.editEmail.setText("");
                                    LoginFragment.this.progressBar.setVisibility(View.INVISIBLE);
                                }
                            })
                            .show();
                } else {
                    LoginFragment.this.startMainActivity(result);
                }
            } else {
                Toast.makeText(LoginFragment.this.getContext(),
                        LoginFragment.this.getContext().getString(R.string.authentication_fail),
                        Toast.LENGTH_SHORT)
                        .show();
                LoginFragment.this.progressBar.setVisibility(View.GONE);
                LoginFragment.this.editEmail.requestFocus();
            }
        }
    }
}
