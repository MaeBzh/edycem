package com.imie.edycem.view.workingtime;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.imie.edycem.R;
import com.imie.edycem.data.ProjectWebServiceClientAdapter;
import com.imie.edycem.data.UserWebServiceClientAdapter;
import com.imie.edycem.data.WorkingTimeWebServiceClientAdapter;
import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.view.login.LoginActivity;
import com.imie.edycem.view.login.LoginFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WorkingTimeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Toolbar toolbar;
    private ImageButton logoutButton;
    private WorkingTime workingTime;
    private User connectedUser;
    private ArrayList<Integer> projects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_working_time);
        this.configureViewPager();
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(this.toolbar);
        this.workingTime = new WorkingTime();
        Intent intent = getIntent();
        this.connectedUser = intent.getParcelableExtra(UserContract.TABLE_NAME);
        new DataTask(this, this.connectedUser).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        this.logoutButton = (ImageButton) menu.findItem(R.id.logout).getActionView();
        this.logoutButton.setBackground(getDrawable(R.drawable.logout));
        this.logoutButton.setPaddingRelative(0, 0, 20, 0);
        this.logoutButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkingTimeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        return true;
    }

    public WorkingTime getWorkingTime() {
        return this.workingTime;
    }

    public User getConnectedUser() {
        return this.connectedUser;
    }

    public ArrayList<Integer> getProjects() {
        return this.projects;
    }

    private void configureViewPager() {
        this.viewPager = (ViewPager) findViewById(R.id.working_time_pager);
        this.viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()) {
        });
    }

    public void nextPage(View view) {
        if (view.getId() == R.id.btn_next) {
            this.viewPager.setCurrentItem(this.viewPager.getCurrentItem() + 1, true);
        }
    }

    public void previousPage(View view) {
        if (view.getId() == R.id.btn_previous) {
            this.viewPager.setCurrentItem(this.viewPager.getCurrentItem() - 1, true);
        }
    }

    public void firstPage(View view) {
        if (view.getId() == R.id.btn_edit) {
            this.viewPager.setCurrentItem(0, true);
        }
    }

    /**
     * Create a thread to use webservices for sending the date of rgpd acceptation.
     */
    public class DataTask extends AsyncTask<Void, Void, ArrayList<Integer>> {

        /**
         * Context from the fragment.
         */
        private Context currentContext;
        private User connectedUser;

        /**
         * Constructor.
         *
         * @param context Context
         */
        protected DataTask(Context context, User connectedUser) {

            this.currentContext = context;
            this.connectedUser = connectedUser;

        }


        @Override
        protected ArrayList<Integer> doInBackground(Void... params) {
//            todo: add date rgpd in SQLite db
            UserWebServiceClientAdapter userWS = new UserWebServiceClientAdapter(this.currentContext);
            JSONObject rgdp = new JSONObject();
            try {
                rgdp.put("id", this.connectedUser.getIdServer());
                rgdp.put("date_rgpd", this.connectedUser.getDateRgpd());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            userWS.updateRgpd(this.connectedUser, rgdp);

            ProjectWebServiceClientAdapter projectWS = new ProjectWebServiceClientAdapter(this.currentContext);

            return projectWS.getProjectList(this.connectedUser);

        }

        @Override
        protected void onPostExecute(ArrayList<Integer> projects) {
            WorkingTimeActivity.this.projects = projects;
        }

    }



}
