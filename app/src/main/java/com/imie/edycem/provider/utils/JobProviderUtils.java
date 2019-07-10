/*
 * JobProviderUtils.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.provider.utils;



import android.content.ContentResolver;

import com.imie.edycem.criterias.base.CriteriaExpression;
import com.imie.edycem.entity.Job;
import com.imie.edycem.provider.JobProviderAdapter;
import com.imie.edycem.provider.contract.JobContract;
import com.imie.edycem.provider.utils.base.JobProviderUtilsBase;

/**
 * Job Provider Utils.
 *
 * This class is an utility class for wrapping provider calls.
 * Feel free to modify it, add new methods to it, etc.
 */
public class JobProviderUtils
    extends JobProviderUtilsBase {

    /**
     * Constructor.
     * @param context The context
     */
    public JobProviderUtils(android.content.Context context) {
        super(context);
    }

    /**
     * Query the DB.
     *
     * @param id id
     *
     * @return Job
     */
    public Job queryWithName(final String name) {
        Job result = null;
        ContentResolver prov = this.getContext().getContentResolver();

        CriteriaExpression crits = new CriteriaExpression(CriteriaExpression.GroupType.AND);
        crits.add(JobContract.ALIASED_COL_NAME,
                name);

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


}
