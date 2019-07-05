package com.imie.edycem.view.workingtime;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imie.edycem.R;

public class PageFragment extends Fragment {

    private static final String KEY_POSITION = "position";


    public PageFragment() {
    }

    public static PageFragment newInstance(int position) {

        PageFragment frag = new PageFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        frag.setArguments(args);

        return (frag);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        int position = getArguments().getInt(KEY_POSITION, -1);

        View result = null;

        switch (position) {
            case 0:
                result = inflater.inflate(R.layout.fragment_user_and_job, container, false);
                break;
            case 1:
                result = inflater.inflate(R.layout.fragment_projects, container, false);
                break;
            case 2:
                result = inflater.inflate(R.layout.fragment_task, container, false);
                break;
            default:
                break;
        }

        return result;
    }

}
