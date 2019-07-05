package com.imie.edycem.view.workingtime;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.imie.edycem.R;

public class WorkingTimeActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_working_time);
        this.configureViewPager();
    }


    private void configureViewPager() {
        this.viewPager = (ViewPager) findViewById(R.id.working_time_pager);
        this.viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()) {
        });
    }

    public void nextPage(View view) {
        if(view.getId() == R.id.btn_next) {
            this.viewPager.setCurrentItem(this.viewPager.getCurrentItem() + 1, true);
        }
    }

    public void previousPage(View view) {
        if(view.getId() == R.id.btn_next) {
            this.viewPager.setCurrentItem(this.viewPager.getCurrentItem() - 1, true);
        }
    }
}
