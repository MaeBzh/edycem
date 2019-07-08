/*
 * SettingsContractBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 8, 2019
 *
 */
package com.imie.edycem.provider.contract.base;

import android.content.ContentValues;


import java.util.ArrayList;

import com.imie.edycem.entity.Settings;



import com.imie.edycem.provider.contract.SettingsContract;

/** Edycem contract base.
 *
 * This class is regenerated. DO NOT MODIFY.
 */
public abstract class SettingsContractBase {


        /** id. */
    public static final String COL_ID =
            "id";
    /** Alias. */
    public static final String ALIASED_COL_ID =
            SettingsContract.TABLE_NAME + "." + COL_ID;

    /** idServer. */
    public static final String COL_IDSERVER =
            "idServer";
    /** Alias. */
    public static final String ALIASED_COL_IDSERVER =
            SettingsContract.TABLE_NAME + "." + COL_IDSERVER;

    /** rgpd. */
    public static final String COL_RGPD =
            "rgpd";
    /** Alias. */
    public static final String ALIASED_COL_RGPD =
            SettingsContract.TABLE_NAME + "." + COL_RGPD;




    /** Constant for parcelisation/serialization. */
    public static final String PARCEL = "Settings";
    /** Table name of SQLite database. */
    public static final String TABLE_NAME = "Settings";
    /** Global Fields. */
    public static final String[] COLS = new String[] {

        
        SettingsContract.COL_ID,
        
        SettingsContract.COL_IDSERVER,
        
        SettingsContract.COL_RGPD
    };

    /** Global Fields. */
    public static final String[] ALIASED_COLS = new String[] {
        
        SettingsContract.ALIASED_COL_ID,
        
        SettingsContract.ALIASED_COL_IDSERVER,
        
        SettingsContract.ALIASED_COL_RGPD
    };


    /**
     * Converts a Settings into a content values.
     *
     * @param item The Settings to convert
     *
     * @return The content values
     */
    public static ContentValues itemToContentValues(final Settings item) {
        final ContentValues result = new ContentValues();

             result.put(SettingsContract.COL_ID,
                String.valueOf(item.getId()));

             result.put(SettingsContract.COL_IDSERVER,
                String.valueOf(item.getIdServer()));

             if (item.getRgpd() != null) {
                result.put(SettingsContract.COL_RGPD,
                    item.getRgpd());
            }


        return result;
    }

    /**
     * Converts a Cursor into a Settings.
     *
     * @param cursor The cursor to convert
     *
     * @return The extracted Settings
     */
    public static Settings cursorToItem(final android.database.Cursor cursor) {
        Settings result = new Settings();
        SettingsContract.cursorToItem(cursor, result);
        return result;
    }

    /**
     * Convert Cursor of database to Settings entity.
     * @param cursor Cursor object
     * @param result Settings entity
     */
    public static void cursorToItem(final android.database.Cursor cursor, final Settings result) {
        if (cursor.getCount() != 0) {
            int index;

            index = cursor.getColumnIndex(SettingsContract.COL_ID);

            if (index > -1) {
                result.setId(cursor.getInt(index));
            }
            index = cursor.getColumnIndex(SettingsContract.COL_IDSERVER);

            if (index > -1) {
            if (!cursor.isNull(index)) {
                    result.setIdServer(cursor.getInt(index));
            }
            }
            index = cursor.getColumnIndex(SettingsContract.COL_RGPD);

            if (index > -1) {
                result.setRgpd(cursor.getString(index));
            }

        }
    }

    /**
     * Convert Cursor of database to Array of Settings entity.
     * @param cursor Cursor object
     * @return Array of Settings entity
     */
    public static ArrayList<Settings> cursorToItems(final android.database.Cursor cursor) {
        final ArrayList<Settings> result = new ArrayList<Settings>(cursor.getCount());

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            Settings item;
            do {
                item = SettingsContract.cursorToItem(cursor);
                result.add(item);
            } while (cursor.moveToNext());
        }

        return result;
    }
}
