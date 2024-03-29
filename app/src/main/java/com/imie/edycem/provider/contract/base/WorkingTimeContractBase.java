/*
 * WorkingTimeContractBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 11, 2019
 *
 */
package com.imie.edycem.provider.contract.base;

import android.content.ContentValues;


import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.Task;



import com.imie.edycem.provider.contract.WorkingTimeContract;
import com.imie.edycem.harmony.util.DateUtils;

/** Edycem contract base.
 *
 * This class is regenerated. DO NOT MODIFY.
 */
public abstract class WorkingTimeContractBase {


        /** id. */
    public static final String COL_ID =
            "id";
    /** Alias. */
    public static final String ALIASED_COL_ID =
            WorkingTimeContract.TABLE_NAME + "." + COL_ID;

    /** date. */
    public static final String COL_DATE =
            "date";
    /** Alias. */
    public static final String ALIASED_COL_DATE =
            WorkingTimeContract.TABLE_NAME + "." + COL_DATE;

    /** spentTime. */
    public static final String COL_SPENTTIME =
            "spentTime";
    /** Alias. */
    public static final String ALIASED_COL_SPENTTIME =
            WorkingTimeContract.TABLE_NAME + "." + COL_SPENTTIME;

    /** description. */
    public static final String COL_DESCRIPTION =
            "description";
    /** Alias. */
    public static final String ALIASED_COL_DESCRIPTION =
            WorkingTimeContract.TABLE_NAME + "." + COL_DESCRIPTION;

    /** user_id. */
    public static final String COL_USER_ID =
            "user_id";
    /** Alias. */
    public static final String ALIASED_COL_USER_ID =
            WorkingTimeContract.TABLE_NAME + "." + COL_USER_ID;

    /** project_id. */
    public static final String COL_PROJECT_ID =
            "project_id";
    /** Alias. */
    public static final String ALIASED_COL_PROJECT_ID =
            WorkingTimeContract.TABLE_NAME + "." + COL_PROJECT_ID;

    /** task_id. */
    public static final String COL_TASK_ID =
            "task_id";
    /** Alias. */
    public static final String ALIASED_COL_TASK_ID =
            WorkingTimeContract.TABLE_NAME + "." + COL_TASK_ID;




    /** Constant for parcelisation/serialization. */
    public static final String PARCEL = "WorkingTime";
    /** Table name of SQLite database. */
    public static final String TABLE_NAME = "WorkingTime";
    /** Global Fields. */
    public static final String[] COLS = new String[] {

        
        WorkingTimeContract.COL_ID,
        
        WorkingTimeContract.COL_DATE,
        
        WorkingTimeContract.COL_SPENTTIME,
        
        WorkingTimeContract.COL_DESCRIPTION,
        
        WorkingTimeContract.COL_USER_ID,
        
        WorkingTimeContract.COL_PROJECT_ID,
        
        WorkingTimeContract.COL_TASK_ID
    };

    /** Global Fields. */
    public static final String[] ALIASED_COLS = new String[] {
        
        WorkingTimeContract.ALIASED_COL_ID,
        
        WorkingTimeContract.ALIASED_COL_DATE,
        
        WorkingTimeContract.ALIASED_COL_SPENTTIME,
        
        WorkingTimeContract.ALIASED_COL_DESCRIPTION,
        
        WorkingTimeContract.ALIASED_COL_USER_ID,
        
        WorkingTimeContract.ALIASED_COL_PROJECT_ID,
        
        WorkingTimeContract.ALIASED_COL_TASK_ID
    };


    /**
     * Converts a WorkingTime into a content values.
     *
     * @param item The WorkingTime to convert
     *
     * @return The content values
     */
    public static ContentValues itemToContentValues(final WorkingTime item) {
        final ContentValues result = new ContentValues();

             result.put(WorkingTimeContract.COL_ID,
                String.valueOf(item.getId()));

             if (item.getDate() != null) {
                result.put(WorkingTimeContract.COL_DATE,
                    item.getDate().toString(ISODateTimeFormat.dateTime()));
            }

             result.put(WorkingTimeContract.COL_SPENTTIME,
                String.valueOf(item.getSpentTime()));

             if (item.getDescription() != null) {
                result.put(WorkingTimeContract.COL_DESCRIPTION,
                    item.getDescription());
            }

             if (item.getUser() != null) {
                result.put(WorkingTimeContract.COL_USER_ID,
                    item.getUser().getId());
            }

             if (item.getProject() != null) {
                result.put(WorkingTimeContract.COL_PROJECT_ID,
                    item.getProject().getId());
            }

             if (item.getTask() != null) {
                result.put(WorkingTimeContract.COL_TASK_ID,
                    item.getTask().getId());
            }


        return result;
    }

    /**
     * Converts a Cursor into a WorkingTime.
     *
     * @param cursor The cursor to convert
     *
     * @return The extracted WorkingTime
     */
    public static WorkingTime cursorToItem(final android.database.Cursor cursor) {
        WorkingTime result = new WorkingTime();
        WorkingTimeContract.cursorToItem(cursor, result);
        return result;
    }

    /**
     * Convert Cursor of database to WorkingTime entity.
     * @param cursor Cursor object
     * @param result WorkingTime entity
     */
    public static void cursorToItem(final android.database.Cursor cursor, final WorkingTime result) {
        if (cursor.getCount() != 0) {
            int index;

            index = cursor.getColumnIndex(WorkingTimeContract.COL_ID);

            if (index > -1) {
                result.setId(cursor.getInt(index));
            }
            index = cursor.getColumnIndex(WorkingTimeContract.COL_DATE);

            if (index > -1) {
                final DateTime dtDate =
                        DateUtils.formatISOStringToDateTime(cursor.getString(index));
                if (dtDate != null) {
                        result.setDate(dtDate);
                } else {
                    result.setDate(new DateTime());
                }
            }
            index = cursor.getColumnIndex(WorkingTimeContract.COL_SPENTTIME);

            if (index > -1) {
                result.setSpentTime(cursor.getInt(index));
            }
            index = cursor.getColumnIndex(WorkingTimeContract.COL_DESCRIPTION);

            if (index > -1) {
                result.setDescription(cursor.getString(index));
            }
            if (result.getUser() == null) {
                final User user = new User();
                index = cursor.getColumnIndex(WorkingTimeContract.COL_USER_ID);

                if (index > -1) {
                    user.setId(cursor.getInt(index));
                    result.setUser(user);
                }

            }
            if (result.getProject() == null) {
                final Project project = new Project();
                index = cursor.getColumnIndex(WorkingTimeContract.COL_PROJECT_ID);

                if (index > -1) {
                    project.setId(cursor.getInt(index));
                    result.setProject(project);
                }

            }
            if (result.getTask() == null) {
                final Task task = new Task();
                index = cursor.getColumnIndex(WorkingTimeContract.COL_TASK_ID);

                if (index > -1) {
                    task.setId(cursor.getInt(index));
                    result.setTask(task);
                }

            }

        }
    }

    /**
     * Convert Cursor of database to Array of WorkingTime entity.
     * @param cursor Cursor object
     * @return Array of WorkingTime entity
     */
    public static ArrayList<WorkingTime> cursorToItems(final android.database.Cursor cursor) {
        final ArrayList<WorkingTime> result = new ArrayList<WorkingTime>(cursor.getCount());

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            WorkingTime item;
            do {
                item = WorkingTimeContract.cursorToItem(cursor);
                result.add(item);
            } while (cursor.moveToNext());
        }

        return result;
    }
}
