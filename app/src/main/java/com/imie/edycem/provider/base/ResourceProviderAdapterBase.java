/*
 * ResourceProviderAdapterBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.provider.base;

import android.content.ContentValues;
import android.net.Uri;

import com.imie.edycem.entity.base.EntityResourceBase;
import com.imie.edycem.provider.ProviderAdapter;
import com.imie.edycem.provider.EdycemProvider;
import com.imie.edycem.provider.contract.ResourceContract;
import com.imie.edycem.data.ResourceSQLiteAdapter;

/**
 * ResourceProviderAdapterBase.
 */
public abstract class ResourceProviderAdapterBase extends ProviderAdapter<EntityResourceBase> {

    /** TAG for debug purpose. */
    protected static final String TAG = "ResourceProviderAdapter";

    /** IMAGE_URI. */
    public      static Uri RESOURCE_URI;

    /** image type. */
    protected static final String resourceType =
            "image";

    /** RESOURCE_ALL. */
    protected static final int RESOURCE_ALL =
            70760763;
    /** RESOURCE_ONE. */
    protected static final int RESOURCE_ONE =
            70760764;


    /**
     * Static constructor.
     */
    static {
        RESOURCE_URI =
                EdycemProvider.generateUri(
                        resourceType);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                resourceType,
                RESOURCE_ALL);
        EdycemProvider.getUriMatcher().addURI(
                EdycemProvider.authority,
                resourceType + "/#",
                RESOURCE_ONE);
    }

    /**
     * Constructor.
     * @param provider Android application provider of Edycem
     */
    public ResourceProviderAdapterBase(EdycemProvider provider) {
        super(
            provider,
            new ResourceSQLiteAdapter(provider.getContext()));

        this.uriIds.add(RESOURCE_ALL);
        this.uriIds.add(RESOURCE_ONE);
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
            case RESOURCE_ALL:
                result = collection + "resource";
                break;
            case RESOURCE_ONE:
                result = single + "resource";
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
        ContentValues deleteCv = new ContentValues();
        
        int matchedUri = EdycemProviderBase
                    .getUriMatcher().match(uri);
        int result = -1;
        switch (matchedUri) {
            case RESOURCE_ONE:
                String id = uri.getPathSegments().get(1);
                selection = ResourceContract.COL_ID
                        + " = ?";
                selectionArgs = new String[1];
                selectionArgs[0] = id;
                result = this.adapter.update(
                        deleteCv,
                        selection,
                        selectionArgs);
                break;
            case RESOURCE_ALL:
                result = this.adapter.update(
                            deleteCv,
                            selection,
                            selectionArgs);
                break;
            default:
                result = -1;
                break;
        }
        return result;
    }

    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        int matchedUri = EdycemProviderBase
                .getUriMatcher().match(uri);
                Uri result = null;
        int id = 0;
        switch (matchedUri) {
            case RESOURCE_ALL:
                if (values.size() > 0) {
                    id = (int) this.adapter.insert(null, values);
                } else {
                    id = (int) this.adapter.insert(ResourceContract.COL_ID, values);
                }
                if (id > 0) {
                    result = Uri.withAppendedPath(
                            RESOURCE_URI,
                            String.valueOf(id));
                }
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    @Override
    public android.database.Cursor query(final Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        final String sortOrder) {

        int matchedUri = EdycemProviderBase.getUriMatcher()
                .match(uri);
        android.database.Cursor result = null;

        switch (matchedUri) {

            case RESOURCE_ALL:
                result = this.adapter.query(
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);
                break;
            case RESOURCE_ONE:
                result = this.queryById(uri.getPathSegments().get(1));
                break;

            default:
                result = null;
                break;
        }

        return result;
    }

    @Override
    public int update(
            final Uri uri,
            final ContentValues values,
            String selection,
            String[] selectionArgs) {

        int matchedUri = EdycemProviderBase.getUriMatcher()
                .match(uri);
        int result = -1;
        switch (matchedUri) {
            case RESOURCE_ONE:
                selectionArgs = new String[1];
                selection = ResourceContract.COL_ID + " = ?";
                selectionArgs[0] = uri.getPathSegments().get(1);
                result = this.adapter.update(
                        values,
                        selection,
                        selectionArgs);
                break;
            case RESOURCE_ALL:
                result = this.adapter.update(
                            values,
                            selection,
                            selectionArgs);
                break;
            default:
                result = -1;
                break;
        }
        return result;
    }



    /**
     * Get the entity URI.
     * @return The URI
     */
    @Override
    public Uri getUri() {
        return RESOURCE_URI;
    }

    /**
     * Query by ID.
     *
     * @param id The id of the entity to retrieve
     * @return The cursor
     */
    private android.database.Cursor queryById(String id) {
        android.database.Cursor result = null;
        String selection = ResourceContract.ALIASED_COL_ID
                        + " = ?";
        
        String[] selectionArgs = new String[2];
        selectionArgs[0] = id;
        selectionArgs[1] = String.valueOf(0);


        result = this.adapter.query(
                    ResourceContract.ALIASED_COLS,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null);
        return result;
    }
}

