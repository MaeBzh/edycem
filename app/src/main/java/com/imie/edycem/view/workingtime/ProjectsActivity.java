package com.imie.edycem.view.workingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.imie.edycem.R;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.view.login.LoginActivity;

public class ProjectsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_projects);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(this.toolbar);
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
                Intent intent = new Intent(ProjectsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        return true;
    }

}
