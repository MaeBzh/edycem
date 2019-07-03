
/*
 * SettingsSQLiteAdapterBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.data.base;

import java.util.ArrayList;
import android.content.ContentValues;


import android.database.sqlite.SQLiteDatabase;


import com.imie.edycem.data.SQLiteAdapter;
import com.imie.edycem.data.SettingsSQLiteAdapter;
import com.imie.edycem.provider.contract.SettingsContract;
import com.imie.edycem.entity.Settings;


import com.imie.edycem.EdycemApplication;



/** Settings adapter database abstract class. <br/>
 * <b><i>This class will be overwrited whenever you regenerate the project<br/>
 * with Harmony.<br />
 * You should edit SettingsAdapter class instead of this<br/>
 * one or you will lose all your modifications.</i></b>
 */
public abstract class SettingsSQLiteAdapterBase
                        extends SQLiteAdapter<Settings> {

    /** TAG for debug purpose. */
    protected static final String TAG = "SettingsDBAdapter";


    /**
     * Get the table name used in DB for your Settings entity.
     * @return A String showing the table name
     */
    public String getTableName() {
        return SettingsContract.TABLE_NAME;
    }

    /**
     * Get the joined table name used in DB for your Settings entity
     * and its parents.
     * @return A String showing the joined table name
     */
    public String getJoinedTableName() {
        String result = SettingsContract.TABLE_NAME;
        return result;
    }

    /**
     * Get the column names from the Settings entity table.
     * @return An array of String representing the columns
     */
    public String[] getCols() {
        return SettingsContract.ALIASED_COLS;
    }

    /**
     * Generate Entity Table Schema.
     * @return "SQL query : CREATE TABLE..."
     */
    public static String getSchema() {
        return "CREATE TABLE "
        + SettingsContract.TABLE_NAME    + " ("
        
         + SettingsContract.COL_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
         + SettingsContract.COL_RGPD    + " VARCHAR NOT NULL"

        
        + ");"
;
    }

    /**
     * Constructor.
     * @param ctx context
     */
    public SettingsSQLiteAdapterBase(final android.content.Context ctx) {
        super(ctx);
    }

    // Converters

    /**
     * Convert Settings entity to Content Values for database.
     * @param item Settings entity object
     * @return ContentValues object
     */
    public ContentValues itemToContentValues(final Settings item) {
        return SettingsContract.itemToContentValues(item);
    }

    /**
     * Convert android.database.Cursor of database to Settings entity.
     * @param cursor android.database.Cursor object
     * @return Settings entity
     */
    public Settings cursorToItem(final android.database.Cursor cursor) {
        return SettingsContract.cursorToItem(cursor);
    }

    /**
     * Convert android.database.Cursor of database to Settings entity.
     * @param cursor android.database.Cursor object
     * @param result Settings entity
     */
    public void cursorToItem(final android.database.Cursor cursor, final Settings result) {
        SettingsContract.cursorToItem(cursor, result);
    }

    //// CRUD Entity ////
    /**
     * Find & read Settings by id in database.
     *
     * @param id Identify of Settings
     * @return Settings entity
     */
    public Settings getByID(final int id) {
        final android.database.Cursor cursor = this.getSingleCursor(id);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
        }

        final Settings result = this.cursorToItem(cursor);
        cursor.close();

        return result;
    }


    /**
     * Read All Settingss entities.
     *
     * @return List of Settings entities
     */
    public ArrayList<Settings> getAll() {
        final android.database.Cursor cursor = this.getAllCursor();
        final ArrayList<Settings> result = this.cursorToItems(cursor);
        cursor.close();

        return result;
    }



    /**
     * Insert a Settings entity into database.
     *
     * @param item The Settings entity to persist
     * @return Id of the Settings entity
     */
    public long insert(final Settings item) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Insert DB(" + SettingsContract.TABLE_NAME + ")");
        }

        final ContentValues values =
                SettingsContract.itemToContentValues(item);
        values.remove(SettingsContract.COL_ID);
        int insertResult;
        if (values.size() != 0) {
            insertResult = (int) this.insert(
                    null,
                    values);
        } else {
            insertResult = (int) this.insert(
                    SettingsContract.COL_ID,
                    values);
        }
        item.setId(insertResult);
        return insertResult;
    }

    /**
     * Either insert or update a Settings entity into database whether.
     * it already exists or not.
     *
     * @param item The Settings entity to persist
     * @return 1 if everything went well, 0 otherwise
     */
    public int insertOrUpdate(final Settings item) {
        int result = 0;
        if (this.getByID(item.getId()) != null) {
            // Item already exists => update it
            result = this.update(item);
        } else {
            // Item doesn't exist => create it
            final long id = this.insert(item);
            if (id != 0) {
                result = 1;
            }
        }

        return result;
    }

    /**
     * Update a Settings entity into database.
     *
     * @param item The Settings entity to persist
     * @return count of updated entities
     */
    public int update(final Settings item) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Update DB(" + SettingsContract.TABLE_NAME + ")");
        }

        final ContentValues values =
                SettingsContract.itemToContentValues(item);
        final String whereClause =
                 SettingsContract.COL_ID
                 + " = ?";
        final String[] whereArgs =
                new String[] {String.valueOf(item.getId()) };

        return this.update(
                values,
                whereClause,
                whereArgs);
    }


    /**
     * Delete a Settings entity of database.
     *
     * @param id id
     * @return count of updated entities
     */
    public int remove(final int id) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(
                TAG,
                "Delete DB("
                    + SettingsContract.TABLE_NAME
                    + ")"
                    + " id : "+ id);
        }

        final String whereClause =
                SettingsContract.COL_ID
                + " = ?";
        final String[] whereArgs = new String[] {
                    String.valueOf(id)};

        return this.delete(
                whereClause,
                whereArgs);
    }

    /**
     * Deletes the given entity.
     * @param settings The entity to delete
     * @return count of updated entities
     */
    public int delete(final Settings settings) {
        return this.remove(settings.getId());
    }

    /**
     *  Internal android.database.Cursor.
     * @param id id
     *  @return A android.database.Cursor pointing to the Settings corresponding
     *        to the given id.
     */
    protected android.database.Cursor getSingleCursor(final int id) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Get entities id : " + id);
        }

        final String whereClause =
                SettingsContract.ALIASED_COL_ID
                + " = ?";
        final String[] whereArgs = new String[] {String.valueOf(id)};

        return this.query(
                SettingsContract.ALIASED_COLS,
                whereClause,
                whereArgs,
                null,
                null,
                null);
    }


    /**
     * Query the DB to find a Settings entity.
     *
     * @param id The id of the entity to get from the DB
     *
     * @return The cursor pointing to the query's result
     */
    public android.database.Cursor query(final int id) {

        String selection = SettingsContract.ALIASED_COL_ID + " = ?";
        

        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(id);

        return this.query(
                SettingsContract.ALIASED_COLS,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }




}

