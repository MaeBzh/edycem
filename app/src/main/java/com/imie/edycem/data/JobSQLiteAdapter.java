/*
 * JobSQLiteAdapter.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.data;

import com.imie.edycem.data.base.JobSQLiteAdapterBase;


/**
 * Job adapter database class. 
 * This class will help you access your database to do any basic operation you
 * need. 
 * Feel free to modify it, override, add more methods etc.
 */
public class JobSQLiteAdapter extends JobSQLiteAdapterBase {

    /**
     * Constructor.
     * @param ctx context
     */
    public JobSQLiteAdapter(final android.content.Context ctx) {
        super(ctx);
    }
}
