/*
 * JobProviderUtilsBase.java, Edycem Android
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
import com.imie.edycem.criterias.base.Criterion;
import com.imie.edycem.criterias.base.Criterion.Type;
import com.imie.edycem.criterias.base.value.ArrayValue;
import com.imie.edycem.criterias.base.CriteriaExpression;
import com.imie.edycem.criterias.base.CriteriaExpression.GroupType;

import com.imie.edycem.entity.Job;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.Project;

import com.imie.edycem.provider.JobProviderAdapter;
import com.imie.edycem.provider.UserProviderAdapter;
import com.imie.edycem.provider.ProjectProviderAdapter;
import com.imie.edycem.provider.EdycemProvider;
import com.imie.edycem.provider.contract.JobContract;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.provider.contract.ProjectContract;

/**
 * Job Provider Utils Base.
 *
 * DO NOT MODIFY THIS CLASS AS IT IS REGENERATED
 *
 * This class is a utility class helpful for complex provider calls.
 * ex : inserting an entity and its relations alltogether, etc.
 */
public abstract class JobProviderUtilsBase
            extends ProviderUtils<Job> {
    /**
     * Tag for debug messages.
     */
    public static final String TAG = "JobProviderUtilBase";

    /**
     * Constructor.
     * @param context Context
     */
    public JobProviderUtilsBase(android.content.Context context) {
        super(context);
    }

    @Override
    public Uri insert(final Job item) {
        Uri result = null;
        ArrayList<ContentProviderOperation> operations =
                new ArrayList<ContentProviderOperation>();
        ContentResolver prov = this.getContext().getContentResolver();


        ContentValues itemValues = JobContract.itemToContentValues(item);
        itemValues.remove(JobContract.COL_ID);

        operations.add(ContentProviderOperation.newInsert(
                JobProviderAdapter.JOB_URI)
                        .withValues(itemValues)
                        .build());

        if (item.getUsers() != null && item.getUsers().size() > 0) {
            CriteriaExpression crit = new CriteriaExpression(GroupType.AND);
            Criterion inCrit = new Criterion();
            crit.add(inCrit);

            inCrit.setKey(UserContract.COL_ID);
            inCrit.setType(Type.IN);
            ArrayValue inValue = new ArrayValue();
            inCrit.addValue(inValue);

            for (int i = 0; i < item.getUsers().size(); i++) {
                inValue.addValue(String.valueOf(item.getUsers().get(i).getId()));
            }

            operations.add(ContentProviderOperation.newUpdate(UserProviderAdapter.USER_URI)
                    .withValueBackReference(
                            UserContract
                                    .COL_JOB_ID,
                            0)
                    .withSelection(
                            crit.toSQLiteSelection(),
                            crit.toSQLiteSelectionArgs())
                    .build());
        }
        if (item.getProjects() != null && item.getProjects().size() > 0) {
            CriteriaExpression crit = new CriteriaExpression(GroupType.AND);
            Criterion inCrit = new Criterion();
            crit.add(inCrit);

            inCrit.setKey(ProjectContract.COL_ID);
            inCrit.setType(Type.IN);
            ArrayValue inValue = new ArrayValue();
            inCrit.addValue(inValue);

            for (int i = 0; i < item.getProjects().size(); i++) {
                inValue.addValue(String.valueOf(item.getProjects().get(i).getId()));
            }

            operations.add(ContentProviderOperation.newUpdate(ProjectProviderAdapter.PROJECT_URI)
                    .withValueBackReference(
                            ProjectContract
                                    .COL_JOB_ID,
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
     * @param item Job
     * @return number of row affected
     */
    public int delete(final Job item) {
        int result = -1;
        ContentResolver prov = this.getContext().getContentResolver();

        Uri uri = JobProviderAdapter.JOB_URI;
        uri = Uri.withAppendedPath(uri, String.valueOf(item.getId()));

        result = prov.delete(uri,
            null,
            null);

        return result;
    }


    /**
     * Query the DB.
     * @param item The item with its ids set
     * @return Job
     */
    public Job query(final Job item) {
        return this.query(item.getId());
    }

    /**
     * Query the DB.
     *
     * @param id id
     *
     * @return Job
     */
    public Job query(final int id) {
        Job result = null;
        ContentResolver prov = this.getContext().getContentResolver();

        CriteriaExpression crits = new CriteriaExpression(GroupType.AND);
        crits.add(JobContract.ALIASED_COL_ID,
                    String.valueOf(id));

        android.database.Cursor cursor = prov.query(
            JobProviderAdapter.JOB_URI,
            JobContract.ALIASED_COLS,
            crits.toSQLiteSelection(),
            crits.toSQLiteSelectionArgs(),
            null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = JobContract.cursorToItem(cursor);

            result.setUsers(
                this.getAssociateUsers(result));
            result.setProjects(
                this.getAssociateProjects(result));
        }
        cursor.close();

        return result;
    }

    /**
     * Query the DB to get all entities.
     * @return ArrayList<Job>
     */
    public ArrayList<Job> queryAll() {
        ArrayList<Job> result =
                    new ArrayList<Job>();
        ContentResolver prov =
                    this.getContext().getContentResolver();

        android.database.Cursor cursor = prov.query(
                JobProviderAdapter.JOB_URI,
                JobContract.ALIASED_COLS,
                null,
                null,
                null);

        result = JobContract.cursorToItems(cursor);

        cursor.close();

        return result;
    }

    /**
     * Query the DB to get the entities filtered by criteria.
     * @param expression The criteria expression defining the selection and selection args
     * @return ArrayList<Job>
     */
    public ArrayList<Job> query(CriteriaExpression expression) {
        ArrayList<Job> result =
                    new ArrayList<Job>();
        ContentResolver prov = this.getContext().getContentResolver();

        android.database.Cursor cursor = prov.query(
                JobProviderAdapter.JOB_URI,
                JobContract.ALIASED_COLS,
                expression.toSQLiteSelection(),
                expression.toSQLiteSelectionArgs(),
                null);

        result = JobContract.cursorToItems(cursor);

        cursor.close();

        return result;
    }

    /**
     * Updates the DB.
     * @param item Job
     
     * @return number of rows updated
     */
    public int update(final Job item) {
        int result = -1;
        ArrayList<ContentProviderOperation> operations =
                new ArrayList<ContentProviderOperation>();
        ContentResolver prov = this.getContext().getContentResolver();
        ContentValues itemValues = JobContract.itemToContentValues(
                item);

        Uri uri = JobProviderAdapter.JOB_URI;
        uri = Uri.withAppendedPath(uri, String.valueOf(item.getId()));


        operations.add(ContentProviderOperation.newUpdate(uri)
                .withValues(itemValues)
                .build());


        if (item.getUsers() != null && item.getUsers().size() > 0) {
            String selection;
            String[] selectionArgs;
            // Set new users for Job
            CriteriaExpression usersCrit =
                        new CriteriaExpression(GroupType.AND);
            Criterion crit = new Criterion();
            ArrayValue values = new ArrayValue();
            crit.setType(Type.IN);
            crit.setKey(UserContract.COL_ID);
            crit.addValue(values);
            usersCrit.add(crit);


            for (User users : item.getUsers()) {
                values.addValue(
                    String.valueOf(users.getId()));
            }
            selection = usersCrit.toSQLiteSelection();
            selectionArgs = usersCrit.toSQLiteSelectionArgs();

            operations.add(ContentProviderOperation.newUpdate(
                    UserProviderAdapter.USER_URI)
                    .withValue(
                            UserContract.COL_JOB_ID,
                            item.getId())
                    .withSelection(
                            selection,
                            selectionArgs)
                    .build());

            // Remove old associated users
            crit.setType(Type.NOT_IN);
            usersCrit.add(UserContract.COL_JOB_ID,
                    String.valueOf(item.getId()),
                    Type.EQUALS);


            operations.add(ContentProviderOperation.newUpdate(
                    UserProviderAdapter.USER_URI)
                    .withValue(
                            UserContract.COL_JOB_ID,
                            null)
                    .withSelection(
                            usersCrit.toSQLiteSelection(),
                            usersCrit.toSQLiteSelectionArgs())
                    .build());
        }

        if (item.getProjects() != null && item.getProjects().size() > 0) {
            String selection;
            String[] selectionArgs;
            // Set new projects for Job
            CriteriaExpression projectsCrit =
                        new CriteriaExpression(GroupType.AND);
            Criterion crit = new Criterion();
            ArrayValue values = new ArrayValue();
            crit.setType(Type.IN);
            crit.setKey(ProjectContract.COL_ID);
            crit.addValue(values);
            projectsCrit.add(crit);


            for (Project projects : item.getProjects()) {
                values.addValue(
                    String.valueOf(projects.getId()));
            }
            selection = projectsCrit.toSQLiteSelection();
            selectionArgs = projectsCrit.toSQLiteSelectionArgs();

            operations.add(ContentProviderOperation.newUpdate(
                    ProjectProviderAdapter.PROJECT_URI)
                    .withValue(
                            ProjectContract.COL_JOB_ID,
                            item.getId())
                    .withSelection(
                            selection,
                            selectionArgs)
                    .build());

            // Remove old associated projects
            crit.setType(Type.NOT_IN);
            projectsCrit.add(ProjectContract.COL_JOB_ID,
                    String.valueOf(item.getId()),
                    Type.EQUALS);


            operations.add(ContentProviderOperation.newUpdate(
                    ProjectProviderAdapter.PROJECT_URI)
                    .withValue(
                            ProjectContract.COL_JOB_ID,
                            null)
                    .withSelection(
                            projectsCrit.toSQLiteSelection(),
                            projectsCrit.toSQLiteSelectionArgs())
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
     * Get associate Users.
     * @param item Job
     * @return User
     */
    public ArrayList<User> getAssociateUsers(
            final Job item) {
        ArrayList<User> result;
        ContentResolver prov = this.getContext().getContentResolver();
        android.database.Cursor userCursor = prov.query(
                UserProviderAdapter.USER_URI,
                UserContract.ALIASED_COLS,
                UserContract.ALIASED_COL_JOB_ID
                        + "= ?",
                new String[]{String.valueOf(item.getId())},
                null);

        result = UserContract.cursorToItems(
                        userCursor);
        userCursor.close();

        return result;
    }

    /**
     * Get associate Projects.
     * @param item Job
     * @return Project
     */
    public ArrayList<Project> getAssociateProjects(
            final Job item) {
        ArrayList<Project> result;
        ContentResolver prov = this.getContext().getContentResolver();
        android.database.Cursor projectCursor = prov.query(
                ProjectProviderAdapter.PROJECT_URI,
                ProjectContract.ALIASED_COLS,
                ProjectContract.ALIASED_COL_JOB_ID
                        + "= ?",
                new String[]{String.valueOf(item.getId())},
                null);

        result = ProjectContract.cursorToItems(
                        projectCursor);
        projectCursor.close();

        return result;
    }

}
