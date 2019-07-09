/*
 * TaskProviderUtils.java, Edycem Android
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
import com.imie.edycem.entity.Task;
import com.imie.edycem.provider.TaskProviderAdapter;
import com.imie.edycem.provider.contract.TaskContract;
import com.imie.edycem.provider.utils.base.TaskProviderUtilsBase;

/**
 * Task Provider Utils.
 *
 * This class is an utility class for wrapping provider calls.
 * Feel free to modify it, add new methods to it, etc.
 */
public class TaskProviderUtils
    extends TaskProviderUtilsBase {

    /**
     * Constructor.
     * @param context The context
     */
    public TaskProviderUtils(android.content.Context context) {
        super(context);
    }

    /**
     * Query the DB.
     *
     * @param taskName the name of the task
     *
     * @return Task
     */
    public Task queryWithName(final String taskName) {
        Task result = null;
        ContentResolver prov = this.getContext().getContentResolver();

        CriteriaExpression crits = new CriteriaExpression(CriteriaExpression.GroupType.AND);
        crits.add(TaskContract.ALIASED_COL_NAME,
                taskName);

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


}
