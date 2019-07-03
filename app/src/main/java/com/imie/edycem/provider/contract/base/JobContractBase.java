/*
 * JobContractBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.provider.contract.base;

import android.content.ContentValues;


import java.util.ArrayList;

import com.imie.edycem.entity.Job;



import com.imie.edycem.provider.contract.JobContract;

/** Edycem contract base.
 *
 * This class is regenerated. DO NOT MODIFY.
 */
public abstract class JobContractBase {


        /** id. */
    public static final String COL_ID =
            "id";
    /** Alias. */
    public static final String ALIASED_COL_ID =
            JobContract.TABLE_NAME + "." + COL_ID;

    /** name. */
    public static final String COL_NAME =
            "name";
    /** Alias. */
    public static final String ALIASED_COL_NAME =
            JobContract.TABLE_NAME + "." + COL_NAME;




    /** Constant for parcelisation/serialization. */
    public static final String PARCEL = "Job";
    /** Table name of SQLite database. */
    public static final String TABLE_NAME = "Job";
    /** Global Fields. */
    public static final String[] COLS = new String[] {

        
        JobContract.COL_ID,
        
        JobContract.COL_NAME
    };

    /** Global Fields. */
    public static final String[] ALIASED_COLS = new String[] {
        
        JobContract.ALIASED_COL_ID,
        
        JobContract.ALIASED_COL_NAME
    };


    /**
     * Converts a Job into a content values.
     *
     * @param item The Job to convert
     *
     * @return The content values
     */
    public static ContentValues itemToContentValues(final Job item) {
        final ContentValues result = new ContentValues();

             result.put(JobContract.COL_ID,
                String.valueOf(item.getId()));

             if (item.getName() != null) {
                result.put(JobContract.COL_NAME,
                    item.getName());
            }


        return result;
    }

    /**
     * Converts a Cursor into a Job.
     *
     * @param cursor The cursor to convert
     *
     * @return The extracted Job
     */
    public static Job cursorToItem(final android.database.Cursor cursor) {
        Job result = new Job();
        JobContract.cursorToItem(cursor, result);
        return result;
    }

    /**
     * Convert Cursor of database to Job entity.
     * @param cursor Cursor object
     * @param result Job entity
     */
    public static void cursorToItem(final android.database.Cursor cursor, final Job result) {
        if (cursor.getCount() != 0) {
            int index;

            index = cursor.getColumnIndex(JobContract.COL_ID);

            if (index > -1) {
                result.setId(cursor.getInt(index));
            }
            index = cursor.getColumnIndex(JobContract.COL_NAME);

            if (index > -1) {
                result.setName(cursor.getString(index));
            }

        }
    }

    /**
     * Convert Cursor of database to Array of Job entity.
     * @param cursor Cursor object
     * @return Array of Job entity
     */
    public static ArrayList<Job> cursorToItems(final android.database.Cursor cursor) {
        final ArrayList<Job> result = new ArrayList<Job>(cursor.getCount());

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            Job item;
            do {
                item = JobContract.cursorToItem(cursor);
                result.add(item);
            } while (cursor.moveToNext());
        }

        return result;
    }
}
