/*
 * ResourceContractBase.java, Edycem Android
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

import com.imie.edycem.entity.base.EntityResourceBase;

import com.imie.edycem.provider.contract.ResourceContract;
import com.imie.edycem.harmony.util.DateUtils;

/** Edycem contract base.
 *
 * This class is regenerated. DO NOT MODIFY.
 */
public abstract class ResourceContractBase {

    /** id. */
    public static final String COL_ID =
            "id";
    /** Alias. */
    public static final String ALIASED_COL_ID =
            ResourceContract.TABLE_NAME + "." + COL_ID;


    /** path. */
    public static final String COL_PATH =
            "path";
    /** Alias. */
    public static final String ALIASED_COL_PATH =
            ResourceContract.TABLE_NAME + "." + COL_PATH;


    /** Discriminator column. */
    public static final String COL_DISCRIMINATORCOLUMN =
            "inheritance_type";
    /** Alias. */
    public static final String ALIASED_COL_DISCRIMINATORCOLUMN =
            ResourceContract.TABLE_NAME + "." + COL_DISCRIMINATORCOLUMN;


    /** Constant for parcelisation/serialization. */
    public static final String PARCEL = "Resource";
    /** Table name of SQLite database. */
    public static final String TABLE_NAME = "Resource";
    /** Global Fields. */
    public static final String[] COLS = new String[] {
            ResourceContract.COL_ID,
            ResourceContract.COL_PATH
    };

    /** Global Fields. */
    public static final String[] ALIASED_COLS = new String[] {
            ResourceContract.ALIASED_COL_ID,
            ResourceContract.ALIASED_COL_PATH
    };


    /**
     * Converts a Resource into a content values.
     *
     * @param item The Resource to convert
     *
     * @return The content values
     */
    public static ContentValues itemToContentValues(final EntityResourceBase item) {
        final ContentValues result = new ContentValues();

            result.put(ResourceContract.COL_ID,
                String.valueOf(item.getId()));

            if (item.getPath() != null) {
                result.put(ResourceContract.COL_PATH,
                    item.getPath());
            }

            

        return result;
    }

    /**
     * Converts a Cursor into a Resource.
     *
     * @param cursor The cursor to convert
     *
     * @return The extracted Resource
     */
    public static EntityResourceBase cursorToItem(final android.database.Cursor cursor) {
        EntityResourceBase result = new EntityResourceBase();
        ResourceContract.cursorToItem(cursor, result);
        return result;
    }

    /**
     * Convert Cursor of database to Resource entity.
     * @param cursor Cursor object
     * @param result Resource entity
     */
    public static void cursorToItem(final android.database.Cursor cursor, final EntityResourceBase result) {
        if (cursor.getCount() != 0) {
            int index;

            index = cursor.getColumnIndexOrThrow(ResourceContract.COL_ID);
            result.setId(
                    cursor.getInt(index));

            index = cursor.getColumnIndexOrThrow(ResourceContract.COL_PATH);
            result.setPath(
                    cursor.getString(index));

            

        }
    }

    /**
     * Convert Cursor of database to Array of Resource entity.
     * @param cursor Cursor object
     * @return Array of Resource entity
     */
    public static ArrayList<EntityResourceBase> cursorToItems(final android.database.Cursor cursor) {
        final ArrayList<EntityResourceBase> result = new ArrayList<EntityResourceBase>(cursor.getCount());

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            EntityResourceBase item;
            do {
                item = ResourceContract.cursorToItem(cursor);
                result.add(item);
            } while (cursor.moveToNext());
        }

        return result;
    }

    /**
     * Converts a Cursor into a Resource.
     *
     * @param cursor The cursor to convert
     *
     * @return The extracted Resource
     */
    public static EntityResourceBase cursorToItemLight(final android.database.Cursor cursor) {
        EntityResourceBase result = new EntityResourceBase();
        ResourceContract.cursorToItemLight(cursor, result);
        return result;
    }

    /**
     * Convert Cursor of database to Resource entity.
     * @param cursor Cursor object
     * @param result Resource entity
     */
    public static void cursorToItemLight(final android.database.Cursor cursor, final EntityResourceBase result) {
        if (cursor.getCount() != 0) {
            int index;

            index = cursor.getColumnIndex(ResourceContract.COL_PATH);
            result.setPath(cursor.getString(index));
        }
    }
}
