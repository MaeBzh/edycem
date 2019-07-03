/*
 * UserContractBase.java, Edycem Android
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

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.Job;
import com.imie.edycem.entity.WorkingTime;



import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.harmony.util.DateUtils;

/** Edycem contract base.
 *
 * This class is regenerated. DO NOT MODIFY.
 */
public abstract class UserContractBase {


        /** id. */
    public static final String COL_ID =
            "id";
    /** Alias. */
    public static final String ALIASED_COL_ID =
            UserContract.TABLE_NAME + "." + COL_ID;

    /** idSmartphone. */
    public static final String COL_IDSMARTPHONE =
            "idSmartphone";
    /** Alias. */
    public static final String ALIASED_COL_IDSMARTPHONE =
            UserContract.TABLE_NAME + "." + COL_IDSMARTPHONE;

    /** password. */
    public static final String COL_PASSWORD =
            "password";
    /** Alias. */
    public static final String ALIASED_COL_PASSWORD =
            UserContract.TABLE_NAME + "." + COL_PASSWORD;

    /** dateRgpd. */
    public static final String COL_DATERGPD =
            "dateRgpd";
    /** Alias. */
    public static final String ALIASED_COL_DATERGPD =
            UserContract.TABLE_NAME + "." + COL_DATERGPD;

    /** job_id. */
    public static final String COL_JOB_ID =
            "job_id";
    /** Alias. */
    public static final String ALIASED_COL_JOB_ID =
            UserContract.TABLE_NAME + "." + COL_JOB_ID;




    /** Constant for parcelisation/serialization. */
    public static final String PARCEL = "User";
    /** Table name of SQLite database. */
    public static final String TABLE_NAME = "User";
    /** Global Fields. */
    public static final String[] COLS = new String[] {

        
        UserContract.COL_ID,
        
        UserContract.COL_IDSMARTPHONE,
        
        UserContract.COL_PASSWORD,
        
        UserContract.COL_DATERGPD,
        
        UserContract.COL_JOB_ID,
    };

    /** Global Fields. */
    public static final String[] ALIASED_COLS = new String[] {
        
        UserContract.ALIASED_COL_ID,
        
        UserContract.ALIASED_COL_IDSMARTPHONE,
        
        UserContract.ALIASED_COL_PASSWORD,
        
        UserContract.ALIASED_COL_DATERGPD,
        
        UserContract.ALIASED_COL_JOB_ID,
        
    };


    /**
     * Converts a User into a content values.
     *
     * @param item The User to convert
     *
     * @return The content values
     */
    public static ContentValues itemToContentValues(final User item) {
        final ContentValues result = new ContentValues();

             result.put(UserContract.COL_ID,
                String.valueOf(item.getId()));

             if (item.getIdSmartphone() != null) {
                result.put(UserContract.COL_IDSMARTPHONE,
                    item.getIdSmartphone());
            }

             if (item.getPassword() != null) {
                result.put(UserContract.COL_PASSWORD,
                    item.getPassword());
            }

             if (item.getDateRgpd() != null) {
                result.put(UserContract.COL_DATERGPD,
                    item.getDateRgpd().toString(ISODateTimeFormat.dateTime()));
            }

             if (item.getJob() != null) {
                result.put(UserContract.COL_JOB_ID,
                    item.getJob().getId());
            }

 
        return result;
    }

    /**
     * Converts a Cursor into a User.
     *
     * @param cursor The cursor to convert
     *
     * @return The extracted User
     */
    public static User cursorToItem(final android.database.Cursor cursor) {
        User result = new User();
        UserContract.cursorToItem(cursor, result);
        return result;
    }

    /**
     * Convert Cursor of database to User entity.
     * @param cursor Cursor object
     * @param result User entity
     */
    public static void cursorToItem(final android.database.Cursor cursor, final User result) {
        if (cursor.getCount() != 0) {
            int index;

            index = cursor.getColumnIndex(UserContract.COL_ID);

            if (index > -1) {
                result.setId(cursor.getInt(index));
            }
            index = cursor.getColumnIndex(UserContract.COL_IDSMARTPHONE);

            if (index > -1) {
                result.setIdSmartphone(cursor.getString(index));
            }
            index = cursor.getColumnIndex(UserContract.COL_PASSWORD);

            if (index > -1) {
                result.setPassword(cursor.getString(index));
            }
            index = cursor.getColumnIndex(UserContract.COL_DATERGPD);

            if (index > -1) {
                final DateTime dtDateRgpd =
                        DateUtils.formatISOStringToDateTime(cursor.getString(index));
                if (dtDateRgpd != null) {
                        result.setDateRgpd(dtDateRgpd);
                } else {
                    result.setDateRgpd(new DateTime());
                }
            }
            if (result.getJob() == null) {
                final Job job = new Job();
                index = cursor.getColumnIndex(UserContract.COL_JOB_ID);

                if (index > -1) {
                    job.setId(cursor.getInt(index));
                    result.setJob(job);
                }

            }

        }
    }

    /**
     * Convert Cursor of database to Array of User entity.
     * @param cursor Cursor object
     * @return Array of User entity
     */
    public static ArrayList<User> cursorToItems(final android.database.Cursor cursor) {
        final ArrayList<User> result = new ArrayList<User>(cursor.getCount());

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            User item;
            do {
                item = UserContract.cursorToItem(cursor);
                result.add(item);
            } while (cursor.moveToNext());
        }

        return result;
    }
}
