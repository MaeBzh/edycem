/*
 * UserProviderAdapterBase.java, Edycem Android
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



import com.imie.edycem.entity.User;
import com.imie.edycem.provider.ProviderAdapter;
import com.imie.edycem.provider.EdycemProvider;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.provider.contract.WorkingTimeContract;
import com.imie.edycem.provider.contract.ProjectContract;
import com.imie.edycem.data.UserSQLiteAdapter;
import com.imie.edycem.data.JobSQLiteAdapter;
import com.imie.edycem.data.WorkingTimeSQLiteAdapter;
import com.imie.edycem.data.ProjectSQLiteAdapter;

/**
 * UserProviderAdapterBase.
 */
public abstract class UserProviderAdapterBase
                extends ProviderAdapter<User> {

    /** TAG for debug purpose. */
    protected static final String TAG = "UserProviderAdapter";

    /** USER_URI. */
    public      static Uri USER_URI;

    /** user type. */
    protected static final String userType =
            "user";

    /** USER_ALL. */
    protected static final int USER_ALL =
            2645995;
    /** USER_ONE. */
    protected static final int USER_ONE =
            2645996;

    /** USER_JOB. */
    protected static final int USER_JOB =
            2645997;
    /** USER_USERWORKINGTIMES. */
    protected static final int USER_USERWORKINGTIMES =
            2645998;
    /** USER_CREATEDPROJECTS. */
    protected static final int USER_CREATEDPROJECTS =
            2645999;

    /**
     * Static constructor.
     */
    static {
        USER_URI =
                EdycemProvider.generateUri(
                        userType);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                userType,
                USER_ALL);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                userType + "/#",
                USER_ONE);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                userType + "/#" + "/job",
                USER_JOB);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                userType + "/#" + "/userworkingtimes",
                USER_USERWORKINGTIMES);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                userType + "/#" + "/createdprojects",
                USER_CREATEDPROJECTS);
    }

    /**
     * Constructor.
     * @param provider Android application provider of Edycem
     */
    public UserProviderAdapterBase(EdycemProvider provider) {
        super(
            provider,
            new UserSQLiteAdapter(provider.getContext()));

        this.uriIds.add(USER_ALL);
        this.uriIds.add(USER_ONE);
        this.uriIds.add(USER_JOB);
        this.uriIds.add(USER_USERWORKINGTIMES);
        this.uriIds.add(USER_CREATEDPROJECTS);
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
            case USER_ALL:
                result = collection + "user";
                break;
            case USER_ONE:
                result = single + "user";
                break;
            case USER_JOB:
                result = single + "user";
                break;
            case USER_USERWORKINGTIMES:
                result = collection + "user";
                break;
            case USER_CREATEDPROJECTS:
                result = collection + "user";
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
            case USER_ONE:
                String id = uri.getPathSegments().get(1);
                selection = UserContract.COL_ID
                        + " = ?";
                selectionArgs = new String[1];
                selectionArgs[0] = id;
                result = this.adapter.delete(
                        selection,
                        selectionArgs);
                break;
            case USER_ALL:
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
            case USER_ALL:
                if (values.size() > 0) {
                    id = (int) this.adapter.insert(null, values);
                } else {
                    id = (int) this.adapter.insert(UserContract.COL_ID, values);
                }
                if (id > 0) {
                    result = Uri.withAppendedPath(
                            USER_URI,
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
        android.database.Cursor userCursor;
        int userId;

        switch (matchedUri) {

            case USER_ALL:
                result = this.adapter.query(
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);
                break;
            case USER_ONE:
                result = this.queryById(uri.getPathSegments().get(1));
                break;

            case USER_JOB:
                userCursor = this.queryById(
                        uri.getPathSegments().get(1));

                if (userCursor.getCount() > 0) {
                    userCursor.moveToFirst();
                    int jobId = userCursor.getInt(
                            userCursor.getColumnIndex(
                                    UserContract.COL_JOB_ID));

                    JobSQLiteAdapter jobAdapter = new JobSQLiteAdapter(this.ctx);
                    jobAdapter.open(this.getDb());
                    result = jobAdapter.query(jobId);
                }
                break;

            case USER_USERWORKINGTIMES:
                userId = Integer.parseInt(uri.getPathSegments().get(1));
                WorkingTimeSQLiteAdapter userWorkingTimesAdapter = new WorkingTimeSQLiteAdapter(this.ctx);
                userWorkingTimesAdapter.open(this.getDb());
                result = userWorkingTimesAdapter.getByUser(userId, WorkingTimeContract.ALIASED_COLS, selection, selectionArgs, null);
                break;

            case USER_CREATEDPROJECTS:
                userId = Integer.parseInt(uri.getPathSegments().get(1));
                ProjectSQLiteAdapter createdProjectsAdapter = new ProjectSQLiteAdapter(this.ctx);
                createdProjectsAdapter.open(this.getDb());
                result = createdProjectsAdapter.getByCreator(userId, ProjectContract.ALIASED_COLS, selection, selectionArgs, null);
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
            case USER_ONE:
                selectionArgs = new String[1];
                selection = UserContract.COL_ID + " = ?";
                selectionArgs[0] = uri.getPathSegments().get(1);
                result = this.adapter.update(
                        values,
                        selection,
                        selectionArgs);
                break;
            case USER_ALL:
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
        return USER_URI;
    }

    /**
     * Query by ID.
     *
     * @param id The id of the entity to retrieve
     * @return The cursor
     */
    private android.database.Cursor queryById(String id) {
        android.database.Cursor result = null;
        String selection = UserContract.ALIASED_COL_ID
                        + " = ?";

        String[] selectionArgs = new String[1];
        selectionArgs[0] = id;
        
        

        result = this.adapter.query(
                    UserContract.ALIASED_COLS,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null);
        return result;
    }
}

