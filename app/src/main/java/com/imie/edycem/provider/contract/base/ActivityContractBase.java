/*
 * ActivityContractBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 5, 2019
 *
 */
package com.imie.edycem.provider.contract.base;

import android.content.ContentValues;


import java.util.ArrayList;

import com.imie.edycem.entity.Activity;
import com.imie.edycem.entity.Task;



import com.imie.edycem.provider.contract.ActivityContract;

/** Edycem contract base.
 *
 * This class is regenerated. DO NOT MODIFY.
 */
public abstract class ActivityContractBase {


        /** id. */
    public static final String COL_ID =
            "id";
    /** Alias. */
    public static final String ALIASED_COL_ID =
            ActivityContract.TABLE_NAME + "." + COL_ID;

    /** idServer. */
    public static final String COL_IDSERVER =
            "idServer";
    /** Alias. */
    public static final String ALIASED_COL_IDSERVER =
            ActivityContract.TABLE_NAME + "." + COL_IDSERVER;

    /** name. */
    public static final String COL_NAME =
            "name";
    /** Alias. */
    public static final String ALIASED_COL_NAME =
            ActivityContract.TABLE_NAME + "." + COL_NAME;




    /** Constant for parcelisation/serialization. */
    public static final String PARCEL = "Activity";
    /** Table name of SQLite database. */
    public static final String TABLE_NAME = "Activity";
    /** Global Fields. */
    public static final String[] COLS = new String[] {

        
        ActivityContract.COL_ID,
        
        ActivityContract.COL_IDSERVER,
        
        ActivityContract.COL_NAME,
    };

    /** Global Fields. */
    public static final String[] ALIASED_COLS = new String[] {
        
        ActivityContract.ALIASED_COL_ID,
        
        ActivityContract.ALIASED_COL_IDSERVER,
        
        ActivityContract.ALIASED_COL_NAME,
        
    };


    /**
     * Converts a Activity into a content values.
     *
     * @param item The Activity to convert
     *
     * @return The content values
     */
    public static ContentValues itemToContentValues(final Activity item) {
        final ContentValues result = new ContentValues();

             result.put(ActivityContract.COL_ID,
                String.valueOf(item.getId()));

             result.put(ActivityContract.COL_IDSERVER,
                String.valueOf(item.getIdServer()));

             if (item.getName() != null) {
                result.put(ActivityContract.COL_NAME,
                    item.getName());
            }

 
        return result;
    }

    /**
     * Converts a Cursor into a Activity.
     *
     * @param cursor The cursor to convert
     *
     * @return The extracted Activity
     */
    public static Activity cursorToItem(final android.database.Cursor cursor) {
        Activity result = new Activity();
        ActivityContract.cursorToItem(cursor, result);
        return result;
    }

    /**
     * Convert Cursor of database to Activity entity.
     * @param cursor Cursor object
     * @param result Activity entity
     */
    public static void cursorToItem(final android.database.Cursor cursor, final Activity result) {
        if (cursor.getCount() != 0) {
            int index;

            index = cursor.getColumnIndex(ActivityContract.COL_ID);

            if (index > -1) {
                result.setId(cursor.getInt(index));
            }
            index = cursor.getColumnIndex(ActivityContract.COL_IDSERVER);

            if (index > -1) {
            if (!cursor.isNull(index)) {
                    result.setIdServer(cursor.getInt(index));
            }
            }
            index = cursor.getColumnIndex(ActivityContract.COL_NAME);

            if (index > -1) {
                result.setName(cursor.getString(index));
            }

        }
    }

    /**
     * Convert Cursor of database to Array of Activity entity.
     * @param cursor Cursor object
     * @return Array of Activity entity
     */
    public static ArrayList<Activity> cursorToItems(final android.database.Cursor cursor) {
        final ArrayList<Activity> result = new ArrayList<Activity>(cursor.getCount());

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            Activity item;
            do {
                item = ActivityContract.cursorToItem(cursor);
                result.add(item);
            } while (cursor.moveToNext());
        }

        return result;
    }
}
