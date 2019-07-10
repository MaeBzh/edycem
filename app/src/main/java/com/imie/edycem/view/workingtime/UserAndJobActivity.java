package com.imie.edycem.view.workingtime;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.imie.edycem.R;
import com.imie.edycem.data.ProjectWebServiceClientAdapter;
import com.imie.edycem.data.UserWebServiceClientAdapter;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.provider.utils.SettingsProviderUtils;
import com.imie.edycem.provider.utils.UserProviderUtils;
import com.imie.edycem.view.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserAndJobActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton logoutButton;
    private User connectedUser;
    private ArrayList<Integer> projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_user_and_job);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(this.toolbar);
        Intent intent = getIntent();
        this.connectedUser = intent.getParcelableExtra(UserContract.TABLE_NAME);
        new DataTask(this, this.connectedUser).execute();
    }

    public ArrayList<Integer> getProjects() {
        return this.projects;
    }

    public User getConnectedUser() {
        return connectedUser;
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
                Intent intent = new Intent(UserAndJobActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        return true;
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

            if (userWS.updateRgpd(this.connectedUser, rgdp) == 0) {
                UserProviderUtils userProviderUtils = new UserProviderUtils(this.currentContext);
                userProviderUtils.update(this.connectedUser);
            }


            ProjectWebServiceClientAdapter projectWS = new ProjectWebServiceClientAdapter(this.currentContext);

            return projectWS.getProjectList(this.connectedUser);

        }

        @Override
        protected void onPostExecute(ArrayList<Integer> projects) {
            UserAndJobActivity.this.projects = projects;
        }

    }




}
