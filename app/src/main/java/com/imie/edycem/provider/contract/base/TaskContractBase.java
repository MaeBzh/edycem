/*
 * TaskContractBase.java, Edycem Android
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

import com.imie.edycem.entity.Task;
import com.imie.edycem.entity.Activity;
import com.imie.edycem.entity.WorkingTime;



import com.imie.edycem.provider.contract.TaskContract;

/** Edycem contract base.
 *
 * This class is regenerated. DO NOT MODIFY.
 */
public abstract class TaskContractBase {


        /** id. */
    public static final String COL_ID =
            "id";
    /** Alias. */
    public static final String ALIASED_COL_ID =
            TaskContract.TABLE_NAME + "." + COL_ID;

    /** name. */
    public static final String COL_NAME =
            "name";
    /** Alias. */
    public static final String ALIASED_COL_NAME =
            TaskContract.TABLE_NAME + "." + COL_NAME;

    /** activity_id. */
    public static final String COL_ACTIVITY_ID =
            "activity_id";
    /** Alias. */
    public static final String ALIASED_COL_ACTIVITY_ID =
            TaskContract.TABLE_NAME + "." + COL_ACTIVITY_ID;




    /** Constant for parcelisation/serialization. */
    public static final String PARCEL = "Task";
    /** Table name of SQLite database. */
    public static final String TABLE_NAME = "Task";
    /** Global Fields. */
    public static final String[] COLS = new String[] {

        
        TaskContract.COL_ID,
        
        TaskContract.COL_NAME,
        
        TaskContract.COL_ACTIVITY_ID,
    };

    /** Global Fields. */
    public static final String[] ALIASED_COLS = new String[] {
        
        TaskContract.ALIASED_COL_ID,
        
        TaskContract.ALIASED_COL_NAME,
        
        TaskContract.ALIASED_COL_ACTIVITY_ID,
        
    };


    /**
     * Converts a Task into a content values.
     *
     * @param item The Task to convert
     *
     * @return The content values
     */
    public static ContentValues itemToContentValues(final Task item) {
        final ContentValues result = new ContentValues();

             result.put(TaskContract.COL_ID,
                String.valueOf(item.getId()));

             if (item.getName() != null) {
                result.put(TaskContract.COL_NAME,
                    item.getName());
            }

             if (item.getActivity() != null) {
                result.put(TaskContract.COL_ACTIVITY_ID,
                    item.getActivity().getId());
            }

 
        return result;
    }

    /**
     * Converts a Cursor into a Task.
     *
     * @param cursor The cursor to convert
     *
     * @return The extracted Task
     */
    public static Task cursorToItem(final android.database.Cursor cursor) {
        Task result = new Task();
        TaskContract.cursorToItem(cursor, result);
        return result;
    }

    /**
     * Convert Cursor of database to Task entity.
     * @param cursor Cursor object
     * @param result Task entity
     */
    public static void cursorToItem(final android.database.Cursor cursor, final Task result) {
        if (cursor.getCount() != 0) {
            int index;

            index = cursor.getColumnIndex(TaskContract.COL_ID);

            if (index > -1) {
                result.setId(cursor.getInt(index));
            }
            index = cursor.getColumnIndex(TaskContract.COL_NAME);

            if (index > -1) {
                result.setName(cursor.getString(index));
            }
            if (result.getActivity() == null) {
                final Activity activity = new Activity();
                index = cursor.getColumnIndex(TaskContract.COL_ACTIVITY_ID);

                if (index > -1) {
                    activity.setId(cursor.getInt(index));
                    result.setActivity(activity);
                }

            }

        }
    }

    /**
     * Convert Cursor of database to Array of Task entity.
     * @param cursor Cursor object
     * @return Array of Task entity
     */
    public static ArrayList<Task> cursorToItems(final android.database.Cursor cursor) {
        final ArrayList<Task> result = new ArrayList<Task>(cursor.getCount());

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            Task item;
            do {
                item = TaskContract.cursorToItem(cursor);
                result.add(item);
            } while (cursor.moveToNext());
        }

        return result;
    }
}
