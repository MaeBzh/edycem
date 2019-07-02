/*
 * HomeActivity.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 2, 2019
 *
 */
package com.imie.edycem;

import com.imie.edycem.harmony.view.HarmonyFragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Home Activity.
 * This is from where you can access to your entities activities by default.
 * BEWARE : This class is regenerated with orm:generate:crud. Don't modify it.
 * @see android.app.Activity
 */
public class HomeActivity extends HarmonyFragmentActivity 
        implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.initButtons();
    }
    
    @Override
    protected int getContentView() {
        return R.layout.main;
    }

    /**
     * Initialize the buttons click listeners.
     */
    private void initButtons() {
    }
    
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            default:
                intent = null;
                break;
        }

        if (intent != null) {
            this.startActivity(intent);
        }
    }
}
