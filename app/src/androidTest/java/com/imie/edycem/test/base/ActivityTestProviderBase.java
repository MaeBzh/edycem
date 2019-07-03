/*
 * ActivityTestProviderBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.test.base;

import android.test.suitebuilder.annotation.SmallTest;

import com.imie.edycem.provider.ActivityProviderAdapter;
import com.imie.edycem.provider.utils.ActivityProviderUtils;
import com.imie.edycem.provider.contract.ActivityContract;

import com.imie.edycem.data.ActivitySQLiteAdapter;

import com.imie.edycem.entity.Activity;
import com.imie.edycem.entity.Task;


import java.util.ArrayList;
import com.imie.edycem.test.utils.*;


import android.content.ContentResolver;
import android.content.ContentValues;


import android.net.Uri;

import junit.framework.Assert;

/** Activity database test abstract class <br/>
 * <b><i>This class will be overwrited whenever you regenerate the project with Harmony.
 * You should edit ActivityTestDB class instead of this one or you will lose all your modifications.</i></b>
 */
public abstract class ActivityTestProviderBase extends TestDBBase {
    protected android.content.Context ctx;

    protected ActivitySQLiteAdapter adapter;

    protected Activity entity;
    protected ContentResolver provider;
    protected ActivityProviderUtils providerUtils;

    protected ArrayList<Activity> entities;

    protected int nbEntities = 0;
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        this.ctx = this.getContext();

        this.adapter = new ActivitySQLiteAdapter(this.ctx);

        this.provider = this.getContext().getContentResolver();
        this.providerUtils = new ActivityProviderUtils(this.getContext());
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
            Activity activity = ActivityUtils.generateRandom(this.ctx);

            try {
                ContentValues values = ActivityContract.itemToContentValues(activity);
                values.remove(ActivityContract.COL_ID);
                result = this.provider.insert(ActivityProviderAdapter.ACTIVITY_URI, values);

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
        Activity result = null;

        if (this.entity != null) {
            try {
                android.database.Cursor c = this.provider.query(Uri.parse(
                        ActivityProviderAdapter.ACTIVITY_URI
                                + "/"
                                + this.entity.getId()),
                        this.adapter.getCols(),
                        null,
                        null,
                        null);
                c.moveToFirst();
                result = ActivityContract.cursorToItem(c);
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ActivityUtils.equals(this.entity, result);
        }
    }

    /** Test case ReadAll Entity */
    @SmallTest
    public void testReadAll() {
        ArrayList<Activity> result = null;
        try {
            android.database.Cursor c = this.provider.query(ActivityProviderAdapter.ACTIVITY_URI, this.adapter.getCols(), null, null, null);
            result = ActivityContract.cursorToItems(c);
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
            Activity activity = ActivityUtils.generateRandom(this.ctx);

            try {
                activity.setId(this.entity.getId());
                if (this.entity.getTasks() != null) {
                    activity.getTasks().addAll(this.entity.getTasks());
                }

                ContentValues values = ActivityContract.itemToContentValues(activity);
                result = this.provider.update(
                    Uri.parse(ActivityProviderAdapter.ACTIVITY_URI
                        + "/"
                        + activity.getId()),
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
                Activity activity = ActivityUtils.generateRandom(this.ctx);

                try {
                    ContentValues values = ActivityContract.itemToContentValues(activity);
                    values.remove(ActivityContract.COL_ID);

                    result = this.provider.update(ActivityProviderAdapter.ACTIVITY_URI, values, null, null);
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
                        Uri.parse(ActivityProviderAdapter.ACTIVITY_URI
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
                    result = this.provider.delete(ActivityProviderAdapter.ACTIVITY_URI, null, null);

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
        Activity result = null;

        if (this.entity != null) {
            result = this.providerUtils.query(this.entity);

            ActivityUtils.equals(this.entity, result);
        }
    }

    /** Test case ReadAll Entity by provider utils. */
    @SmallTest
    public void testUtilsReadAll() {
        ArrayList<Activity> result = null;
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
            Activity activity = ActivityUtils.generateRandom(this.ctx);

            activity.setId(this.entity.getId());
            if (this.entity.getTasks() != null) {
                for (Task tasks : this.entity.getTasks()) {
                    boolean found = false;
                    for (Task tasks2 : activity.getTasks()) {
                        if (tasks.getId() == tasks2.getId() ) {
                            found = true;
                            break;
                        }
                    }
                    if(!found) {
                        activity.getTasks().add(tasks);
                    }
                }
            }
            result = this.providerUtils.update(activity);

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
