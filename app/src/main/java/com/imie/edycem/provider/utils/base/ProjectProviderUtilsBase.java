/*
 * ProjectProviderUtilsBase.java, Edycem Android
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

import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.entity.Job;
import com.imie.edycem.entity.User;

import com.imie.edycem.provider.ProjectProviderAdapter;
import com.imie.edycem.provider.WorkingTimeProviderAdapter;
import com.imie.edycem.provider.JobProviderAdapter;
import com.imie.edycem.provider.UserProviderAdapter;
import com.imie.edycem.provider.EdycemProvider;
import com.imie.edycem.provider.contract.ProjectContract;
import com.imie.edycem.provider.contract.WorkingTimeContract;
import com.imie.edycem.provider.contract.JobContract;
import com.imie.edycem.provider.contract.UserContract;

/**
 * Project Provider Utils Base.
 *
 * DO NOT MODIFY THIS CLASS AS IT IS REGENERATED
 *
 * This class is a utility class helpful for complex provider calls.
 * ex : inserting an entity and its relations alltogether, etc.
 */
public abstract class ProjectProviderUtilsBase
            extends ProviderUtils<Project> {
    /**
     * Tag for debug messages.
     */
    public static final String TAG = "ProjectProviderUtilBase";

    /**
     * Constructor.
     * @param context Context
     */
    public ProjectProviderUtilsBase(android.content.Context context) {
        super(context);
    }

    @Override
    public Uri insert(final Project item) {
        Uri result = null;
        ArrayList<ContentProviderOperation> operations =
                new ArrayList<ContentProviderOperation>();
        ContentResolver prov = this.getContext().getContentResolver();


        ContentValues itemValues = ProjectContract.itemToContentValues(item);
        itemValues.remove(ProjectContract.COL_ID);

        operations.add(ContentProviderOperation.newInsert(
                ProjectProviderAdapter.PROJECT_URI)
                        .withValues(itemValues)
                        .build());

        if (item.getProjectWorkingTimes() != null && item.getProjectWorkingTimes().size() > 0) {
            CriteriaExpression crit = new CriteriaExpression(GroupType.AND);
            Criterion inCrit = new Criterion();
            crit.add(inCrit);

            inCrit.setKey(WorkingTimeContract.COL_ID);
            inCrit.setType(Type.IN);
            ArrayValue inValue = new ArrayValue();
            inCrit.addValue(inValue);

            for (int i = 0; i < item.getProjectWorkingTimes().size(); i++) {
                inValue.addValue(String.valueOf(item.getProjectWorkingTimes().get(i).getId()));
            }

            operations.add(ContentProviderOperation.newUpdate(WorkingTimeProviderAdapter.WORKINGTIME_URI)
                    .withValueBackReference(
                            WorkingTimeContract
                                    .COL_PROJECT_ID,
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
     * @param item Project
     * @return number of row affected
     */
    public int delete(final Project item) {
        int result = -1;
        ContentResolver prov = this.getContext().getContentResolver();

        Uri uri = ProjectProviderAdapter.PROJECT_URI;
        uri = Uri.withAppendedPath(uri, String.valueOf(item.getId()));

        result = prov.delete(uri,
            null,
            null);

        return result;
    }


    /**
     * Query the DB.
     * @param item The item with its ids set
     * @return Project
     */
    public Project query(final Project item) {
        return this.query(item.getId());
    }

    /**
     * Query the DB.
     *
     * @param id id
     *
     * @return Project
     */
    public Project query(final int id) {
        Project result = null;
        ContentResolver prov = this.getContext().getContentResolver();

        CriteriaExpression crits = new CriteriaExpression(GroupType.AND);
        crits.add(ProjectContract.ALIASED_COL_ID,
                    String.valueOf(id));

        android.database.Cursor cursor = prov.query(
            ProjectProviderAdapter.PROJECT_URI,
            ProjectContract.ALIASED_COLS,
            crits.toSQLiteSelection(),
            crits.toSQLiteSelectionArgs(),
            null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = ProjectContract.cursorToItem(cursor);

            result.setProjectWorkingTimes(
                this.getAssociateProjectWorkingTimes(result));
            if (result.getJob() != null) {
                result.setJob(
                    this.getAssociateJob(result));
            }
            if (result.getCreator() != null) {
                result.setCreator(
                    this.getAssociateCreator(result));
            }
        }
        cursor.close();

        return result;
    }

    /**
     * Query the DB to get all entities.
     * @return ArrayList<Project>
     */
    public ArrayList<Project> queryAll() {
        ArrayList<Project> result =
                    new ArrayList<Project>();
        ContentResolver prov =
                    this.getContext().getContentResolver();

        android.database.Cursor cursor = prov.query(
                ProjectProviderAdapter.PROJECT_URI,
                ProjectContract.ALIASED_COLS,
                null,
                null,
                null);

        result = ProjectContract.cursorToItems(cursor);

        cursor.close();

        return result;
    }

    /**
     * Query the DB to get the entities filtered by criteria.
     * @param expression The criteria expression defining the selection and selection args
     * @return ArrayList<Project>
     */
    public ArrayList<Project> query(CriteriaExpression expression) {
        ArrayList<Project> result =
                    new ArrayList<Project>();
        ContentResolver prov = this.getContext().getContentResolver();

        android.database.Cursor cursor = prov.query(
                ProjectProviderAdapter.PROJECT_URI,
                ProjectContract.ALIASED_COLS,
                expression.toSQLiteSelection(),
                expression.toSQLiteSelectionArgs(),
                null);

        result = ProjectContract.cursorToItems(cursor);

        cursor.close();

        return result;
    }

    /**
     * Updates the DB.
     * @param item Project
     
     * @return number of rows updated
     */
    public int update(final Project item) {
        int result = -1;
        ArrayList<ContentProviderOperation> operations =
                new ArrayList<ContentProviderOperation>();
        ContentResolver prov = this.getContext().getContentResolver();
        ContentValues itemValues = ProjectContract.itemToContentValues(
                item);

        Uri uri = ProjectProviderAdapter.PROJECT_URI;
        uri = Uri.withAppendedPath(uri, String.valueOf(item.getId()));


        operations.add(ContentProviderOperation.newUpdate(uri)
                .withValues(itemValues)
                .build());


        if (item.getProjectWorkingTimes() != null && item.getProjectWorkingTimes().size() > 0) {
            String selection;
            String[] selectionArgs;
            // Set new projectWorkingTimes for Project
            CriteriaExpression projectWorkingTimesCrit =
                        new CriteriaExpression(GroupType.AND);
            Criterion crit = new Criterion();
            ArrayValue values = new ArrayValue();
            crit.setType(Type.IN);
            crit.setKey(WorkingTimeContract.COL_ID);
            crit.addValue(values);
            projectWorkingTimesCrit.add(crit);


            for (WorkingTime projectWorkingTimes : item.getProjectWorkingTimes()) {
                values.addValue(
                    String.valueOf(projectWorkingTimes.getId()));
            }
            selection = projectWorkingTimesCrit.toSQLiteSelection();
            selectionArgs = projectWorkingTimesCrit.toSQLiteSelectionArgs();

            operations.add(ContentProviderOperation.newUpdate(
                    WorkingTimeProviderAdapter.WORKINGTIME_URI)
                    .withValue(
                            WorkingTimeContract.COL_PROJECT_ID,
                            item.getId())
                    .withSelection(
                            selection,
                            selectionArgs)
                    .build());

            // Remove old associated projectWorkingTimes
            crit.setType(Type.NOT_IN);
            projectWorkingTimesCrit.add(WorkingTimeContract.COL_PROJECT_ID,
                    String.valueOf(item.getId()),
                    Type.EQUALS);


            operations.add(ContentProviderOperation.newUpdate(
                    WorkingTimeProviderAdapter.WORKINGTIME_URI)
                    .withValue(
                            WorkingTimeContract.COL_PROJECT_ID,
                            null)
                    .withSelection(
                            projectWorkingTimesCrit.toSQLiteSelection(),
                            projectWorkingTimesCrit.toSQLiteSelectionArgs())
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
     * Get associate ProjectWorkingTimes.
     * @param item Project
     * @return WorkingTime
     */
    public ArrayList<WorkingTime> getAssociateProjectWorkingTimes(
            final Project item) {
        ArrayList<WorkingTime> result;
        ContentResolver prov = this.getContext().getContentResolver();
        android.database.Cursor workingTimeCursor = prov.query(
                WorkingTimeProviderAdapter.WORKINGTIME_URI,
                WorkingTimeContract.ALIASED_COLS,
                WorkingTimeContract.ALIASED_COL_PROJECT_ID
                        + "= ?",
                new String[]{String.valueOf(item.getId())},
                null);

        result = WorkingTimeContract.cursorToItems(
                        workingTimeCursor);
        workingTimeCursor.close();

        return result;
    }

    /**
     * Get associate Job.
     * @param item Project
     * @return Job
     */
    public Job getAssociateJob(
            final Project item) {
        Job result;
        ContentResolver prov = this.getContext().getContentResolver();
        android.database.Cursor jobCursor = prov.query(
                JobProviderAdapter.JOB_URI,
                JobContract.ALIASED_COLS,
                JobContract.ALIASED_COL_ID + "= ?",
                new String[]{String.valueOf(item.getJob().getId())},
                null);

        if (jobCursor.getCount() > 0) {
            jobCursor.moveToFirst();
            result = JobContract.cursorToItem(jobCursor);
        } else {
            result = null;
        }
        jobCursor.close();

        return result;
    }

    /**
     * Get associate Creator.
     * @param item Project
     * @return User
     */
    public User getAssociateCreator(
            final Project item) {
        User result;
        ContentResolver prov = this.getContext().getContentResolver();
        android.database.Cursor userCursor = prov.query(
                UserProviderAdapter.USER_URI,
                UserContract.ALIASED_COLS,
                UserContract.ALIASED_COL_ID + "= ?",
                new String[]{String.valueOf(item.getCreator().getId())},
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

}
