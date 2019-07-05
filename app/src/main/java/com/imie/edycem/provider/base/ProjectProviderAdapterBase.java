/*
 * ProjectProviderAdapterBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 5, 2019
 *
 */
package com.imie.edycem.provider.base;

import android.content.ContentUris;
import android.content.ContentValues;


import com.google.common.collect.ObjectArrays;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;



import com.imie.edycem.entity.Project;
import com.imie.edycem.provider.ProviderAdapter;
import com.imie.edycem.provider.EdycemProvider;
import com.imie.edycem.provider.contract.ProjectContract;
import com.imie.edycem.provider.contract.WorkingTimeContract;
import com.imie.edycem.data.ProjectSQLiteAdapter;
import com.imie.edycem.data.WorkingTimeSQLiteAdapter;
import com.imie.edycem.data.JobSQLiteAdapter;
import com.imie.edycem.data.UserSQLiteAdapter;

/**
 * ProjectProviderAdapterBase.
 */
public abstract class ProjectProviderAdapterBase
                extends ProviderAdapter<Project> {

    /** TAG for debug purpose. */
    protected static final String TAG = "ProjectProviderAdapter";

    /** PROJECT_URI. */
    public      static Uri PROJECT_URI;

    /** project type. */
    protected static final String projectType =
            "project";

    /** PROJECT_ALL. */
    protected static final int PROJECT_ALL =
            1355342585;
    /** PROJECT_ONE. */
    protected static final int PROJECT_ONE =
            1355342586;

    /** PROJECT_PROJECTWORKINGTIMES. */
    protected static final int PROJECT_PROJECTWORKINGTIMES =
            1355342587;
    /** PROJECT_JOB. */
    protected static final int PROJECT_JOB =
            1355342588;
    /** PROJECT_CREATOR. */
    protected static final int PROJECT_CREATOR =
            1355342589;

    /**
     * Static constructor.
     */
    static {
        PROJECT_URI =
                EdycemProvider.generateUri(
                        projectType);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                projectType,
                PROJECT_ALL);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                projectType + "/#",
                PROJECT_ONE);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                projectType + "/#" + "/projectworkingtimes",
                PROJECT_PROJECTWORKINGTIMES);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                projectType + "/#" + "/job",
                PROJECT_JOB);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                projectType + "/#" + "/creator",
                PROJECT_CREATOR);
    }

    /**
     * Constructor.
     * @param provider Android application provider of Edycem
     */
    public ProjectProviderAdapterBase(EdycemProvider provider) {
        super(
            provider,
            new ProjectSQLiteAdapter(provider.getContext()));

        this.uriIds.add(PROJECT_ALL);
        this.uriIds.add(PROJECT_ONE);
        this.uriIds.add(PROJECT_PROJECTWORKINGTIMES);
        this.uriIds.add(PROJECT_JOB);
        this.uriIds.add(PROJECT_CREATOR);
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
            case PROJECT_ALL:
                result = collection + "project";
                break;
            case PROJECT_ONE:
                result = single + "project";
                break;
            case PROJECT_PROJECTWORKINGTIMES:
                result = collection + "project";
                break;
            case PROJECT_JOB:
                result = single + "project";
                break;
            case PROJECT_CREATOR:
                result = single + "project";
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
            case PROJECT_ONE:
                String id = uri.getPathSegments().get(1);
                selection = ProjectContract.COL_ID
                        + " = ?";
                selectionArgs = new String[1];
                selectionArgs[0] = id;
                result = this.adapter.delete(
                        selection,
                        selectionArgs);
                break;
            case PROJECT_ALL:
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
            case PROJECT_ALL:
                if (values.size() > 0) {
                    id = (int) this.adapter.insert(null, values);
                } else {
                    id = (int) this.adapter.insert(ProjectContract.COL_ID, values);
                }
                if (id > 0) {
                    result = Uri.withAppendedPath(
                            PROJECT_URI,
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
        android.database.Cursor projectCursor;
        int projectId;

        switch (matchedUri) {

            case PROJECT_ALL:
                result = this.adapter.query(
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);
                break;
            case PROJECT_ONE:
                result = this.queryById(uri.getPathSegments().get(1));
                break;

            case PROJECT_PROJECTWORKINGTIMES:
                projectId = Integer.parseInt(uri.getPathSegments().get(1));
                WorkingTimeSQLiteAdapter projectWorkingTimesAdapter = new WorkingTimeSQLiteAdapter(this.ctx);
                projectWorkingTimesAdapter.open(this.getDb());
                result = projectWorkingTimesAdapter.getByProject(projectId, WorkingTimeContract.ALIASED_COLS, selection, selectionArgs, null);
                break;

            case PROJECT_JOB:
                projectCursor = this.queryById(
                        uri.getPathSegments().get(1));

                if (projectCursor.getCount() > 0) {
                    projectCursor.moveToFirst();
                    int jobId = projectCursor.getInt(
                            projectCursor.getColumnIndex(
                                    ProjectContract.COL_JOB_ID));

                    JobSQLiteAdapter jobAdapter = new JobSQLiteAdapter(this.ctx);
                    jobAdapter.open(this.getDb());
                    result = jobAdapter.query(jobId);
                }
                break;

            case PROJECT_CREATOR:
                projectCursor = this.queryById(
                        uri.getPathSegments().get(1));

                if (projectCursor.getCount() > 0) {
                    projectCursor.moveToFirst();
                    int creatorId = projectCursor.getInt(
                            projectCursor.getColumnIndex(
                                    ProjectContract.COL_CREATOR_ID));

                    UserSQLiteAdapter userAdapter = new UserSQLiteAdapter(this.ctx);
                    userAdapter.open(this.getDb());
                    result = userAdapter.query(creatorId);
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
            case PROJECT_ONE:
                selectionArgs = new String[1];
                selection = ProjectContract.COL_ID + " = ?";
                selectionArgs[0] = uri.getPathSegments().get(1);
                result = this.adapter.update(
                        values,
                        selection,
                        selectionArgs);
                break;
            case PROJECT_ALL:
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
        return PROJECT_URI;
    }

    /**
     * Query by ID.
     *
     * @param id The id of the entity to retrieve
     * @return The cursor
     */
    private android.database.Cursor queryById(String id) {
        android.database.Cursor result = null;
        String selection = ProjectContract.ALIASED_COL_ID
                        + " = ?";

        String[] selectionArgs = new String[1];
        selectionArgs[0] = id;
        
        

        result = this.adapter.query(
                    ProjectContract.ALIASED_COLS,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null);
        return result;
    }
}

