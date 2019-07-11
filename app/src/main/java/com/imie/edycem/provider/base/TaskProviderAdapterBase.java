/*
 * TaskProviderAdapterBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 11, 2019
 *
 */
package com.imie.edycem.provider.base;

import android.content.ContentUris;
import android.content.ContentValues;


import com.google.common.collect.ObjectArrays;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;



import com.imie.edycem.entity.Task;
import com.imie.edycem.provider.ProviderAdapter;
import com.imie.edycem.provider.EdycemProvider;
import com.imie.edycem.provider.contract.TaskContract;
import com.imie.edycem.provider.contract.WorkingTimeContract;
import com.imie.edycem.data.TaskSQLiteAdapter;
import com.imie.edycem.data.ActivitySQLiteAdapter;
import com.imie.edycem.data.WorkingTimeSQLiteAdapter;

/**
 * TaskProviderAdapterBase.
 */
public abstract class TaskProviderAdapterBase
                extends ProviderAdapter<Task> {

    /** TAG for debug purpose. */
    protected static final String TAG = "TaskProviderAdapter";

    /** TASK_URI. */
    public      static Uri TASK_URI;

    /** task type. */
    protected static final String taskType =
            "task";

    /** TASK_ALL. */
    protected static final int TASK_ALL =
            2599333;
    /** TASK_ONE. */
    protected static final int TASK_ONE =
            2599334;

    /** TASK_ACTIVITY. */
    protected static final int TASK_ACTIVITY =
            2599335;
    /** TASK_TASKWORKINGTIMES. */
    protected static final int TASK_TASKWORKINGTIMES =
            2599336;

    /**
     * Static constructor.
     */
    static {
        TASK_URI =
                EdycemProvider.generateUri(
                        taskType);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                taskType,
                TASK_ALL);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                taskType + "/#",
                TASK_ONE);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                taskType + "/#" + "/activity",
                TASK_ACTIVITY);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                taskType + "/#" + "/taskworkingtimes",
                TASK_TASKWORKINGTIMES);
    }

    /**
     * Constructor.
     * @param provider Android application provider of Edycem
     */
    public TaskProviderAdapterBase(EdycemProvider provider) {
        super(
            provider,
            new TaskSQLiteAdapter(provider.getContext()));

        this.uriIds.add(TASK_ALL);
        this.uriIds.add(TASK_ONE);
        this.uriIds.add(TASK_ACTIVITY);
        this.uriIds.add(TASK_TASKWORKINGTIMES);
    }

    @Override
    public String getType(final Uri uri) {
        String result;
        final String single =
                "vnc.android.cursor.item/"
                    + EdycemProvider.authority + ".";
        final String collection =
                "vnc.android.cursor.collection/"
                    + EdycemProvider.authority + ".";

        int matchedUri = EdycemProviderBase
                .getUriMatcher().match(uri);

        switch (matchedUri) {
            case TASK_ALL:
                result = collection + "task";
                break;
            case TASK_ONE:
                result = single + "task";
                break;
            case TASK_ACTIVITY:
                result = single + "task";
                break;
            case TASK_TASKWORKINGTIMES:
                result = collection + "task";
                break;
            default:
                result = null;
                break;
        }

        return result;
    }

    @Override
    public int delete(
            final Uri uri,
            String selection,
            String[] selectionArgs) {
        int matchedUri = EdycemProviderBase.getUriMatcher().match(uri);
        int result = -1;

        switch (matchedUri) {
            case TASK_ONE:
                String id = uri.getPathSegments().get(1);
                selection = TaskContract.COL_ID
                        + " = ?";
                selectionArgs = new String[1];
                selectionArgs[0] = id;
                result = this.adapter.delete(
                        selection,
                        selectionArgs);
                break;
            case TASK_ALL:
                result = this.adapter.delete(
                            selection,
                            selectionArgs);
                break;
            default:
                result = -1;
                break;
        }

        if (result > 0) {
            this.ctx.getContentResolver().notifyChange(uri, null);
        }

        return result;
    }

    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        int matchedUri = EdycemProviderBase.getUriMatcher().match(uri);
                Uri result = null;
        int id = 0;

        switch (matchedUri) {
            case TASK_ALL:
                if (values.size() > 0) {
                    id = (int) this.adapter.insert(null, values);
                } else {
                    id = (int) this.adapter.insert(TaskContract.COL_ID, values);
                }
                if (id > 0) {
                    result = Uri.withAppendedPath(
                            TASK_URI,
                            String.valueOf(id));
                }
                break;
            default:
                result = null;
                break;
        }

        if (result != null) {
            this.ctx.getContentResolver().notifyChange(uri, null);
        }

        return result;
    }

    @Override
    public android.database.Cursor query(final Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        final String sortOrder) {

        int matchedUri = EdycemProviderBase.getUriMatcher().match(uri);
        android.database.Cursor result = null;
        android.database.Cursor taskCursor;
        int taskId;

        switch (matchedUri) {

            case TASK_ALL:
                result = this.adapter.query(
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);
                break;
            case TASK_ONE:
                result = this.queryById(uri.getPathSegments().get(1));
                break;

            case TASK_ACTIVITY:
                taskCursor = this.queryById(
                        uri.getPathSegments().get(1));

                if (taskCursor.getCount() > 0) {
                    taskCursor.moveToFirst();
                    int activityId = taskCursor.getInt(
                            taskCursor.getColumnIndex(
                                    TaskContract.COL_ACTIVITY_ID));

                    ActivitySQLiteAdapter activityAdapter = new ActivitySQLiteAdapter(this.ctx);
                    activityAdapter.open(this.getDb());
                    result = activityAdapter.query(activityId);
                }
                break;

            case TASK_TASKWORKINGTIMES:
                taskId = Integer.parseInt(uri.getPathSegments().get(1));
                WorkingTimeSQLiteAdapter taskWorkingTimesAdapter = new WorkingTimeSQLiteAdapter(this.ctx);
                taskWorkingTimesAdapter.open(this.getDb());
                result = taskWorkingTimesAdapter.getByTask(taskId, WorkingTimeContract.ALIASED_COLS, selection, selectionArgs, null);
                break;

            default:
                result = null;
                break;
        }

        if (result != null) {
            result.setNotificationUri(this.ctx.getContentResolver(), uri);
        }

        return result;
    }

    @Override
    public int update(
            final Uri uri,
            final ContentValues values,
            String selection,
            String[] selectionArgs) {

        
        int matchedUri = EdycemProviderBase.getUriMatcher().match(uri);
        int result = -1;

        switch (matchedUri) {
            case TASK_ONE:
                selectionArgs = new String[1];
                selection = TaskContract.COL_ID + " = ?";
                selectionArgs[0] = uri.getPathSegments().get(1);
                result = this.adapter.update(
                        values,
                        selection,
                        selectionArgs);
                break;
            case TASK_ALL:
                result = this.adapter.update(
                            values,
                            selection,
                            selectionArgs);
                break;
            default:
                result = -1;
                break;
        }

        if (result > 0) {
            this.ctx.getContentResolver().notifyChange(uri, null);
        }

        return result;
    }



    /**
     * Get the entity URI.
     * @return The URI
     */
    @Override
    public Uri getUri() {
        return TASK_URI;
    }

    /**
     * Query by ID.
     *
     * @param id The id of the entity to retrieve
     * @return The cursor
     */
    private android.database.Cursor queryById(String id) {
        android.database.Cursor result = null;
        String selection = TaskContract.ALIASED_COL_ID
                        + " = ?";

        String[] selectionArgs = new String[1];
        selectionArgs[0] = id;
        
        

        result = this.adapter.query(
                    TaskContract.ALIASED_COLS,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null);
        return result;
    }
}

