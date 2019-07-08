/*
 * TaskProviderUtilsBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 8, 2019
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
import com.imie.edycem.criterias.base.Criterion;
import com.imie.edycem.criterias.base.Criterion.Type;
import com.imie.edycem.criterias.base.value.ArrayValue;
import com.imie.edycem.criterias.base.CriteriaExpression;
import com.imie.edycem.criterias.base.CriteriaExpression.GroupType;

import com.imie.edycem.entity.Task;
import com.imie.edycem.entity.Activity;
import com.imie.edycem.entity.WorkingTime;

import com.imie.edycem.provider.TaskProviderAdapter;
import com.imie.edycem.provider.ActivityProviderAdapter;
import com.imie.edycem.provider.WorkingTimeProviderAdapter;
import com.imie.edycem.provider.EdycemProvider;
import com.imie.edycem.provider.contract.TaskContract;
import com.imie.edycem.provider.contract.ActivityContract;
import com.imie.edycem.provider.contract.WorkingTimeContract;

/**
 * Task Provider Utils Base.
 *
 * DO NOT MODIFY THIS CLASS AS IT IS REGENERATED
 *
 * This class is a utility class helpful for complex provider calls.
 * ex : inserting an entity and its relations alltogether, etc.
 */
public abstract class TaskProviderUtilsBase
            extends ProviderUtils<Task> {
    /**
     * Tag for debug messages.
     */
    public static final String TAG = "TaskProviderUtilBase";

    /**
     * Constructor.
     * @param context Context
     */
    public TaskProviderUtilsBase(android.content.Context context) {
        super(context);
    }

    @Override
    public Uri insert(final Task item) {
        Uri result = null;
        ArrayList<ContentProviderOperation> operations =
                new ArrayList<ContentProviderOperation>();
        ContentResolver prov = this.getContext().getContentResolver();


        ContentValues itemValues = TaskContract.itemToContentValues(item);
        itemValues.remove(TaskContract.COL_ID);

        operations.add(ContentProviderOperation.newInsert(
                TaskProviderAdapter.TASK_URI)
                        .withValues(itemValues)
                        .build());

        if (item.getTaskWorkingTimes() != null && item.getTaskWorkingTimes().size() > 0) {
            CriteriaExpression crit = new CriteriaExpression(GroupType.AND);
            Criterion inCrit = new Criterion();
            crit.add(inCrit);

            inCrit.setKey(WorkingTimeContract.COL_ID);
            inCrit.setType(Type.IN);
            ArrayValue inValue = new ArrayValue();
            inCrit.addValue(inValue);

            for (int i = 0; i < item.getTaskWorkingTimes().size(); i++) {
                inValue.addValue(String.valueOf(item.getTaskWorkingTimes().get(i).getId()));
            }

            operations.add(ContentProviderOperation.newUpdate(WorkingTimeProviderAdapter.WORKINGTIME_URI)
                    .withValueBackReference(
                            WorkingTimeContract
                                    .COL_TASK_ID,
                            0)
                    .withSelection(
                            crit.toSQLiteSelection(),
                            crit.toSQLiteSelectionArgs())
                    .build());
        }

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
     * @param item Task
     * @return number of row affected
     */
    public int delete(final Task item) {
        int result = -1;
        ContentResolver prov = this.getContext().getContentResolver();

        Uri uri = TaskProviderAdapter.TASK_URI;
        uri = Uri.withAppendedPath(uri, String.valueOf(item.getId()));

        result = prov.delete(uri,
            null,
            null);

        return result;
    }


    /**
     * Query the DB.
     * @param item The item with its ids set
     * @return Task
     */
    public Task query(final Task item) {
        return this.query(item.getId());
    }

    /**
     * Query the DB.
     *
     * @param id id
     *
     * @return Task
     */
    public Task query(final int id) {
        Task result = null;
        ContentResolver prov = this.getContext().getContentResolver();

        CriteriaExpression crits = new CriteriaExpression(GroupType.AND);
        crits.add(TaskContract.ALIASED_COL_ID,
                    String.valueOf(id));

        android.database.Cursor cursor = prov.query(
            TaskProviderAdapter.TASK_URI,
            TaskContract.ALIASED_COLS,
            crits.toSQLiteSelection(),
            crits.toSQLiteSelectionArgs(),
            null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = TaskContract.cursorToItem(cursor);

            if (result.getActivity() != null) {
                result.setActivity(
                    this.getAssociateActivity(result));
            }
            result.setTaskWorkingTimes(
                this.getAssociateTaskWorkingTimes(result));
        }
        cursor.close();

        return result;
    }

    /**
     * Query the DB to get all entities.
     * @return ArrayList<Task>
     */
    public ArrayList<Task> queryAll() {
        ArrayList<Task> result =
                    new ArrayList<Task>();
        ContentResolver prov =
                    this.getContext().getContentResolver();

        android.database.Cursor cursor = prov.query(
                TaskProviderAdapter.TASK_URI,
                TaskContract.ALIASED_COLS,
                null,
                null,
                null);

        result = TaskContract.cursorToItems(cursor);

        cursor.close();

        return result;
    }

    /**
     * Query the DB to get the entities filtered by criteria.
     * @param expression The criteria expression defining the selection and selection args
     * @return ArrayList<Task>
     */
    public ArrayList<Task> query(CriteriaExpression expression) {
        ArrayList<Task> result =
                    new ArrayList<Task>();
        ContentResolver prov = this.getContext().getContentResolver();

        android.database.Cursor cursor = prov.query(
                TaskProviderAdapter.TASK_URI,
                TaskContract.ALIASED_COLS,
                expression.toSQLiteSelection(),
                expression.toSQLiteSelectionArgs(),
                null);

        result = TaskContract.cursorToItems(cursor);

        cursor.close();

        return result;
    }

    /**
     * Updates the DB.
     * @param item Task
     
     * @return number of rows updated
     */
    public int update(final Task item) {
        int result = -1;
        ArrayList<ContentProviderOperation> operations =
                new ArrayList<ContentProviderOperation>();
        ContentResolver prov = this.getContext().getContentResolver();
        ContentValues itemValues = TaskContract.itemToContentValues(
                item);

        Uri uri = TaskProviderAdapter.TASK_URI;
        uri = Uri.withAppendedPath(uri, String.valueOf(item.getId()));


        operations.add(ContentProviderOperation.newUpdate(uri)
                .withValues(itemValues)
                .build());


        if (item.getTaskWorkingTimes() != null && item.getTaskWorkingTimes().size() > 0) {
            String selection;
            String[] selectionArgs;
            // Set new taskWorkingTimes for Task
            CriteriaExpression taskWorkingTimesCrit =
                        new CriteriaExpression(GroupType.AND);
            Criterion crit = new Criterion();
            ArrayValue values = new ArrayValue();
            crit.setType(Type.IN);
            crit.setKey(WorkingTimeContract.COL_ID);
            crit.addValue(values);
            taskWorkingTimesCrit.add(crit);


            for (WorkingTime taskWorkingTimes : item.getTaskWorkingTimes()) {
                values.addValue(
                    String.valueOf(taskWorkingTimes.getId()));
            }
            selection = taskWorkingTimesCrit.toSQLiteSelection();
            selectionArgs = taskWorkingTimesCrit.toSQLiteSelectionArgs();

            operations.add(ContentProviderOperation.newUpdate(
                    WorkingTimeProviderAdapter.WORKINGTIME_URI)
                    .withValue(
                            WorkingTimeContract.COL_TASK_ID,
                            item.getId())
                    .withSelection(
                            selection,
                            selectionArgs)
                    .build());

            // Remove old associated taskWorkingTimes
            crit.setType(Type.NOT_IN);
            taskWorkingTimesCrit.add(WorkingTimeContract.COL_TASK_ID,
                    String.valueOf(item.getId()),
                    Type.EQUALS);


            operations.add(ContentProviderOperation.newUpdate(
                    WorkingTimeProviderAdapter.WORKINGTIME_URI)
                    .withValue(
                            WorkingTimeContract.COL_TASK_ID,
                            null)
                    .withSelection(
                            taskWorkingTimesCrit.toSQLiteSelection(),
                            taskWorkingTimesCrit.toSQLiteSelectionArgs())
                    .build());
        }


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
     * Get associate Activity.
     * @param item Task
     * @return Activity
     */
    public Activity getAssociateActivity(
            final Task item) {
        Activity result;
        ContentResolver prov = this.getContext().getContentResolver();
        android.database.Cursor activityCursor = prov.query(
                ActivityProviderAdapter.ACTIVITY_URI,
                ActivityContract.ALIASED_COLS,
                ActivityContract.ALIASED_COL_ID + "= ?",
                new String[]{String.valueOf(item.getActivity().getId())},
                null);

        if (activityCursor.getCount() > 0) {
            activityCursor.moveToFirst();
            result = ActivityContract.cursorToItem(activityCursor);
        } else {
            result = null;
        }
        activityCursor.close();

        return result;
    }

    /**
     * Get associate TaskWorkingTimes.
     * @param item Task
     * @return WorkingTime
     */
    public ArrayList<WorkingTime> getAssociateTaskWorkingTimes(
            final Task item) {
        ArrayList<WorkingTime> result;
        ContentResolver prov = this.getContext().getContentResolver();
        android.database.Cursor workingTimeCursor = prov.query(
                WorkingTimeProviderAdapter.WORKINGTIME_URI,
                WorkingTimeContract.ALIASED_COLS,
                WorkingTimeContract.ALIASED_COL_TASK_ID
                        + "= ?",
                new String[]{String.valueOf(item.getId())},
                null);

        result = WorkingTimeContract.cursorToItems(
                        workingTimeCursor);
        workingTimeCursor.close();

        return result;
    }

}
