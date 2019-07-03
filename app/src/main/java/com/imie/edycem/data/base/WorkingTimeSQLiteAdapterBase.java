
/*
 * WorkingTimeSQLiteAdapterBase.java, Edycem Android
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
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import android.content.ContentValues;


import android.database.sqlite.SQLiteDatabase;


import com.google.common.base.Strings;
import com.google.common.collect.ObjectArrays;
import com.imie.edycem.data.SQLiteAdapter;
import com.imie.edycem.data.WorkingTimeSQLiteAdapter;
import com.imie.edycem.data.UserSQLiteAdapter;
import com.imie.edycem.data.ProjectSQLiteAdapter;
import com.imie.edycem.data.TaskSQLiteAdapter;
import com.imie.edycem.provider.contract.WorkingTimeContract;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.provider.contract.ProjectContract;
import com.imie.edycem.provider.contract.TaskContract;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.Task;

import com.imie.edycem.harmony.util.DateUtils;
import com.imie.edycem.EdycemApplication;



/** WorkingTime adapter database abstract class. <br/>
 * <b><i>This class will be overwrited whenever you regenerate the project<br/>
 * with Harmony.<br />
 * You should edit WorkingTimeAdapter class instead of this<br/>
 * one or you will lose all your modifications.</i></b>
 */
public abstract class WorkingTimeSQLiteAdapterBase
                        extends SQLiteAdapter<WorkingTime> {

    /** TAG for debug purpose. */
    protected static final String TAG = "WorkingTimeDBAdapter";


    /**
     * Get the table name used in DB for your WorkingTime entity.
     * @return A String showing the table name
     */
    public String getTableName() {
        return WorkingTimeContract.TABLE_NAME;
    }

    /**
     * Get the joined table name used in DB for your WorkingTime entity
     * and its parents.
     * @return A String showing the joined table name
     */
    public String getJoinedTableName() {
        String result = WorkingTimeContract.TABLE_NAME;
        return result;
    }

    /**
     * Get the column names from the WorkingTime entity table.
     * @return An array of String representing the columns
     */
    public String[] getCols() {
        return WorkingTimeContract.ALIASED_COLS;
    }

    /**
     * Generate Entity Table Schema.
     * @return "SQL query : CREATE TABLE..."
     */
    public static String getSchema() {
        return "CREATE TABLE "
        + WorkingTimeContract.TABLE_NAME    + " ("
        
         + WorkingTimeContract.COL_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
         + WorkingTimeContract.COL_DATE    + " DATE NOT NULL,"
         + WorkingTimeContract.COL_SPENTTIME    + " VARCHAR NOT NULL,"
         + WorkingTimeContract.COL_DESCRIPTION    + " VARCHAR NOT NULL,"
         + WorkingTimeContract.COL_USER_ID    + " INTEGER NOT NULL,"
         + WorkingTimeContract.COL_PROJECT_ID    + " INTEGER NOT NULL,"
         + WorkingTimeContract.COL_TASK_ID    + " INTEGER NOT NULL,"

        
         + "FOREIGN KEY(" + WorkingTimeContract.COL_USER_ID + ") REFERENCES " 
             + UserContract.TABLE_NAME 
                + " (" + UserContract.COL_ID + "),"
         + "FOREIGN KEY(" + WorkingTimeContract.COL_PROJECT_ID + ") REFERENCES " 
             + ProjectContract.TABLE_NAME 
                + " (" + ProjectContract.COL_ID + "),"
         + "FOREIGN KEY(" + WorkingTimeContract.COL_TASK_ID + ") REFERENCES " 
             + TaskContract.TABLE_NAME 
                + " (" + TaskContract.COL_ID + ")"
        + ");"
;
    }

    /**
     * Constructor.
     * @param ctx context
     */
    public WorkingTimeSQLiteAdapterBase(final android.content.Context ctx) {
        super(ctx);
    }

    // Converters

    /**
     * Convert WorkingTime entity to Content Values for database.
     * @param item WorkingTime entity object
     * @return ContentValues object
     */
    public ContentValues itemToContentValues(final WorkingTime item) {
        return WorkingTimeContract.itemToContentValues(item);
    }

    /**
     * Convert android.database.Cursor of database to WorkingTime entity.
     * @param cursor android.database.Cursor object
     * @return WorkingTime entity
     */
    public WorkingTime cursorToItem(final android.database.Cursor cursor) {
        return WorkingTimeContract.cursorToItem(cursor);
    }

    /**
     * Convert android.database.Cursor of database to WorkingTime entity.
     * @param cursor android.database.Cursor object
     * @param result WorkingTime entity
     */
    public void cursorToItem(final android.database.Cursor cursor, final WorkingTime result) {
        WorkingTimeContract.cursorToItem(cursor, result);
    }

    //// CRUD Entity ////
    /**
     * Find & read WorkingTime by id in database.
     *
     * @param id Identify of WorkingTime
     * @return WorkingTime entity
     */
    public WorkingTime getByID(final int id) {
        final android.database.Cursor cursor = this.getSingleCursor(id);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
        }

        final WorkingTime result = this.cursorToItem(cursor);
        cursor.close();

        if (result.getUser() != null) {
            final UserSQLiteAdapter userAdapter =
                    new UserSQLiteAdapter(this.ctx);
            userAdapter.open(this.mDatabase);

            result.setUser(userAdapter.getByID(
                            result.getUser().getId()));
        }
        if (result.getProject() != null) {
            final ProjectSQLiteAdapter projectAdapter =
                    new ProjectSQLiteAdapter(this.ctx);
            projectAdapter.open(this.mDatabase);

            result.setProject(projectAdapter.getByID(
                            result.getProject().getId()));
        }
        if (result.getTask() != null) {
            final TaskSQLiteAdapter taskAdapter =
                    new TaskSQLiteAdapter(this.ctx);
            taskAdapter.open(this.mDatabase);

            result.setTask(taskAdapter.getByID(
                            result.getTask().getId()));
        }
        return result;
    }

    /**
     * Find & read WorkingTime by user.
     * @param userId userId
     * @param orderBy Order by string (can be null)
     * @return List of WorkingTime entities
     */
     public android.database.Cursor getByUser(final int userId, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        String idSelection = WorkingTimeContract.COL_USER_ID + "= ?";
        String idSelectionArgs = String.valueOf(userId);
        if (!Strings.isNullOrEmpty(selection)) {
            selection += " AND " + idSelection;
            selectionArgs = ObjectArrays.concat(selectionArgs, idSelectionArgs);
        } else {
            selection = idSelection;
            selectionArgs = new String[]{idSelectionArgs};
        }
        final android.database.Cursor cursor = this.query(
                projection,
                selection,
                selectionArgs,
                null,
                null,
                orderBy);

        return cursor;
     }
    /**
     * Find & read WorkingTime by project.
     * @param projectId projectId
     * @param orderBy Order by string (can be null)
     * @return List of WorkingTime entities
     */
     public android.database.Cursor getByProject(final int projectId, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        String idSelection = WorkingTimeContract.COL_PROJECT_ID + "= ?";
        String idSelectionArgs = String.valueOf(projectId);
        if (!Strings.isNullOrEmpty(selection)) {
            selection += " AND " + idSelection;
            selectionArgs = ObjectArrays.concat(selectionArgs, idSelectionArgs);
        } else {
            selection = idSelection;
            selectionArgs = new String[]{idSelectionArgs};
        }
        final android.database.Cursor cursor = this.query(
                projection,
                selection,
                selectionArgs,
                null,
                null,
                orderBy);

        return cursor;
     }
    /**
     * Find & read WorkingTime by task.
     * @param taskId taskId
     * @param orderBy Order by string (can be null)
     * @return List of WorkingTime entities
     */
     public android.database.Cursor getByTask(final int taskId, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        String idSelection = WorkingTimeContract.COL_TASK_ID + "= ?";
        String idSelectionArgs = String.valueOf(taskId);
        if (!Strings.isNullOrEmpty(selection)) {
            selection += " AND " + idSelection;
            selectionArgs = ObjectArrays.concat(selectionArgs, idSelectionArgs);
        } else {
            selection = idSelection;
            selectionArgs = new String[]{idSelectionArgs};
        }
        final android.database.Cursor cursor = this.query(
                projection,
                selection,
                selectionArgs,
                null,
                null,
                orderBy);

        return cursor;
     }

    /**
     * Read All WorkingTimes entities.
     *
     * @return List of WorkingTime entities
     */
    public ArrayList<WorkingTime> getAll() {
        final android.database.Cursor cursor = this.getAllCursor();
        final ArrayList<WorkingTime> result = this.cursorToItems(cursor);
        cursor.close();

        return result;
    }



    /**
     * Insert a WorkingTime entity into database.
     *
     * @param item The WorkingTime entity to persist
     * @return Id of the WorkingTime entity
     */
    public long insert(final WorkingTime item) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Insert DB(" + WorkingTimeContract.TABLE_NAME + ")");
        }

        final ContentValues values =
                WorkingTimeContract.itemToContentValues(item);
        values.remove(WorkingTimeContract.COL_ID);
        int insertResult;
        if (values.size() != 0) {
            insertResult = (int) this.insert(
                    null,
                    values);
        } else {
            insertResult = (int) this.insert(
                    WorkingTimeContract.COL_ID,
                    values);
        }
        item.setId(insertResult);
        return insertResult;
    }

    /**
     * Either insert or update a WorkingTime entity into database whether.
     * it already exists or not.
     *
     * @param item The WorkingTime entity to persist
     * @return 1 if everything went well, 0 otherwise
     */
    public int insertOrUpdate(final WorkingTime item) {
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
     * Update a WorkingTime entity into database.
     *
     * @param item The WorkingTime entity to persist
     * @return count of updated entities
     */
    public int update(final WorkingTime item) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Update DB(" + WorkingTimeContract.TABLE_NAME + ")");
        }

        final ContentValues values =
                WorkingTimeContract.itemToContentValues(item);
        final String whereClause =
                 WorkingTimeContract.COL_ID
                 + " = ?";
        final String[] whereArgs =
                new String[] {String.valueOf(item.getId()) };

        return this.update(
                values,
                whereClause,
                whereArgs);
    }


    /**
     * Delete a WorkingTime entity of database.
     *
     * @param id id
     * @return count of updated entities
     */
    public int remove(final int id) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(
                TAG,
                "Delete DB("
                    + WorkingTimeContract.TABLE_NAME
                    + ")"
                    + " id : "+ id);
        }

        final String whereClause =
                WorkingTimeContract.COL_ID
                + " = ?";
        final String[] whereArgs = new String[] {
                    String.valueOf(id)};

        return this.delete(
                whereClause,
                whereArgs);
    }

    /**
     * Deletes the given entity.
     * @param workingTime The entity to delete
     * @return count of updated entities
     */
    public int delete(final WorkingTime workingTime) {
        return this.remove(workingTime.getId());
    }

    /**
     *  Internal android.database.Cursor.
     * @param id id
     *  @return A android.database.Cursor pointing to the WorkingTime corresponding
     *        to the given id.
     */
    protected android.database.Cursor getSingleCursor(final int id) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Get entities id : " + id);
        }

        final String whereClause =
                WorkingTimeContract.ALIASED_COL_ID
                + " = ?";
        final String[] whereArgs = new String[] {String.valueOf(id)};

        return this.query(
                WorkingTimeContract.ALIASED_COLS,
                whereClause,
                whereArgs,
                null,
                null,
                null);
    }


    /**
     * Query the DB to find a WorkingTime entity.
     *
     * @param id The id of the entity to get from the DB
     *
     * @return The cursor pointing to the query's result
     */
    public android.database.Cursor query(final int id) {

        String selection = WorkingTimeContract.ALIASED_COL_ID + " = ?";
        

        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(id);

        return this.query(
                WorkingTimeContract.ALIASED_COLS,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }




}

