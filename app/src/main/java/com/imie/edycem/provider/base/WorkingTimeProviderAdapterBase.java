/*
 * WorkingTimeProviderAdapterBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 8, 2019
 *
 */
package com.imie.edycem.provider.base;

import android.content.ContentUris;
import android.content.ContentValues;


import com.google.common.collect.ObjectArrays;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;



import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.provider.ProviderAdapter;
import com.imie.edycem.provider.EdycemProvider;
import com.imie.edycem.provider.contract.WorkingTimeContract;
import com.imie.edycem.data.WorkingTimeSQLiteAdapter;
import com.imie.edycem.data.UserSQLiteAdapter;
import com.imie.edycem.data.ProjectSQLiteAdapter;
import com.imie.edycem.data.TaskSQLiteAdapter;

/**
 * WorkingTimeProviderAdapterBase.
 */
public abstract class WorkingTimeProviderAdapterBase
                extends ProviderAdapter<WorkingTime> {

    /** TAG for debug purpose. */
    protected static final String TAG = "WorkingTimeProviderAdapter";

    /** WORKINGTIME_URI. */
    public      static Uri WORKINGTIME_URI;

    /** workingTime type. */
    protected static final String workingTimeType =
            "workingtime";

    /** WORKINGTIME_ALL. */
    protected static final int WORKINGTIME_ALL =
            1797167486;
    /** WORKINGTIME_ONE. */
    protected static final int WORKINGTIME_ONE =
            1797167487;

    /** WORKINGTIME_USER. */
    protected static final int WORKINGTIME_USER =
            1797167488;
    /** WORKINGTIME_PROJECT. */
    protected static final int WORKINGTIME_PROJECT =
            1797167489;
    /** WORKINGTIME_TASK. */
    protected static final int WORKINGTIME_TASK =
            1797167490;

    /**
     * Static constructor.
     */
    static {
        WORKINGTIME_URI =
                EdycemProvider.generateUri(
                        workingTimeType);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                workingTimeType,
                WORKINGTIME_ALL);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                workingTimeType + "/#",
                WORKINGTIME_ONE);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                workingTimeType + "/#" + "/user",
                WORKINGTIME_USER);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                workingTimeType + "/#" + "/project",
                WORKINGTIME_PROJECT);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                workingTimeType + "/#" + "/task",
                WORKINGTIME_TASK);
    }

    /**
     * Constructor.
     * @param provider Android application provider of Edycem
     */
    public WorkingTimeProviderAdapterBase(EdycemProvider provider) {
        super(
            provider,
            new WorkingTimeSQLiteAdapter(provider.getContext()));

        this.uriIds.add(WORKINGTIME_ALL);
        this.uriIds.add(WORKINGTIME_ONE);
        this.uriIds.add(WORKINGTIME_USER);
        this.uriIds.add(WORKINGTIME_PROJECT);
        this.uriIds.add(WORKINGTIME_TASK);
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
            case WORKINGTIME_ALL:
                result = collection + "workingtime";
                break;
            case WORKINGTIME_ONE:
                result = single + "workingtime";
                break;
            case WORKINGTIME_USER:
                result = single + "workingtime";
                break;
            case WORKINGTIME_PROJECT:
                result = single + "workingtime";
                break;
            case WORKINGTIME_TASK:
                result = single + "workingtime";
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
            case WORKINGTIME_ONE:
                String id = uri.getPathSegments().get(1);
                selection = WorkingTimeContract.COL_ID
                        + " = ?";
                selectionArgs = new String[1];
                selectionArgs[0] = id;
                result = this.adapter.delete(
                        selection,
                        selectionArgs);
                break;
            case WORKINGTIME_ALL:
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
            case WORKINGTIME_ALL:
                if (values.size() > 0) {
                    id = (int) this.adapter.insert(null, values);
                } else {
                    id = (int) this.adapter.insert(WorkingTimeContract.COL_ID, values);
                }
                if (id > 0) {
                    result = Uri.withAppendedPath(
                            WORKINGTIME_URI,
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
        android.database.Cursor workingTimeCursor;

        switch (matchedUri) {

            case WORKINGTIME_ALL:
                result = this.adapter.query(
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);
                break;
            case WORKINGTIME_ONE:
                result = this.queryById(uri.getPathSegments().get(1));
                break;

            case WORKINGTIME_USER:
                workingTimeCursor = this.queryById(
                        uri.getPathSegments().get(1));

                if (workingTimeCursor.getCount() > 0) {
                    workingTimeCursor.moveToFirst();
                    int userId = workingTimeCursor.getInt(
                            workingTimeCursor.getColumnIndex(
                                    WorkingTimeContract.COL_USER_ID));

                    UserSQLiteAdapter userAdapter = new UserSQLiteAdapter(this.ctx);
                    userAdapter.open(this.getDb());
                    result = userAdapter.query(userId);
                }
                break;

            case WORKINGTIME_PROJECT:
                workingTimeCursor = this.queryById(
                        uri.getPathSegments().get(1));

                if (workingTimeCursor.getCount() > 0) {
                    workingTimeCursor.moveToFirst();
                    int projectId = workingTimeCursor.getInt(
                            workingTimeCursor.getColumnIndex(
                                    WorkingTimeContract.COL_PROJECT_ID));

                    ProjectSQLiteAdapter projectAdapter = new ProjectSQLiteAdapter(this.ctx);
                    projectAdapter.open(this.getDb());
                    result = projectAdapter.query(projectId);
                }
                break;

            case WORKINGTIME_TASK:
                workingTimeCursor = this.queryById(
                        uri.getPathSegments().get(1));

                if (workingTimeCursor.getCount() > 0) {
                    workingTimeCursor.moveToFirst();
                    int taskId = workingTimeCursor.getInt(
                            workingTimeCursor.getColumnIndex(
                                    WorkingTimeContract.COL_TASK_ID));

                    TaskSQLiteAdapter taskAdapter = new TaskSQLiteAdapter(this.ctx);
                    taskAdapter.open(this.getDb());
                    result = taskAdapter.query(taskId);
                }
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
            case WORKINGTIME_ONE:
                selectionArgs = new String[1];
                selection = WorkingTimeContract.COL_ID + " = ?";
                selectionArgs[0] = uri.getPathSegments().get(1);
                result = this.adapter.update(
                        values,
                        selection,
                        selectionArgs);
                break;
            case WORKINGTIME_ALL:
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
        return WORKINGTIME_URI;
    }

    /**
     * Query by ID.
     *
     * @param id The id of the entity to retrieve
     * @return The cursor
     */
    private android.database.Cursor queryById(String id) {
        android.database.Cursor result = null;
        String selection = WorkingTimeContract.ALIASED_COL_ID
                        + " = ?";

        String[] selectionArgs = new String[1];
        selectionArgs[0] = id;
        
        

        result = this.adapter.query(
                    WorkingTimeContract.ALIASED_COLS,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null);
        return result;
    }
}

