
/*
 * JobSQLiteAdapterBase.java, Edycem Android
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
import android.content.ContentValues;


import android.database.sqlite.SQLiteDatabase;


import com.imie.edycem.data.SQLiteAdapter;
import com.imie.edycem.data.JobSQLiteAdapter;
import com.imie.edycem.data.UserSQLiteAdapter;
import com.imie.edycem.data.ProjectSQLiteAdapter;
import com.imie.edycem.provider.contract.JobContract;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.provider.contract.ProjectContract;
import com.imie.edycem.entity.Job;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.Project;


import com.imie.edycem.EdycemApplication;



/** Job adapter database abstract class. <br/>
 * <b><i>This class will be overwrited whenever you regenerate the project<br/>
 * with Harmony.<br />
 * You should edit JobAdapter class instead of this<br/>
 * one or you will lose all your modifications.</i></b>
 */
public abstract class JobSQLiteAdapterBase
                        extends SQLiteAdapter<Job> {

    /** TAG for debug purpose. */
    protected static final String TAG = "JobDBAdapter";


    /**
     * Get the table name used in DB for your Job entity.
     * @return A String showing the table name
     */
    public String getTableName() {
        return JobContract.TABLE_NAME;
    }

    /**
     * Get the joined table name used in DB for your Job entity
     * and its parents.
     * @return A String showing the joined table name
     */
    public String getJoinedTableName() {
        String result = JobContract.TABLE_NAME;
        return result;
    }

    /**
     * Get the column names from the Job entity table.
     * @return An array of String representing the columns
     */
    public String[] getCols() {
        return JobContract.ALIASED_COLS;
    }

    /**
     * Generate Entity Table Schema.
     * @return "SQL query : CREATE TABLE..."
     */
    public static String getSchema() {
        return "CREATE TABLE "
        + JobContract.TABLE_NAME    + " ("
        
         + JobContract.COL_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
         + JobContract.COL_IDSERVER    + " INTEGER,"
         + JobContract.COL_NAME    + " VARCHAR NOT NULL"

        
        + ");"
;
    }

    /**
     * Constructor.
     * @param ctx context
     */
    public JobSQLiteAdapterBase(final android.content.Context ctx) {
        super(ctx);
    }

    // Converters

    /**
     * Convert Job entity to Content Values for database.
     * @param item Job entity object
     * @return ContentValues object
     */
    public ContentValues itemToContentValues(final Job item) {
        return JobContract.itemToContentValues(item);
    }

    /**
     * Convert android.database.Cursor of database to Job entity.
     * @param cursor android.database.Cursor object
     * @return Job entity
     */
    public Job cursorToItem(final android.database.Cursor cursor) {
        return JobContract.cursorToItem(cursor);
    }

    /**
     * Convert android.database.Cursor of database to Job entity.
     * @param cursor android.database.Cursor object
     * @param result Job entity
     */
    public void cursorToItem(final android.database.Cursor cursor, final Job result) {
        JobContract.cursorToItem(cursor, result);
    }

    //// CRUD Entity ////
    /**
     * Find & read Job by id in database.
     *
     * @param id Identify of Job
     * @return Job entity
     */
    public Job getByID(final int id) {
        final android.database.Cursor cursor = this.getSingleCursor(id);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
        }

        final Job result = this.cursorToItem(cursor);
        cursor.close();

        final UserSQLiteAdapter usersAdapter =
                new UserSQLiteAdapter(this.ctx);
        usersAdapter.open(this.mDatabase);
        android.database.Cursor usersCursor = usersAdapter
                    .getByJob(
                            result.getId(),
                            UserContract.ALIASED_COLS,
                            null,
                            null,
                            null);
        result.setUsers(usersAdapter.cursorToItems(usersCursor));

        usersCursor.close();
        final ProjectSQLiteAdapter projectsAdapter =
                new ProjectSQLiteAdapter(this.ctx);
        projectsAdapter.open(this.mDatabase);
        android.database.Cursor projectsCursor = projectsAdapter
                    .getByJob(
                            result.getId(),
                            ProjectContract.ALIASED_COLS,
                            null,
                            null,
                            null);
        result.setProjects(projectsAdapter.cursorToItems(projectsCursor));

        projectsCursor.close();
        return result;
    }


    /**
     * Read All Jobs entities.
     *
     * @return List of Job entities
     */
    public ArrayList<Job> getAll() {
        final android.database.Cursor cursor = this.getAllCursor();
        final ArrayList<Job> result = this.cursorToItems(cursor);
        cursor.close();

        return result;
    }



    /**
     * Insert a Job entity into database.
     *
     * @param item The Job entity to persist
     * @return Id of the Job entity
     */
    public long insert(final Job item) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Insert DB(" + JobContract.TABLE_NAME + ")");
        }

        final ContentValues values =
                JobContract.itemToContentValues(item);
        values.remove(JobContract.COL_ID);
        int insertResult;
        if (values.size() != 0) {
            insertResult = (int) this.insert(
                    null,
                    values);
        } else {
            insertResult = (int) this.insert(
                    JobContract.COL_ID,
                    values);
        }
        item.setId(insertResult);
        if (item.getUsers() != null) {
            UserSQLiteAdapterBase usersAdapter =
                    new UserSQLiteAdapter(this.ctx);
            usersAdapter.open(this.mDatabase);
            for (User user
                        : item.getUsers()) {
                user.setJob(item);
                usersAdapter.insertOrUpdate(user);
            }
        }
        if (item.getProjects() != null) {
            ProjectSQLiteAdapterBase projectsAdapter =
                    new ProjectSQLiteAdapter(this.ctx);
            projectsAdapter.open(this.mDatabase);
            for (Project project
                        : item.getProjects()) {
                project.setJob(item);
                projectsAdapter.insertOrUpdate(project);
            }
        }
        return insertResult;
    }

    /**
     * Either insert or update a Job entity into database whether.
     * it already exists or not.
     *
     * @param item The Job entity to persist
     * @return 1 if everything went well, 0 otherwise
     */
    public int insertOrUpdate(final Job item) {
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
     * Update a Job entity into database.
     *
     * @param item The Job entity to persist
     * @return count of updated entities
     */
    public int update(final Job item) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Update DB(" + JobContract.TABLE_NAME + ")");
        }

        final ContentValues values =
                JobContract.itemToContentValues(item);
        final String whereClause =
                 JobContract.COL_ID
                 + " = ?";
        final String[] whereArgs =
                new String[] {String.valueOf(item.getId()) };

        return this.update(
                values,
                whereClause,
                whereArgs);
    }


    /**
     * Delete a Job entity of database.
     *
     * @param id id
     * @return count of updated entities
     */
    public int remove(final int id) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(
                TAG,
                "Delete DB("
                    + JobContract.TABLE_NAME
                    + ")"
                    + " id : "+ id);
        }

        final String whereClause =
                JobContract.COL_ID
                + " = ?";
        final String[] whereArgs = new String[] {
                    String.valueOf(id)};

        return this.delete(
                whereClause,
                whereArgs);
    }

    /**
     * Deletes the given entity.
     * @param job The entity to delete
     * @return count of updated entities
     */
    public int delete(final Job job) {
        return this.remove(job.getId());
    }

    /**
     *  Internal android.database.Cursor.
     * @param id id
     *  @return A android.database.Cursor pointing to the Job corresponding
     *        to the given id.
     */
    protected android.database.Cursor getSingleCursor(final int id) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Get entities id : " + id);
        }

        final String whereClause =
                JobContract.ALIASED_COL_ID
                + " = ?";
        final String[] whereArgs = new String[] {String.valueOf(id)};

        return this.query(
                JobContract.ALIASED_COLS,
                whereClause,
                whereArgs,
                null,
                null,
                null);
    }


    /**
     * Query the DB to find a Job entity.
     *
     * @param id The id of the entity to get from the DB
     *
     * @return The cursor pointing to the query's result
     */
    public android.database.Cursor query(final int id) {

        String selection = JobContract.ALIASED_COL_ID + " = ?";
        

        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(id);

        return this.query(
                JobContract.ALIASED_COLS,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }




}

