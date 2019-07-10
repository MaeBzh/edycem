package com.imie.edycem.view.workingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.imie.edycem.R;
import com.imie.edycem.entity.User;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.view.login.LoginActivity;

public class WorkingTimeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Toolbar toolbar;
    private ImageButton logoutButton;
    private User connectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_working_time);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(this.toolbar);
        Intent intent = getIntent();
        this.connectedUser = intent.getParcelableExtra(UserContract.TABLE_NAME);

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
}
