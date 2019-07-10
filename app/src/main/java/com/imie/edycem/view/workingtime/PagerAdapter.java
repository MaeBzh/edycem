package com.imie.edycem.view.workingtime;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.imie.edycem.R;

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return (5);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment result = null;
        switch (position) {
            case 0:
                result = new UserAndJobFragment();
                break;
            case 1:
                result = new ProjectsFragment();
                break;
            case 2:
                result = new TasksFragment();
                break;
            case 3:
                result = new WorkingTimeFragment();
                break;
            case 4:
                result = new SummaryFragment();
                break;
            default:
                break;
        }
        return result;
    }

}
