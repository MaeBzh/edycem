/*
 * HarmonyFragment.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 2, 2019
 *
 */
package com.imie.edycem.harmony.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.imie.edycem.menu.EdycemMenu;

/**
 * Harmony custom Fragment.
 * This fragment will help you use a lot of harmony's functionnality
 * (menu wrappers, etc).
 */
public abstract class HarmonyFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        
        try {
            EdycemMenu.getInstance(this.getActivity(), this)
                                            .clear(menu);
            EdycemMenu.getInstance(this.getActivity(), this)
                                          .updateMenu(menu, this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result;

        try {
            result = EdycemMenu.getInstance(
                   this.getActivity(), this).dispatch(item, this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            EdycemMenu.getInstance(this.getActivity(), this)
            .onActivityResult(requestCode, resultCode, data, this.getActivity(),
            this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
