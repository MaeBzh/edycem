/*
 * UserProviderUtilsBase.java, Edycem Android
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

import com.imie.edycem.entity.User;
import com.imie.edycem.entity.Job;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.entity.Project;

import com.imie.edycem.provider.UserProviderAdapter;
import com.imie.edycem.provider.JobProviderAdapter;
import com.imie.edycem.provider.WorkingTimeProviderAdapter;
import com.imie.edycem.provider.ProjectProviderAdapter;
import com.imie.edycem.provider.EdycemProvider;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.provider.contract.JobContract;
import com.imie.edycem.provider.contract.WorkingTimeContract;
import com.imie.edycem.provider.contract.ProjectContract;

/**
 * User Provider Utils Base.
 *
 * DO NOT MODIFY THIS CLASS AS IT IS REGENERATED
 *
 * This class is a utility class helpful for complex provider calls.
 * ex : inserting an entity and its relations alltogether, etc.
 */
public abstract class UserProviderUtilsBase
            extends ProviderUtils<User> {
    /**
     * Tag for debug messages.
     */
    public static final String TAG = "UserProviderUtilBase";

    /**
     * Constructor.
     * @param context Context
     */
    public UserProviderUtilsBase(android.content.Context context) {
        super(context);
    }

    @Override
    public Uri insert(final User item) {
        Uri result = null;
        ArrayList<ContentProviderOperation> operations =
                new ArrayList<ContentProviderOperation>();
        ContentResolver prov = this.getContext().getContentResolver();


        ContentValues itemValues = UserContract.itemToContentValues(item);
        itemValues.remove(UserContract.COL_ID);

        operations.add(ContentProviderOperation.newInsert(
                UserProviderAdapter.USER_URI)
                        .withValues(itemValues)
                        .build());

        if (item.getUserWorkingTimes() != null && item.getUserWorkingTimes().size() > 0) {
            CriteriaExpression crit = new CriteriaExpression(GroupType.AND);
            Criterion inCrit = new Criterion();
            crit.add(inCrit);

            inCrit.setKey(WorkingTimeContract.COL_ID);
            inCrit.setType(Type.IN);
            ArrayValue inValue = new ArrayValue();
            inCrit.addValue(inValue);

            for (int i = 0; i < item.getUserWorkingTimes().size(); i++) {
                inValue.addValue(String.valueOf(item.getUserWorkingTimes().get(i).getId()));
            }

            operations.add(ContentProviderOperation.newUpdate(WorkingTimeProviderAdapter.WORKINGTIME_URI)
                    .withValueBackReference(
                            WorkingTimeContract
                                    .COL_USER_ID,
                            0)
                    .withSelection(
                            crit.toSQLiteSelection(),
                            crit.toSQLiteSelectionArgs())
                    .build());
        }
        if (item.getCreatedProjects() != null && item.getCreatedProjects().size() > 0) {
            CriteriaExpression crit = new CriteriaExpression(GroupType.AND);
            Criterion inCrit = new Criterion();
            crit.add(inCrit);

            inCrit.setKey(ProjectContract.COL_ID);
            inCrit.setType(Type.IN);
            ArrayValue inValue = new ArrayValue();
            inCrit.addValue(inValue);

            for (int i = 0; i < item.getCreatedProjects().size(); i++) {
                inValue.addValue(String.valueOf(item.getCreatedProjects().get(i).getId()));
            }

            operations.add(ContentProviderOperation.newUpdate(ProjectProviderAdapter.PROJECT_URI)
                    .withValueBackReference(
                            ProjectContract
                                    .COL_CREATOR_ID,
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
     * @param item User
     * @return number of row affected
     */
    public int delete(final User item) {
        int result = -1;
        ContentResolver prov = this.getContext().getContentResolver();

        Uri uri = UserProviderAdapter.USER_URI;
        uri = Uri.withAppendedPath(uri, String.valueOf(item.getId()));

        result = prov.delete(uri,
            null,
            null);

        return result;
    }


    /**
     * Query the DB.
     * @param item The item with its ids set
     * @return User
     */
    public User query(final User item) {
        return this.query(item.getId());
    }

    /**
     * Query the DB.
     *
     * @param id id
     *
     * @return User
     */
    public User query(final int id) {
        User result = null;
        ContentResolver prov = this.getContext().getContentResolver();

        CriteriaExpression crits = new CriteriaExpression(GroupType.AND);
        crits.add(UserContract.ALIASED_COL_ID,
                    String.valueOf(id));

        android.database.Cursor cursor = prov.query(
            UserProviderAdapter.USER_URI,
            UserContract.ALIASED_COLS,
            crits.toSQLiteSelection(),
            crits.toSQLiteSelectionArgs(),
            null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = UserContract.cursorToItem(cursor);

            if (result.getJob() != null) {
                result.setJob(
                    this.getAssociateJob(result));
            }
            result.setUserWorkingTimes(
                this.getAssociateUserWorkingTimes(result));
            result.setCreatedProjects(
                this.getAssociateCreatedProjects(result));
        }
        cursor.close();

        return result;
    }

    /**
     * Query the DB to get all entities.
     * @return ArrayList<User>
     */
    public ArrayList<User> queryAll() {
        ArrayList<User> result =
                    new ArrayList<User>();
        ContentResolver prov =
                    this.getContext().getContentResolver();

        android.database.Cursor cursor = prov.query(
                UserProviderAdapter.USER_URI,
                UserContract.ALIASED_COLS,
                null,
                null,
                null);

        result = UserContract.cursorToItems(cursor);

        cursor.close();

        return result;
    }

    /**
     * Query the DB to get the entities filtered by criteria.
     * @param expression The criteria expression defining the selection and selection args
     * @return ArrayList<User>
     */
    public ArrayList<User> query(CriteriaExpression expression) {
        ArrayList<User> result =
                    new ArrayList<User>();
        ContentResolver prov = this.getContext().getContentResolver();

        android.database.Cursor cursor = prov.query(
                UserProviderAdapter.USER_URI,
                UserContract.ALIASED_COLS,
                expression.toSQLiteSelection(),
                expression.toSQLiteSelectionArgs(),
                null);

        result = UserContract.cursorToItems(cursor);

        cursor.close();

        return result;
    }

    /**
     * Updates the DB.
     * @param item User
     
     * @return number of rows updated
     */
    public int update(final User item) {
        int result = -1;
        ArrayList<ContentProviderOperation> operations =
                new ArrayList<ContentProviderOperation>();
        ContentResolver prov = this.getContext().getContentResolver();
        ContentValues itemValues = UserContract.itemToContentValues(
                item);

        Uri uri = UserProviderAdapter.USER_URI;
        uri = Uri.withAppendedPath(uri, String.valueOf(item.getId()));


        operations.add(ContentProviderOperation.newUpdate(uri)
                .withValues(itemValues)
                .build());


        if (item.getUserWorkingTimes() != null && item.getUserWorkingTimes().size() > 0) {
            String selection;
            String[] selectionArgs;
            // Set new userWorkingTimes for User
            CriteriaExpression userWorkingTimesCrit =
                        new CriteriaExpression(GroupType.AND);
            Criterion crit = new Criterion();
            ArrayValue values = new ArrayValue();
            crit.setType(Type.IN);
            crit.setKey(WorkingTimeContract.COL_ID);
            crit.addValue(values);
            userWorkingTimesCrit.add(crit);


            for (WorkingTime userWorkingTimes : item.getUserWorkingTimes()) {
                values.addValue(
                    String.valueOf(userWorkingTimes.getId()));
            }
            selection = userWorkingTimesCrit.toSQLiteSelection();
            selectionArgs = userWorkingTimesCrit.toSQLiteSelectionArgs();

            operations.add(ContentProviderOperation.newUpdate(
                    WorkingTimeProviderAdapter.WORKINGTIME_URI)
                    .withValue(
                            WorkingTimeContract.COL_USER_ID,
                            item.getId())
                    .withSelection(
                            selection,
                            selectionArgs)
                    .build());

            // Remove old associated userWorkingTimes
            crit.setType(Type.NOT_IN);
            userWorkingTimesCrit.add(WorkingTimeContract.COL_USER_ID,
                    String.valueOf(item.getId()),
                    Type.EQUALS);


            operations.add(ContentProviderOperation.newUpdate(
                    WorkingTimeProviderAdapter.WORKINGTIME_URI)
                    .withValue(
                            WorkingTimeContract.COL_USER_ID,
                            null)
                    .withSelection(
                            userWorkingTimesCrit.toSQLiteSelection(),
                            userWorkingTimesCrit.toSQLiteSelectionArgs())
                    .build());
        }

        if (item.getCreatedProjects() != null && item.getCreatedProjects().size() > 0) {
            String selection;
            String[] selectionArgs;
            // Set new createdProjects for User
            CriteriaExpression createdProjectsCrit =
                        new CriteriaExpression(GroupType.AND);
            Criterion crit = new Criterion();
            ArrayValue values = new ArrayValue();
            crit.setType(Type.IN);
            crit.setKey(ProjectContract.COL_ID);
            crit.addValue(values);
            createdProjectsCrit.add(crit);


            for (Project createdProjects : item.getCreatedProjects()) {
                values.addValue(
                    String.valueOf(createdProjects.getId()));
            }
            selection = createdProjectsCrit.toSQLiteSelection();
            selectionArgs = createdProjectsCrit.toSQLiteSelectionArgs();

            operations.add(ContentProviderOperation.newUpdate(
                    ProjectProviderAdapter.PROJECT_URI)
                    .withValue(
                            ProjectContract.COL_CREATOR_ID,
                            item.getId())
                    .withSelection(
                            selection,
                            selectionArgs)
                    .build());

            // Remove old associated createdProjects
            crit.setType(Type.NOT_IN);
            createdProjectsCrit.add(ProjectContract.COL_CREATOR_ID,
                    String.valueOf(item.getId()),
                    Type.EQUALS);


            operations.add(ContentProviderOperation.newUpdate(
                    ProjectProviderAdapter.PROJECT_URI)
                    .withValue(
                            ProjectContract.COL_CREATOR_ID,
                            null)
                    .withSelection(
                            createdProjectsCrit.toSQLiteSelection(),
                            createdProjectsCrit.toSQLiteSelectionArgs())
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
     * Get associate Job.
     * @param item User
     * @return Job
     */
    public Job getAssociateJob(
            final User item) {
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
     * Get associate UserWorkingTimes.
     * @param item User
     * @return WorkingTime
     */
    public ArrayList<WorkingTime> getAssociateUserWorkingTimes(
            final User item) {
        ArrayList<WorkingTime> result;
        ContentResolver prov = this.getContext().getContentResolver();
        android.database.Cursor workingTimeCursor = prov.query(
                WorkingTimeProviderAdapter.WORKINGTIME_URI,
                WorkingTimeContract.ALIASED_COLS,
                WorkingTimeContract.ALIASED_COL_USER_ID
                        + "= ?",
                new String[]{String.valueOf(item.getId())},
                null);

        result = WorkingTimeContract.cursorToItems(
                        workingTimeCursor);
        workingTimeCursor.close();

        return result;
    }

    /**
     * Get associate CreatedProjects.
     * @param item User
     * @return Project
     */
    public ArrayList<Project> getAssociateCreatedProjects(
            final User item) {
        ArrayList<Project> result;
        ContentResolver prov = this.getContext().getContentResolver();
        android.database.Cursor projectCursor = prov.query(
                ProjectProviderAdapter.PROJECT_URI,
                ProjectContract.ALIASED_COLS,
                ProjectContract.ALIASED_COL_CREATOR_ID
                        + "= ?",
                new String[]{String.valueOf(item.getId())},
                null);

        result = ProjectContract.cursorToItems(
                        projectCursor);
        projectCursor.close();

        return result;
    }

}
