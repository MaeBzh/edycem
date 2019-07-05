
/*
 * ProjectSQLiteAdapterBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 4, 2019
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
import com.imie.edycem.data.ProjectSQLiteAdapter;
import com.imie.edycem.data.WorkingTimeSQLiteAdapter;
import com.imie.edycem.data.JobSQLiteAdapter;
import com.imie.edycem.provider.contract.ProjectContract;
import com.imie.edycem.provider.contract.WorkingTimeContract;
import com.imie.edycem.provider.contract.JobContract;
import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.entity.Job;

import com.imie.edycem.harmony.util.DateUtils;
import com.imie.edycem.EdycemApplication;



/** Project adapter database abstract class. <br/>
 * <b><i>This class will be overwrited whenever you regenerate the project<br/>
 * with Harmony.<br />
 * You should edit ProjectAdapter class instead of this<br/>
 * one or you will lose all your modifications.</i></b>
 */
public abstract class ProjectSQLiteAdapterBase
                        extends SQLiteAdapter<Project> {

    /** TAG for debug purpose. */
    protected static final String TAG = "ProjectDBAdapter";


    /**
     * Get the table name used in DB for your Project entity.
     * @return A String showing the table name
     */
    public String getTableName() {
        return ProjectContract.TABLE_NAME;
    }

    /**
     * Get the joined table name used in DB for your Project entity
     * and its parents.
     * @return A String showing the joined table name
     */
    public String getJoinedTableName() {
        String result = ProjectContract.TABLE_NAME;
        return result;
    }

    /**
     * Get the column names from the Project entity table.
     * @return An array of String representing the columns
     */
    public String[] getCols() {
        return ProjectContract.ALIASED_COLS;
    }

    /**
     * Generate Entity Table Schema.
     * @return "SQL query : CREATE TABLE..."
     */
    public static String getSchema() {
        return "CREATE TABLE "
        + ProjectContract.TABLE_NAME    + " ("
        
         + ProjectContract.COL_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
         + ProjectContract.COL_NAME    + " VARCHAR NOT NULL,"
         + ProjectContract.COL_DESCRIPTION    + " VARCHAR NOT NULL,"
         + ProjectContract.COL_COMPANY    + " VARCHAR NOT NULL,"
         + ProjectContract.COL_CLAIMANTNAME    + " VARCHAR NOT NULL,"
         + ProjectContract.COL_RELEVANTSITE    + " VARCHAR,"
         + ProjectContract.COL_ELIGIBLECIR    + " INTEGER,"
         + ProjectContract.COL_ASPARTOFPULPIT    + " BOOLEAN,"
         + ProjectContract.COL_DEADLINE    + " DATETIME,"
         + ProjectContract.COL_DOCUMENTS    + " VARCHAR,"
         + ProjectContract.COL_ACTIVITYTYPE    + " VARCHAR,"
         + ProjectContract.COL_ISVALIDATE    + " BOOLEAN NOT NULL,"
         + ProjectContract.COL_JOB_ID    + " INTEGER NOT NULL,"

        
         + "FOREIGN KEY(" + ProjectContract.COL_JOB_ID + ") REFERENCES " 
             + JobContract.TABLE_NAME 
                + " (" + JobContract.COL_ID + ")"
        + ");"
;
    }

    /**
     * Constructor.
     * @param ctx context
     */
    public ProjectSQLiteAdapterBase(final android.content.Context ctx) {
        super(ctx);
    }

    // Converters

    /**
     * Convert Project entity to Content Values for database.
     * @param item Project entity object
     * @return ContentValues object
     */
    public ContentValues itemToContentValues(final Project item) {
        return ProjectContract.itemToContentValues(item);
    }

    /**
     * Convert android.database.Cursor of database to Project entity.
     * @param cursor android.database.Cursor object
     * @return Project entity
     */
    public Project cursorToItem(final android.database.Cursor cursor) {
        return ProjectContract.cursorToItem(cursor);
    }

    /**
     * Convert android.database.Cursor of database to Project entity.
     * @param cursor android.database.Cursor object
     * @param result Project entity
     */
    public void cursorToItem(final android.database.Cursor cursor, final Project result) {
        ProjectContract.cursorToItem(cursor, result);
    }

    //// CRUD Entity ////
    /**
     * Find & read Project by id in database.
     *
     * @param id Identify of Project
     * @return Project entity
     */
    public Project getByID(final int id) {
        final android.database.Cursor cursor = this.getSingleCursor(id);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
        }

        final Project result = this.cursorToItem(cursor);
        cursor.close();

        final WorkingTimeSQLiteAdapter projectWorkingTimesAdapter =
                new WorkingTimeSQLiteAdapter(this.ctx);
        projectWorkingTimesAdapter.open(this.mDatabase);
        android.database.Cursor projectworkingtimesCursor = projectWorkingTimesAdapter
                    .getByProject(
                            result.getId(),
                            WorkingTimeContract.ALIASED_COLS,
                            null,
                            null,
                            null);
        result.setProjectWorkingTimes(projectWorkingTimesAdapter.cursorToItems(projectworkingtimesCursor));

        projectworkingtimesCursor.close();
        if (result.getJob() != null) {
            final JobSQLiteAdapter jobAdapter =
                    new JobSQLiteAdapter(this.ctx);
            jobAdapter.open(this.mDatabase);

            result.setJob(jobAdapter.getByID(
                            result.getJob().getId()));
        }
        return result;
    }

    /**
     * Find & read Project by job.
     * @param jobId jobId
     * @param orderBy Order by string (can be null)
     * @return List of Project entities
     */
     public android.database.Cursor getByJob(final int jobId, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        String idSelection = ProjectContract.COL_JOB_ID + "= ?";
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
     * Read All Projects entities.
     *
     * @return List of Project entities
     */
    public ArrayList<Project> getAll() {
        final android.database.Cursor cursor = this.getAllCursor();
        final ArrayList<Project> result = this.cursorToItems(cursor);
        cursor.close();

        return result;
    }



    /**
     * Insert a Project entity into database.
     *
     * @param item The Project entity to persist
     * @return Id of the Project entity
     */
    public long insert(final Project item) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Insert DB(" + ProjectContract.TABLE_NAME + ")");
        }

        final ContentValues values =
                ProjectContract.itemToContentValues(item);
        values.remove(ProjectContract.COL_ID);
        int insertResult;
        if (values.size() != 0) {
            insertResult = (int) this.insert(
                    null,
                    values);
        } else {
            insertResult = (int) this.insert(
                    ProjectContract.COL_ID,
                    values);
        }
        item.setId(insertResult);
        if (item.getProjectWorkingTimes() != null) {
            WorkingTimeSQLiteAdapterBase projectWorkingTimesAdapter =
                    new WorkingTimeSQLiteAdapter(this.ctx);
            projectWorkingTimesAdapter.open(this.mDatabase);
            for (WorkingTime workingtime
                        : item.getProjectWorkingTimes()) {
                workingtime.setProject(item);
                projectWorkingTimesAdapter.insertOrUpdate(workingtime);
            }
        }
        return insertResult;
    }

    /**
     * Either insert or update a Project entity into database whether.
     * it already exists or not.
     *
     * @param item The Project entity to persist
     * @return 1 if everything went well, 0 otherwise
     */
    public int insertOrUpdate(final Project item) {
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
     * Update a Project entity into database.
     *
     * @param item The Project entity to persist
     * @return count of updated entities
     */
    public int update(final Project item) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Update DB(" + ProjectContract.TABLE_NAME + ")");
        }

        final ContentValues values =
                ProjectContract.itemToContentValues(item);
        final String whereClause =
                 ProjectContract.COL_ID
                 + " = ?";
        final String[] whereArgs =
                new String[] {String.valueOf(item.getId()) };

        return this.update(
                values,
                whereClause,
                whereArgs);
    }


    /**
     * Delete a Project entity of database.
     *
     * @param id id
     * @return count of updated entities
     */
    public int remove(final int id) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(
                TAG,
                "Delete DB("
                    + ProjectContract.TABLE_NAME
                    + ")"
                    + " id : "+ id);
        }

        final String whereClause =
                ProjectContract.COL_ID
                + " = ?";
        final String[] whereArgs = new String[] {
                    String.valueOf(id)};

        return this.delete(
                whereClause,
                whereArgs);
    }

    /**
     * Deletes the given entity.
     * @param project The entity to delete
     * @return count of updated entities
     */
    public int delete(final Project project) {
        return this.remove(project.getId());
    }

    /**
     *  Internal android.database.Cursor.
     * @param id id
     *  @return A android.database.Cursor pointing to the Project corresponding
     *        to the given id.
     */
    protected android.database.Cursor getSingleCursor(final int id) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Get entities id : " + id);
        }

        final String whereClause =
                ProjectContract.ALIASED_COL_ID
                + " = ?";
        final String[] whereArgs = new String[] {String.valueOf(id)};

        return this.query(
                ProjectContract.ALIASED_COLS,
                whereClause,
                whereArgs,
                null,
                null,
                null);
    }


    /**
     * Query the DB to find a Project entity.
     *
     * @param id The id of the entity to get from the DB
     *
     * @return The cursor pointing to the query's result
     */
    public android.database.Cursor query(final int id) {

        String selection = ProjectContract.ALIASED_COL_ID + " = ?";
        

        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(id);

        return this.query(
                ProjectContract.ALIASED_COLS,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }




}

