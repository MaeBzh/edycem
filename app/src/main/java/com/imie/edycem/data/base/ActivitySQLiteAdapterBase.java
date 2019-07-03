
/*
 * ActivitySQLiteAdapterBase.java, Edycem Android
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
import com.imie.edycem.data.ActivitySQLiteAdapter;
import com.imie.edycem.data.TaskSQLiteAdapter;
import com.imie.edycem.provider.contract.ActivityContract;
import com.imie.edycem.provider.contract.TaskContract;
import com.imie.edycem.entity.Activity;
import com.imie.edycem.entity.Task;


import com.imie.edycem.EdycemApplication;



/** Activity adapter database abstract class. <br/>
 * <b><i>This class will be overwrited whenever you regenerate the project<br/>
 * with Harmony.<br />
 * You should edit ActivityAdapter class instead of this<br/>
 * one or you will lose all your modifications.</i></b>
 */
public abstract class ActivitySQLiteAdapterBase
                        extends SQLiteAdapter<Activity> {

    /** TAG for debug purpose. */
    protected static final String TAG = "ActivityDBAdapter";


    /**
     * Get the table name used in DB for your Activity entity.
     * @return A String showing the table name
     */
    public String getTableName() {
        return ActivityContract.TABLE_NAME;
    }

    /**
     * Get the joined table name used in DB for your Activity entity
     * and its parents.
     * @return A String showing the joined table name
     */
    public String getJoinedTableName() {
        String result = ActivityContract.TABLE_NAME;
        return result;
    }

    /**
     * Get the column names from the Activity entity table.
     * @return An array of String representing the columns
     */
    public String[] getCols() {
        return ActivityContract.ALIASED_COLS;
    }

    /**
     * Generate Entity Table Schema.
     * @return "SQL query : CREATE TABLE..."
     */
    public static String getSchema() {
        return "CREATE TABLE "
        + ActivityContract.TABLE_NAME    + " ("
        
         + ActivityContract.COL_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
         + ActivityContract.COL_NAME    + " VARCHAR NOT NULL"

        
        + ");"
;
    }

    /**
     * Constructor.
     * @param ctx context
     */
    public ActivitySQLiteAdapterBase(final android.content.Context ctx) {
        super(ctx);
    }

    // Converters

    /**
     * Convert Activity entity to Content Values for database.
     * @param item Activity entity object
     * @return ContentValues object
     */
    public ContentValues itemToContentValues(final Activity item) {
        return ActivityContract.itemToContentValues(item);
    }

    /**
     * Convert android.database.Cursor of database to Activity entity.
     * @param cursor android.database.Cursor object
     * @return Activity entity
     */
    public Activity cursorToItem(final android.database.Cursor cursor) {
        return ActivityContract.cursorToItem(cursor);
    }

    /**
     * Convert android.database.Cursor of database to Activity entity.
     * @param cursor android.database.Cursor object
     * @param result Activity entity
     */
    public void cursorToItem(final android.database.Cursor cursor, final Activity result) {
        ActivityContract.cursorToItem(cursor, result);
    }

    //// CRUD Entity ////
    /**
     * Find & read Activity by id in database.
     *
     * @param id Identify of Activity
     * @return Activity entity
     */
    public Activity getByID(final int id) {
        final android.database.Cursor cursor = this.getSingleCursor(id);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
        }

        final Activity result = this.cursorToItem(cursor);
        cursor.close();

        final TaskSQLiteAdapter tasksAdapter =
                new TaskSQLiteAdapter(this.ctx);
        tasksAdapter.open(this.mDatabase);
        android.database.Cursor tasksCursor = tasksAdapter
                    .getByActivity(
                            result.getId(),
                            TaskContract.ALIASED_COLS,
                            null,
                            null,
                            null);
        result.setTasks(tasksAdapter.cursorToItems(tasksCursor));

        tasksCursor.close();
        return result;
    }


    /**
     * Read All Activitys entities.
     *
     * @return List of Activity entities
     */
    public ArrayList<Activity> getAll() {
        final android.database.Cursor cursor = this.getAllCursor();
        final ArrayList<Activity> result = this.cursorToItems(cursor);
        cursor.close();

        return result;
    }



    /**
     * Insert a Activity entity into database.
     *
     * @param item The Activity entity to persist
     * @return Id of the Activity entity
     */
    public long insert(final Activity item) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Insert DB(" + ActivityContract.TABLE_NAME + ")");
        }

        final ContentValues values =
                ActivityContract.itemToContentValues(item);
        values.remove(ActivityContract.COL_ID);
        int insertResult;
        if (values.size() != 0) {
            insertResult = (int) this.insert(
                    null,
                    values);
        } else {
            insertResult = (int) this.insert(
                    ActivityContract.COL_ID,
                    values);
        }
        item.setId(insertResult);
        if (item.getTasks() != null) {
            TaskSQLiteAdapterBase tasksAdapter =
                    new TaskSQLiteAdapter(this.ctx);
            tasksAdapter.open(this.mDatabase);
            for (Task task
                        : item.getTasks()) {
                task.setActivity(item);
                tasksAdapter.insertOrUpdate(task);
            }
        }
        return insertResult;
    }

    /**
     * Either insert or update a Activity entity into database whether.
     * it already exists or not.
     *
     * @param item The Activity entity to persist
     * @return 1 if everything went well, 0 otherwise
     */
    public int insertOrUpdate(final Activity item) {
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
     * Update a Activity entity into database.
     *
     * @param item The Activity entity to persist
     * @return count of updated entities
     */
    public int update(final Activity item) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Update DB(" + ActivityContract.TABLE_NAME + ")");
        }

        final ContentValues values =
                ActivityContract.itemToContentValues(item);
        final String whereClause =
                 ActivityContract.COL_ID
                 + " = ?";
        final String[] whereArgs =
                new String[] {String.valueOf(item.getId()) };

        return this.update(
                values,
                whereClause,
                whereArgs);
    }


    /**
     * Delete a Activity entity of database.
     *
     * @param id id
     * @return count of updated entities
     */
    public int remove(final int id) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(
                TAG,
                "Delete DB("
                    + ActivityContract.TABLE_NAME
                    + ")"
                    + " id : "+ id);
        }

        final String whereClause =
                ActivityContract.COL_ID
                + " = ?";
        final String[] whereArgs = new String[] {
                    String.valueOf(id)};

        return this.delete(
                whereClause,
                whereArgs);
    }

    /**
     * Deletes the given entity.
     * @param activity The entity to delete
     * @return count of updated entities
     */
    public int delete(final Activity activity) {
        return this.remove(activity.getId());
    }

    /**
     *  Internal android.database.Cursor.
     * @param id id
     *  @return A android.database.Cursor pointing to the Activity corresponding
     *        to the given id.
     */
    protected android.database.Cursor getSingleCursor(final int id) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Get entities id : " + id);
        }

        final String whereClause =
                ActivityContract.ALIASED_COL_ID
                + " = ?";
        final String[] whereArgs = new String[] {String.valueOf(id)};

        return this.query(
                ActivityContract.ALIASED_COLS,
                whereClause,
                whereArgs,
                null,
                null,
                null);
    }


    /**
     * Query the DB to find a Activity entity.
     *
     * @param id The id of the entity to get from the DB
     *
     * @return The cursor pointing to the query's result
     */
    public android.database.Cursor query(final int id) {

        String selection = ActivityContract.ALIASED_COL_ID + " = ?";
        

        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(id);

        return this.query(
                ActivityContract.ALIASED_COLS,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }




}

