/*
 * EdycemSQLiteOpenHelper.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.data;

import com.imie.edycem.data.base.EdycemSQLiteOpenHelperBase;

import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * This class makes it easy for ContentProvider implementations to defer <br />
 * opening and upgrading the database until first use, to avoid blocking <br />
 * application startup with long-running database upgrades.
 * @see android.database.sqlite.SQLiteOpenHelper
 */
public class EdycemSQLiteOpenHelper
                    extends EdycemSQLiteOpenHelperBase {

    /**
     * Constructor.
     * @param ctx context
     * @param name name
     * @param factory factory
     * @param version version
     */
    public EdycemSQLiteOpenHelper(final android.content.Context ctx,
           final String name, final CursorFactory factory, final int version) {
        super(ctx, name, factory, version);
    }

}
