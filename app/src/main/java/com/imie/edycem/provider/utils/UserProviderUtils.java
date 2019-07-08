/*
 * UserProviderUtils.java, Edycem Android
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
import com.imie.edycem.entity.User;
import com.imie.edycem.provider.UserProviderAdapter;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.provider.utils.base.UserProviderUtilsBase;

/**
 * User Provider Utils.
 *
 * This class is an utility class for wrapping provider calls.
 * Feel free to modify it, add new methods to it, etc.
 */
public class UserProviderUtils
    extends UserProviderUtilsBase {

    /**
     * Constructor.
     * @param context The context
     */
    public UserProviderUtils(android.content.Context context) {
        super(context);
    }

    /**
     * Query the DB.
     *
     * @param email email
     *
     * @return User
     */
    public User queryWithEmail(final String email) {
        User result = null;
        ContentResolver prov = this.getContext().getContentResolver();

        CriteriaExpression crits = new CriteriaExpression(CriteriaExpression.GroupType.AND);
        crits.add(UserContract.ALIASED_COL_EMAIL,
                email);

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


}
