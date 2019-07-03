/*
 * SettingsSQLiteAdapter.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.data;

import com.imie.edycem.data.base.SettingsSQLiteAdapterBase;


/**
 * Settings adapter database class. 
 * This class will help you access your database to do any basic operation you
 * need. 
 * Feel free to modify it, override, add more methods etc.
 */
public class SettingsSQLiteAdapter extends SettingsSQLiteAdapterBase {

    /**
     * Constructor.
     * @param ctx context
     */
    public SettingsSQLiteAdapter(final android.content.Context ctx) {
        super(ctx);
    }
}
