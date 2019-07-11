/*
 * ActivityProviderUtilsBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 11, 2019
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

import com.imie.edycem.entity.Activity;
import com.imie.edycem.entity.Task;

import com.imie.edycem.provider.ActivityProviderAdapter;
import com.imie.edycem.provider.TaskProviderAdapter;
import com.imie.edycem.provider.EdycemProvider;
import com.imie.edycem.provider.contract.ActivityContract;
import com.imie.edycem.provider.contract.TaskContract;

/**
 * Activity Provider Utils Base.
 *
 * DO NOT MODIFY THIS CLASS AS IT IS REGENERATED
 *
 * This class is a utility class helpful for complex provider calls.
 * ex : inserting an entity and its relations alltogether, etc.
 */
public abstract class ActivityProviderUtilsBase
            extends ProviderUtils<Activity> {
    /**
     * Tag for debug messages.
     */
    public static final String TAG = "ActivityProviderUtilBase";

    /**
     * Constructor.
     * @param context Context
     */
    public ActivityProviderUtilsBase(android.content.Context context) {
        super(context);
    }

    @Override
    public Uri insert(final Activity item) {
        Uri result = null;
        ArrayList<ContentProviderOperation> operations =
                new ArrayList<ContentProviderOperation>();
        ContentResolver prov = this.getContext().getContentResolver();


        ContentValues itemValues = ActivityContract.itemToContentValues(item);
        itemValues.remove(ActivityContract.COL_ID);

        operations.add(ContentProviderOperation.newInsert(
                ActivityProviderAdapter.ACTIVITY_URI)
                        .withValues(itemValues)
                        .build());

        if (item.getTasks() != null && item.getTasks().size() > 0) {
            CriteriaExpression crit = new CriteriaExpression(GroupType.AND);
            Criterion inCrit = new Criterion();
            crit.add(inCrit);

            inCrit.setKey(TaskContract.COL_ID);
            inCrit.setType(Type.IN);
            ArrayValue inValue = new ArrayValue();
            inCrit.addValue(inValue);

            for (int i = 0; i < item.getTasks().size(); i++) {
                inValue.addValue(String.valueOf(item.getTasks().get(i).getId()));
            }

            operations.add(ContentProviderOperation.newUpdate(TaskProviderAdapter.TASK_URI)
                    .withValueBackReference(
                            TaskContract
                                    .COL_ACTIVITY_ID,
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
     * @param item Activity
     * @return number of row affected
     */
    public int delete(final Activity item) {
        int result = -1;
        ContentResolver prov = this.getContext().getContentResolver();

        Uri uri = ActivityProviderAdapter.ACTIVITY_URI;
        uri = Uri.withAppendedPath(uri, String.valueOf(item.getId()));

        result = prov.delete(uri,
            null,
            null);

        return result;
    }


    /**
     * Query the DB.
     * @param item The item with its ids set
     * @return Activity
     */
    public Activity query(final Activity item) {
        return this.query(item.getId());
    }

    /**
     * Query the DB.
     *
     * @param id id
     *
     * @return Activity
     */
    public Activity query(final int id) {
        Activity result = null;
        ContentResolver prov = this.getContext().getContentResolver();

        CriteriaExpression crits = new CriteriaExpression(GroupType.AND);
        crits.add(ActivityContract.ALIASED_COL_ID,
                    String.valueOf(id));

        android.database.Cursor cursor = prov.query(
            ActivityProviderAdapter.ACTIVITY_URI,
            ActivityContract.ALIASED_COLS,
            crits.toSQLiteSelection(),
            crits.toSQLiteSelectionArgs(),
            null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = ActivityContract.cursorToItem(cursor);

            result.setTasks(
                this.getAssociateTasks(result));
        }
        cursor.close();

        return result;
    }

    /**
     * Query the DB to get all entities.
     * @return ArrayList<Activity>
     */
    public ArrayList<Activity> queryAll() {
        ArrayList<Activity> result =
                    new ArrayList<Activity>();
        ContentResolver prov =
                    this.getContext().getContentResolver();

        android.database.Cursor cursor = prov.query(
                ActivityProviderAdapter.ACTIVITY_URI,
                ActivityContract.ALIASED_COLS,
                null,
                null,
                null);

        result = ActivityContract.cursorToItems(cursor);

        cursor.close();

        return result;
    }

    /**
     * Query the DB to get the entities filtered by criteria.
     * @param expression The criteria expression defining the selection and selection args
     * @return ArrayList<Activity>
     */
    public ArrayList<Activity> query(CriteriaExpression expression) {
        ArrayList<Activity> result =
                    new ArrayList<Activity>();
        ContentResolver prov = this.getContext().getContentResolver();

        android.database.Cursor cursor = prov.query(
                ActivityProviderAdapter.ACTIVITY_URI,
                ActivityContract.ALIASED_COLS,
                expression.toSQLiteSelection(),
                expression.toSQLiteSelectionArgs(),
                null);

        result = ActivityContract.cursorToItems(cursor);

        cursor.close();

        return result;
    }

    /**
     * Updates the DB.
     * @param item Activity
     
     * @return number of rows updated
     */
    public int update(final Activity item) {
        int result = -1;
        ArrayList<ContentProviderOperation> operations =
                new ArrayList<ContentProviderOperation>();
        ContentResolver prov = this.getContext().getContentResolver();
        ContentValues itemValues = ActivityContract.itemToContentValues(
                item);

        Uri uri = ActivityProviderAdapter.ACTIVITY_URI;
        uri = Uri.withAppendedPath(uri, String.valueOf(item.getId()));


        operations.add(ContentProviderOperation.newUpdate(uri)
                .withValues(itemValues)
                .build());


        if (item.getTasks() != null && item.getTasks().size() > 0) {
            String selection;
            String[] selectionArgs;
            // Set new tasks for Activity
            CriteriaExpression tasksCrit =
                        new CriteriaExpression(GroupType.AND);
            Criterion crit = new Criterion();
            ArrayValue values = new ArrayValue();
            crit.setType(Type.IN);
            crit.setKey(TaskContract.COL_ID);
            crit.addValue(values);
            tasksCrit.add(crit);


            for (Task tasks : item.getTasks()) {
                values.addValue(
                    String.valueOf(tasks.getId()));
            }
            selection = tasksCrit.toSQLiteSelection();
            selectionArgs = tasksCrit.toSQLiteSelectionArgs();

            operations.add(ContentProviderOperation.newUpdate(
                    TaskProviderAdapter.TASK_URI)
                    .withValue(
                            TaskContract.COL_ACTIVITY_ID,
                            item.getId())
                    .withSelection(
                            selection,
                            selectionArgs)
                    .build());

            // Remove old associated tasks
            crit.setType(Type.NOT_IN);
            tasksCrit.add(TaskContract.COL_ACTIVITY_ID,
                    String.valueOf(item.getId()),
                    Type.EQUALS);


            operations.add(ContentProviderOperation.newUpdate(
                    TaskProviderAdapter.TASK_URI)
                    .withValue(
                            TaskContract.COL_ACTIVITY_ID,
                            null)
                    .withSelection(
                            tasksCrit.toSQLiteSelection(),
                            tasksCrit.toSQLiteSelectionArgs())
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
     * Get associate Tasks.
     * @param item Activity
     * @return Task
     */
    public ArrayList<Task> getAssociateTasks(
            final Activity item) {
        ArrayList<Task> result;
        ContentResolver prov = this.getContext().getContentResolver();
        android.database.Cursor taskCursor = prov.query(
                TaskProviderAdapter.TASK_URI,
                TaskContract.ALIASED_COLS,
                TaskContract.ALIASED_COL_ACTIVITY_ID
                        + "= ?",
                new String[]{String.valueOf(item.getId())},
                null);

        result = TaskContract.cursorToItems(
                        taskCursor);
        taskCursor.close();

        return result;
    }

}
