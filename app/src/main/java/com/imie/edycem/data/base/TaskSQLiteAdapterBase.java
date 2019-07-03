
/*
 * TaskSQLiteAdapterBase.java, Edycem Android
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
import com.imie.edycem.data.TaskSQLiteAdapter;
import com.imie.edycem.provider.contract.TaskContract;
import com.imie.edycem.entity.Task;


import com.imie.edycem.EdycemApplication;



/** Task adapter database abstract class. <br/>
 * <b><i>This class will be overwrited whenever you regenerate the project<br/>
 * with Harmony.<br />
 * You should edit TaskAdapter class instead of this<br/>
 * one or you will lose all your modifications.</i></b>
 */
public abstract class TaskSQLiteAdapterBase
                        extends SQLiteAdapter<Task> {

    /** TAG for debug purpose. */
    protected static final String TAG = "TaskDBAdapter";


    /**
     * Get the table name used in DB for your Task entity.
     * @return A String showing the table name
     */
    public String getTableName() {
        return TaskContract.TABLE_NAME;
    }

    /**
     * Get the joined table name used in DB for your Task entity
     * and its parents.
     * @return A String showing the joined table name
     */
    public String getJoinedTableName() {
        String result = TaskContract.TABLE_NAME;
        return result;
    }

    /**
     * Get the column names from the Task entity table.
     * @return An array of String representing the columns
     */
    public String[] getCols() {
        return TaskContract.ALIASED_COLS;
    }

    /**
     * Generate Entity Table Schema.
     * @return "SQL query : CREATE TABLE..."
     */
    public static String getSchema() {
        return "CREATE TABLE "
        + TaskContract.TABLE_NAME    + " ("
        
         + TaskContract.COL_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
         + TaskContract.COL_NAME    + " VARCHAR NOT NULL"

        
        + ");"
;
    }

    /**
     * Constructor.
     * @param ctx context
     */
    public TaskSQLiteAdapterBase(final android.content.Context ctx) {
        super(ctx);
    }

    // Converters

    /**
     * Convert Task entity to Content Values for database.
     * @param item Task entity object
     * @return ContentValues object
     */
    public ContentValues itemToContentValues(final Task item) {
        return TaskContract.itemToContentValues(item);
    }

    /**
     * Convert android.database.Cursor of database to Task entity.
     * @param cursor android.database.Cursor object
     * @return Task entity
     */
    public Task cursorToItem(final android.database.Cursor cursor) {
        return TaskContract.cursorToItem(cursor);
    }

    /**
     * Convert android.database.Cursor of database to Task entity.
     * @param cursor android.database.Cursor object
     * @param result Task entity
     */
    public void cursorToItem(final android.database.Cursor cursor, final Task result) {
        TaskContract.cursorToItem(cursor, result);
    }

    //// CRUD Entity ////
    /**
     * Find & read Task by id in database.
     *
     * @param id Identify of Task
     * @return Task entity
     */
    public Task getByID(final int id) {
        final android.database.Cursor cursor = this.getSingleCursor(id);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
        }

        final Task result = this.cursorToItem(cursor);
        cursor.close();

        return result;
    }


    /**
     * Read All Tasks entities.
     *
     * @return List of Task entities
     */
    public ArrayList<Task> getAll() {
        final android.database.Cursor cursor = this.getAllCursor();
        final ArrayList<Task> result = this.cursorToItems(cursor);
        cursor.close();

        return result;
    }



    /**
     * Insert a Task entity into database.
     *
     * @param item The Task entity to persist
     * @return Id of the Task entity
     */
    public long insert(final Task item) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Insert DB(" + TaskContract.TABLE_NAME + ")");
        }

        final ContentValues values =
                TaskContract.itemToContentValues(item);
        values.remove(TaskContract.COL_ID);
        int insertResult;
        if (values.size() != 0) {
            insertResult = (int) this.insert(
                    null,
                    values);
        } else {
            insertResult = (int) this.insert(
                    TaskContract.COL_ID,
                    values);
        }
        item.setId(insertResult);
        return insertResult;
    }

    /**
     * Either insert or update a Task entity into database whether.
     * it already exists or not.
     *
     * @param item The Task entity to persist
     * @return 1 if everything went well, 0 otherwise
     */
    public int insertOrUpdate(final Task item) {
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
     * Update a Task entity into database.
     *
     * @param item The Task entity to persist
     * @return count of updated entities
     */
    public int update(final Task item) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Update DB(" + TaskContract.TABLE_NAME + ")");
        }

        final ContentValues values =
                TaskContract.itemToContentValues(item);
        final String whereClause =
                 TaskContract.COL_ID
                 + " = ?";
        final String[] whereArgs =
                new String[] {String.valueOf(item.getId()) };

        return this.update(
                values,
                whereClause,
                whereArgs);
    }


    /**
     * Delete a Task entity of database.
     *
     * @param id id
     * @return count of updated entities
     */
    public int remove(final int id) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(
                TAG,
                "Delete DB("
                    + TaskContract.TABLE_NAME
                    + ")"
                    + " id : "+ id);
        }

        final String whereClause =
                TaskContract.COL_ID
                + " = ?";
        final String[] whereArgs = new String[] {
                    String.valueOf(id)};

        return this.delete(
                whereClause,
                whereArgs);
    }

    /**
     * Deletes the given entity.
     * @param task The entity to delete
     * @return count of updated entities
     */
    public int delete(final Task task) {
        return this.remove(task.getId());
    }

    /**
     *  Internal android.database.Cursor.
     * @param id id
     *  @return A android.database.Cursor pointing to the Task corresponding
     *        to the given id.
     */
    protected android.database.Cursor getSingleCursor(final int id) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Get entities id : " + id);
        }

        final String whereClause =
                TaskContract.ALIASED_COL_ID
                + " = ?";
        final String[] whereArgs = new String[] {String.valueOf(id)};

        return this.query(
                TaskContract.ALIASED_COLS,
                whereClause,
                whereArgs,
                null,
                null,
                null);
    }


    /**
     * Query the DB to find a Task entity.
     *
     * @param id The id of the entity to get from the DB
     *
     * @return The cursor pointing to the query's result
     */
    public android.database.Cursor query(final int id) {

        String selection = TaskContract.ALIASED_COL_ID + " = ?";
        

        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(id);

        return this.query(
                TaskContract.ALIASED_COLS,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }




}

