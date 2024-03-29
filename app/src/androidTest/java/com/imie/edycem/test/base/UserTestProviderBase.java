/*
 * UserTestProviderBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 5, 2019
 *
 */
package com.imie.edycem.test.base;

import android.test.suitebuilder.annotation.SmallTest;

import com.imie.edycem.provider.UserProviderAdapter;
import com.imie.edycem.provider.utils.UserProviderUtils;
import com.imie.edycem.provider.contract.UserContract;

import com.imie.edycem.data.UserSQLiteAdapter;

import com.imie.edycem.entity.User;
import com.imie.edycem.entity.WorkingTime;

import com.imie.edycem.fixture.UserDataLoader;

import java.util.ArrayList;
import com.imie.edycem.test.utils.*;


import android.content.ContentResolver;
import android.content.ContentValues;


import android.net.Uri;

import junit.framework.Assert;

/** User database test abstract class <br/>
 * <b><i>This class will be overwrited whenever you regenerate the project with Harmony.
 * You should edit UserTestDB class instead of this one or you will lose all your modifications.</i></b>
 */
public abstract class UserTestProviderBase extends TestDBBase {
    protected android.content.Context ctx;

    protected UserSQLiteAdapter adapter;

    protected User entity;
    protected ContentResolver provider;
    protected UserProviderUtils providerUtils;

    protected ArrayList<User> entities;

    protected int nbEntities = 0;
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        this.ctx = this.getContext();

        this.adapter = new UserSQLiteAdapter(this.ctx);

        this.entities = new ArrayList<User>();
        this.entities.addAll(UserDataLoader.getInstance(this.ctx).getMap().values());
        if (this.entities.size()>0) {
            this.entity = this.entities.get(TestUtils.generateRandomInt(0,entities.size()-1));
        }

        this.nbEntities += UserDataLoader.getInstance(this.ctx).getMap().size();
        this.provider = this.getContext().getContentResolver();
        this.providerUtils = new UserProviderUtils(this.getContext());
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /********** Direct Provider calls. *******/

    /** Test case Create Entity */
    @SmallTest
    public void testCreate() {
        Uri result = null;
        if (this.entity != null) {
            User user = UserUtils.generateRandom(this.ctx);

            try {
                ContentValues values = UserContract.itemToContentValues(user);
                values.remove(UserContract.COL_ID);
                result = this.provider.insert(UserProviderAdapter.USER_URI, values);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Assert.assertNotNull(result);
            Assert.assertTrue(Integer.parseInt(result.getPathSegments().get(1)) > 0);

        }
    }

    /** Test case Read Entity */
    @SmallTest
    public void testRead() {
        User result = null;

        if (this.entity != null) {
            try {
                android.database.Cursor c = this.provider.query(Uri.parse(
                        UserProviderAdapter.USER_URI
                                + "/"
                                + this.entity.getId()),
                        this.adapter.getCols(),
                        null,
                        null,
                        null);
                c.moveToFirst();
                result = UserContract.cursorToItem(c);
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            UserUtils.equals(this.entity, result);
        }
    }

    /** Test case ReadAll Entity */
    @SmallTest
    public void testReadAll() {
        ArrayList<User> result = null;
        try {
            android.database.Cursor c = this.provider.query(UserProviderAdapter.USER_URI, this.adapter.getCols(), null, null, null);
            result = UserContract.cursorToItems(c);
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(result);
        if (result != null) {
            Assert.assertEquals(result.size(), this.nbEntities);
        }
    }

    /** Test case Update Entity */
    @SmallTest
    public void testUpdate() {
        int result = -1;
        if (this.entity != null) {
            User user = UserUtils.generateRandom(this.ctx);

            try {
                user.setId(this.entity.getId());
                if (this.entity.getUserWorkingTimes() != null) {
                    user.getUserWorkingTimes().addAll(this.entity.getUserWorkingTimes());
                }

                ContentValues values = UserContract.itemToContentValues(user);
                result = this.provider.update(
                    Uri.parse(UserProviderAdapter.USER_URI
                        + "/"
                        + user.getId()),
                    values,
                    null,
                    null);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Assert.assertTrue(result > 0);
        }
    }

    /** Test case UpdateAll Entity */
    @SmallTest
    public void testUpdateAll() {
        int result = -1;
        if (this.entities != null) {
            if (this.entities.size() > 0) {
                User user = UserUtils.generateRandom(this.ctx);

                try {
                    ContentValues values = UserContract.itemToContentValues(user);
                    values.remove(UserContract.COL_ID);
                    values.remove(UserContract.COL_EMAIL);

                    result = this.provider.update(UserProviderAdapter.USER_URI, values, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Assert.assertEquals(result, this.nbEntities);
            }
        }
    }

    /** Test case Delete Entity */
    @SmallTest
    public void testDelete() {
        int result = -1;
        if (this.entity != null) {
            try {
                result = this.provider.delete(
                        Uri.parse(UserProviderAdapter.USER_URI
                            + "/"
                            + this.entity.getId()),
                        null,
                        null);

            } catch (Exception e) {
                e.printStackTrace();
            }
            Assert.assertTrue(result >= 0);
        }

    }

    /** Test case DeleteAll Entity */
    @SmallTest
    public void testDeleteAll() {
        int result = -1;
        if (this.entities != null) {
            if (this.entities.size() > 0) {

                try {
                    result = this.provider.delete(UserProviderAdapter.USER_URI, null, null);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Assert.assertEquals(result, this.nbEntities);
            }
        }
    }

    /****** Provider Utils calls ********/

    /** Test case Read Entity by provider utils. */
    @SmallTest
    public void testUtilsRead() {
        User result = null;

        if (this.entity != null) {
            result = this.providerUtils.query(this.entity);

            UserUtils.equals(this.entity, result);
        }
    }

    /** Test case ReadAll Entity by provider utils. */
    @SmallTest
    public void testUtilsReadAll() {
        ArrayList<User> result = null;
        result = this.providerUtils.queryAll();

        Assert.assertNotNull(result);
        if (result != null) {
            Assert.assertEquals(result.size(), this.nbEntities);
        }
    }

    /** Test case Update Entity by provider utils. */
    @SmallTest
    public void testUtilsUpdate() {
        int result = -1;
        if (this.entity != null) {
            User user = UserUtils.generateRandom(this.ctx);

            user.setId(this.entity.getId());
            if (this.entity.getUserWorkingTimes() != null) {
                for (WorkingTime userWorkingTimes : this.entity.getUserWorkingTimes()) {
                    boolean found = false;
                    for (WorkingTime userWorkingTimes2 : user.getUserWorkingTimes()) {
                        if (userWorkingTimes.getId() == userWorkingTimes2.getId() ) {
                            found = true;
                            break;
                        }
                    }
                    if(!found) {
                        user.getUserWorkingTimes().add(userWorkingTimes);
                    }
                }
            }
            result = this.providerUtils.update(user);

            Assert.assertTrue(result > 0);
        }
    }


    /** Test case Delete Entity by provider utils. */
    @SmallTest
    public void testUtilsDelete() {
        int result = -1;
        if (this.entity != null) {
            result = this.providerUtils.delete(this.entity);
            Assert.assertTrue(result >= 0);
        }

    }
}
