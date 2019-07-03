/*
 * WorkingTimeProviderUtilsBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.provider.utils.base;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;

import android.content.ContentResolver;
import android.content.ContentValues;

import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;


import com.imie.edycem.provider.utils.ProviderUtils;
import com.imie.edycem.criterias.base.CriteriaExpression;
import com.imie.edycem.criterias.base.CriteriaExpression.GroupType;

import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.Task;

import com.imie.edycem.provider.WorkingTimeProviderAdapter;
import com.imie.edycem.provider.UserProviderAdapter;
import com.imie.edycem.provider.ProjectProviderAdapter;
import com.imie.edycem.provider.TaskProviderAdapter;
import com.imie.edycem.provider.EdycemProvider;
import com.imie.edycem.provider.contract.WorkingTimeContract;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.provider.contract.ProjectContract;
import com.imie.edycem.provider.contract.TaskContract;

/**
 * WorkingTime Provider Utils Base.
 *
 * DO NOT MODIFY THIS CLASS AS IT IS REGENERATED
 *
 * This class is a utility class helpful for complex provider calls.
 * ex : inserting an entity and its relations alltogether, etc.
 */
public abstract class WorkingTimeProviderUtilsBase
            extends ProviderUtils<WorkingTime> {
    /**
     * Tag for debug messages.
     */
    public static final String TAG = "WorkingTimeProviderUtilBase";

    /**
     * Constructor.
     * @param context Context
     */
    public WorkingTimeProviderUtilsBase(android.content.Context context) {
        super(context);
    }

    @Override
    public Uri insert(final WorkingTime item) {
        Uri result = null;
        ArrayList<ContentProviderOperation> operations =
                new ArrayList<ContentProviderOperation>();
        ContentResolver prov = this.getContext().getContentResolver();


        ContentValues itemValues = WorkingTimeContract.itemToContentValues(item);
        itemValues.remove(WorkingTimeContract.COL_ID);

        operations.add(ContentProviderOperation.newInsert(
                WorkingTimeProviderAdapter.WORKINGTIME_URI)
                        .withValues(itemValues)
                        .build());


        try {
            ContentProviderResult[] results =
                    prov.applyBatch(EdycemProvider.authority, operations);
            if (results[0] != null) {
                result = results[0].uri;
                item.setId(Integer.parseInt(result.getPathSegments().get(1)));
            }
        } catch (RemoteException e) {
            android.util.Log.e(TAG, e.getMessage());
        } catch (OperationApplicationException e) {
            android.util.Log.e(TAG, e.getMessage());
        }

        return result;
    }


    /**
     * Delete from DB.
     * @param item WorkingTime
     * @return number of row affected
     */
    public int delete(final WorkingTime item) {
        int result = -1;
        ContentResolver prov = this.getContext().getContentResolver();

        Uri uri = WorkingTimeProviderAdapter.WORKINGTIME_URI;
        uri = Uri.withAppendedPath(uri, String.valueOf(item.getId()));

        result = prov.delete(uri,
            null,
            null);

        return result;
    }


    /**
     * Query the DB.
     * @param item The item with its ids set
     * @return WorkingTime
     */
    public WorkingTime query(final WorkingTime item) {
        return this.query(item.getId());
    }

    /**
     * Query the DB.
     *
     * @param id id
     *
     * @return WorkingTime
     */
    public WorkingTime query(final int id) {
        WorkingTime result = null;
        ContentResolver prov = this.getContext().getContentResolver();

        CriteriaExpression crits = new CriteriaExpression(GroupType.AND);
        crits.add(WorkingTimeContract.ALIASED_COL_ID,
                    String.valueOf(id));

        android.database.Cursor cursor = prov.query(
            WorkingTimeProviderAdapter.WORKINGTIME_URI,
            WorkingTimeContract.ALIASED_COLS,
            crits.toSQLiteSelection(),
            crits.toSQLiteSelectionArgs(),
            null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = WorkingTimeContract.cursorToItem(cursor);

            if (result.getUser() != null) {
                result.setUser(
                    this.getAssociateUser(result));
            }
            if (result.getProject() != null) {
                result.setProject(
                    this.getAssociateProject(result));
            }
            if (result.getTask() != null) {
                result.setTask(
                    this.getAssociateTask(result));
            }
        }
        cursor.close();

        return result;
    }

    /**
     * Query the DB to get all entities.
     * @return ArrayList<WorkingTime>
     */
    public ArrayList<WorkingTime> queryAll() {
        ArrayList<WorkingTime> result =
                    new ArrayList<WorkingTime>();
        ContentResolver prov =
                    this.getContext().getContentResolver();

        android.database.Cursor cursor = prov.query(
                WorkingTimeProviderAdapter.WORKINGTIME_URI,
                WorkingTimeContract.ALIASED_COLS,
                null,
                null,
                null);

        result = WorkingTimeContract.cursorToItems(cursor);

        cursor.close();

        return result;
    }

    /**
     * Query the DB to get the entities filtered by criteria.
     * @param expression The criteria expression defining the selection and selection args
     * @return ArrayList<WorkingTime>
     */
    public ArrayList<WorkingTime> query(CriteriaExpression expression) {
        ArrayList<WorkingTime> result =
                    new ArrayList<WorkingTime>();
        ContentResolver prov = this.getContext().getContentResolver();

        android.database.Cursor cursor = prov.query(
                WorkingTimeProviderAdapter.WORKINGTIME_URI,
                WorkingTimeContract.ALIASED_COLS,
                expression.toSQLiteSelection(),
                expression.toSQLiteSelectionArgs(),
                null);

        result = WorkingTimeContract.cursorToItems(cursor);

        cursor.close();

        return result;
    }

    /**
     * Updates the DB.
     * @param item WorkingTime
     
     * @return number of rows updated
     */
    public int update(final WorkingTime item) {
        int result = -1;
        ArrayList<ContentProviderOperation> operations =
                new ArrayList<ContentProviderOperation>();
        ContentResolver prov = this.getContext().getContentResolver();
        ContentValues itemValues = WorkingTimeContract.itemToContentValues(
                item);

        Uri uri = WorkingTimeProviderAdapter.WORKINGTIME_URI;
        uri = Uri.withAppendedPath(uri, String.valueOf(item.getId()));


        operations.add(ContentProviderOperation.newUpdate(uri)
                .withValues(itemValues)
                .build());



        try {
            ContentProviderResult[] results = prov.applyBatch(EdycemProvider.authority, operations);
            result = results[0].count;
        } catch (RemoteException e) {
            android.util.Log.e(TAG, e.getMessage());
        } catch (OperationApplicationException e) {
            android.util.Log.e(TAG, e.getMessage());
        }

        return result;
    }

    /** Relations operations. */
    /**
     * Get associate User.
     * @param item WorkingTime
     * @return User
     */
    public User getAssociateUser(
            final WorkingTime item) {
        User result;
        ContentResolver prov = this.getContext().getContentResolver();
        android.database.Cursor userCursor = prov.query(
                UserProviderAdapter.USER_URI,
                UserContract.ALIASED_COLS,
                UserContract.ALIASED_COL_ID + "= ?",
                new String[]{String.valueOf(item.getUser().getId())},
                null);

        if (userCursor.getCount() > 0) {
            userCursor.moveToFirst();
            result = UserContract.cursorToItem(userCursor);
        } else {
            result = null;
        }
        userCursor.close();

        return result;
    }

    /**
     * Get associate Project.
     * @param item WorkingTime
     * @return Project
     */
    public Project getAssociateProject(
            final WorkingTime item) {
        Project result;
        ContentResolver prov = this.getContext().getContentResolver();
        android.database.Cursor projectCursor = prov.query(
                ProjectProviderAdapter.PROJECT_URI,
                ProjectContract.ALIASED_COLS,
                ProjectContract.ALIASED_COL_ID + "= ?",
                new String[]{String.valueOf(item.getProject().getId())},
                null);

        if (projectCursor.getCount() > 0) {
            projectCursor.moveToFirst();
            result = ProjectContract.cursorToItem(projectCursor);
        } else {
            result = null;
        }
        projectCursor.close();

        return result;
    }

    /**
     * Get associate Task.
     * @param item WorkingTime
     * @return Task
     */
    public Task getAssociateTask(
            final WorkingTime item) {
        Task result;
        ContentResolver prov = this.getContext().getContentResolver();
        android.database.Cursor taskCursor = prov.query(
                TaskProviderAdapter.TASK_URI,
                TaskContract.ALIASED_COLS,
                TaskContract.ALIASED_COL_ID + "= ?",
                new String[]{String.valueOf(item.getTask().getId())},
                null);

        if (taskCursor.getCount() > 0) {
            taskCursor.moveToFirst();
            result = TaskContract.cursorToItem(taskCursor);
        } else {
            result = null;
        }
        taskCursor.close();

        return result;
    }

}
