
/*
 * UserSQLiteAdapterBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 5, 2019
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
import com.imie.edycem.data.UserSQLiteAdapter;
import com.imie.edycem.data.JobSQLiteAdapter;
import com.imie.edycem.data.WorkingTimeSQLiteAdapter;
import com.imie.edycem.data.ProjectSQLiteAdapter;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.provider.contract.JobContract;
import com.imie.edycem.provider.contract.WorkingTimeContract;
import com.imie.edycem.provider.contract.ProjectContract;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.Job;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.entity.Project;

import com.imie.edycem.harmony.util.DateUtils;
import com.imie.edycem.EdycemApplication;



/** User adapter database abstract class. <br/>
 * <b><i>This class will be overwrited whenever you regenerate the project<br/>
 * with Harmony.<br />
 * You should edit UserAdapter class instead of this<br/>
 * one or you will lose all your modifications.</i></b>
 */
public abstract class UserSQLiteAdapterBase
                        extends SQLiteAdapter<User> {

    /** TAG for debug purpose. */
    protected static final String TAG = "UserDBAdapter";


    /**
     * Get the table name used in DB for your User entity.
     * @return A String showing the table name
     */
    public String getTableName() {
        return UserContract.TABLE_NAME;
    }

    /**
     * Get the joined table name used in DB for your User entity
     * and its parents.
     * @return A String showing the joined table name
     */
    public String getJoinedTableName() {
        String result = UserContract.TABLE_NAME;
        return result;
    }

    /**
     * Get the column names from the User entity table.
     * @return An array of String representing the columns
     */
    public String[] getCols() {
        return UserContract.ALIASED_COLS;
    }

    /**
     * Generate Entity Table Schema.
     * @return "SQL query : CREATE TABLE..."
     */
    public static String getSchema() {
        return "CREATE TABLE "
        + UserContract.TABLE_NAME    + " ("
        
         + UserContract.COL_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
         + UserContract.COL_FIRSTNAME    + " VARCHAR NOT NULL,"
         + UserContract.COL_LASTNAME    + " VARCHAR NOT NULL,"
         + UserContract.COL_EMAIL    + " VARCHAR NOT NULL,"
         + UserContract.COL_ISELIGIBLE    + " BOOLEAN NOT NULL,"
         + UserContract.COL_IDSMARTPHONE    + " VARCHAR NOT NULL,"
         + UserContract.COL_DATERGPD    + " DATETIME,"
         + UserContract.COL_JOB_ID    + " INTEGER NOT NULL,"

        
         + "FOREIGN KEY(" + UserContract.COL_JOB_ID + ") REFERENCES " 
             + JobContract.TABLE_NAME 
                + " (" + JobContract.COL_ID + ")"
        + ", UNIQUE(" + UserContract.COL_EMAIL + ")"
        + ");"
;
    }

    /**
     * Constructor.
     * @param ctx context
     */
    public UserSQLiteAdapterBase(final android.content.Context ctx) {
        super(ctx);
    }

    // Converters

    /**
     * Convert User entity to Content Values for database.
     * @param item User entity object
     * @return ContentValues object
     */
    public ContentValues itemToContentValues(final User item) {
        return UserContract.itemToContentValues(item);
    }

    /**
     * Convert android.database.Cursor of database to User entity.
     * @param cursor android.database.Cursor object
     * @return User entity
     */
    public User cursorToItem(final android.database.Cursor cursor) {
        return UserContract.cursorToItem(cursor);
    }

    /**
     * Convert android.database.Cursor of database to User entity.
     * @param cursor android.database.Cursor object
     * @param result User entity
     */
    public void cursorToItem(final android.database.Cursor cursor, final User result) {
        UserContract.cursorToItem(cursor, result);
    }

    //// CRUD Entity ////
    /**
     * Find & read User by id in database.
     *
     * @param id Identify of User
     * @return User entity
     */
    public User getByID(final int id) {
        final android.database.Cursor cursor = this.getSingleCursor(id);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
        }

        final User result = this.cursorToItem(cursor);
        cursor.close();

        if (result.getJob() != null) {
            final JobSQLiteAdapter jobAdapter =
                    new JobSQLiteAdapter(this.ctx);
            jobAdapter.open(this.mDatabase);

            result.setJob(jobAdapter.getByID(
                            result.getJob().getId()));
        }
        final WorkingTimeSQLiteAdapter userWorkingTimesAdapter =
                new WorkingTimeSQLiteAdapter(this.ctx);
        userWorkingTimesAdapter.open(this.mDatabase);
        android.database.Cursor userworkingtimesCursor = userWorkingTimesAdapter
                    .getByUser(
                            result.getId(),
                            WorkingTimeContract.ALIASED_COLS,
                            null,
                            null,
                            null);
        result.setUserWorkingTimes(userWorkingTimesAdapter.cursorToItems(userworkingtimesCursor));

        userworkingtimesCursor.close();
        final ProjectSQLiteAdapter createdProjectsAdapter =
                new ProjectSQLiteAdapter(this.ctx);
        createdProjectsAdapter.open(this.mDatabase);
        android.database.Cursor createdprojectsCursor = createdProjectsAdapter
                    .getByCreator(
                            result.getId(),
                            ProjectContract.ALIASED_COLS,
                            null,
                            null,
                            null);
        result.setCreatedProjects(createdProjectsAdapter.cursorToItems(createdprojectsCursor));

        createdprojectsCursor.close();
        return result;
    }

    /**
     * Find & read User by job.
     * @param jobId jobId
     * @param orderBy Order by string (can be null)
     * @return List of User entities
     */
     public android.database.Cursor getByJob(final int jobId, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        String idSelection = UserContract.COL_JOB_ID + "= ?";
        String idSelectionArgs = String.valueOf(jobId);
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
     * Read All Users entities.
     *
     * @return List of User entities
     */
    public ArrayList<User> getAll() {
        final android.database.Cursor cursor = this.getAllCursor();
        final ArrayList<User> result = this.cursorToItems(cursor);
        cursor.close();

        return result;
    }



    /**
     * Insert a User entity into database.
     *
     * @param item The User entity to persist
     * @return Id of the User entity
     */
    public long insert(final User item) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Insert DB(" + UserContract.TABLE_NAME + ")");
        }

        final ContentValues values =
                UserContract.itemToContentValues(item);
        values.remove(UserContract.COL_ID);
        int insertResult;
        if (values.size() != 0) {
            insertResult = (int) this.insert(
                    null,
                    values);
        } else {
            insertResult = (int) this.insert(
                    UserContract.COL_ID,
                    values);
        }
        item.setId(insertResult);
        if (item.getUserWorkingTimes() != null) {
            WorkingTimeSQLiteAdapterBase userWorkingTimesAdapter =
                    new WorkingTimeSQLiteAdapter(this.ctx);
            userWorkingTimesAdapter.open(this.mDatabase);
            for (WorkingTime workingtime
                        : item.getUserWorkingTimes()) {
                workingtime.setUser(item);
                userWorkingTimesAdapter.insertOrUpdate(workingtime);
            }
        }
        if (item.getCreatedProjects() != null) {
            ProjectSQLiteAdapterBase createdProjectsAdapter =
                    new ProjectSQLiteAdapter(this.ctx);
            createdProjectsAdapter.open(this.mDatabase);
            for (Project project
                        : item.getCreatedProjects()) {
                project.setCreator(item);
                createdProjectsAdapter.insertOrUpdate(project);
            }
        }
        return insertResult;
    }

    /**
     * Either insert or update a User entity into database whether.
     * it already exists or not.
     *
     * @param item The User entity to persist
     * @return 1 if everything went well, 0 otherwise
     */
    public int insertOrUpdate(final User item) {
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
     * Update a User entity into database.
     *
     * @param item The User entity to persist
     * @return count of updated entities
     */
    public int update(final User item) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Update DB(" + UserContract.TABLE_NAME + ")");
        }

        final ContentValues values =
                UserContract.itemToContentValues(item);
        final String whereClause =
                 UserContract.COL_ID
                 + " = ?";
        final String[] whereArgs =
                new String[] {String.valueOf(item.getId()) };

        return this.update(
                values,
                whereClause,
                whereArgs);
    }


    /**
     * Delete a User entity of database.
     *
     * @param id id
     * @return count of updated entities
     */
    public int remove(final int id) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(
                TAG,
                "Delete DB("
                    + UserContract.TABLE_NAME
                    + ")"
                    + " id : "+ id);
        }

        final String whereClause =
                UserContract.COL_ID
                + " = ?";
        final String[] whereArgs = new String[] {
                    String.valueOf(id)};

        return this.delete(
                whereClause,
                whereArgs);
    }

    /**
     * Deletes the given entity.
     * @param user The entity to delete
     * @return count of updated entities
     */
    public int delete(final User user) {
        return this.remove(user.getId());
    }

    /**
     *  Internal android.database.Cursor.
     * @param id id
     *  @return A android.database.Cursor pointing to the User corresponding
     *        to the given id.
     */
    protected android.database.Cursor getSingleCursor(final int id) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Get entities id : " + id);
        }

        final String whereClause =
                UserContract.ALIASED_COL_ID
                + " = ?";
        final String[] whereArgs = new String[] {String.valueOf(id)};

        return this.query(
                UserContract.ALIASED_COLS,
                whereClause,
                whereArgs,
                null,
                null,
                null);
    }


    /**
     * Query the DB to find a User entity.
     *
     * @param id The id of the entity to get from the DB
     *
     * @return The cursor pointing to the query's result
     */
    public android.database.Cursor query(final int id) {

        String selection = UserContract.ALIASED_COL_ID + " = ?";
        

        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(id);

        return this.query(
                UserContract.ALIASED_COLS,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }




}

