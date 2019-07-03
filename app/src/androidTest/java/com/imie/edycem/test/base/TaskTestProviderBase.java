/*
 * TaskTestProviderBase.java, Edycem Android
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

import com.imie.edycem.provider.TaskProviderAdapter;
import com.imie.edycem.provider.utils.TaskProviderUtils;
import com.imie.edycem.provider.contract.TaskContract;

import com.imie.edycem.data.TaskSQLiteAdapter;

import com.imie.edycem.entity.Task;
import com.imie.edycem.entity.WorkingTime;


import java.util.ArrayList;
import com.imie.edycem.test.utils.*;


import android.content.ContentResolver;
import android.content.ContentValues;


import android.net.Uri;

import junit.framework.Assert;

/** Task database test abstract class <br/>
 * <b><i>This class will be overwrited whenever you regenerate the project with Harmony.
 * You should edit TaskTestDB class instead of this one or you will lose all your modifications.</i></b>
 */
public abstract class TaskTestProviderBase extends TestDBBase {
    protected android.content.Context ctx;

    protected TaskSQLiteAdapter adapter;

    protected Task entity;
    protected ContentResolver provider;
    protected TaskProviderUtils providerUtils;

    protected ArrayList<Task> entities;

    protected int nbEntities = 0;
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        this.ctx = this.getContext();

        this.adapter = new TaskSQLiteAdapter(this.ctx);

        this.provider = this.getContext().getContentResolver();
        this.providerUtils = new TaskProviderUtils(this.getContext());
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
            Task task = TaskUtils.generateRandom(this.ctx);

            try {
                ContentValues values = TaskContract.itemToContentValues(task);
                values.remove(TaskContract.COL_ID);
                result = this.provider.insert(TaskProviderAdapter.TASK_URI, values);

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
        Task result = null;

        if (this.entity != null) {
            try {
                android.database.Cursor c = this.provider.query(Uri.parse(
                        TaskProviderAdapter.TASK_URI
                                + "/"
                                + this.entity.getId()),
                        this.adapter.getCols(),
                        null,
                        null,
                        null);
                c.moveToFirst();
                result = TaskContract.cursorToItem(c);
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            TaskUtils.equals(this.entity, result);
        }
    }

    /** Test case ReadAll Entity */
    @SmallTest
    public void testReadAll() {
        ArrayList<Task> result = null;
        try {
            android.database.Cursor c = this.provider.query(TaskProviderAdapter.TASK_URI, this.adapter.getCols(), null, null, null);
            result = TaskContract.cursorToItems(c);
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
            Task task = TaskUtils.generateRandom(this.ctx);

            try {
                task.setId(this.entity.getId());
                if (this.entity.getTaskWorkingTimes() != null) {
                    task.getTaskWorkingTimes().addAll(this.entity.getTaskWorkingTimes());
                }

                ContentValues values = TaskContract.itemToContentValues(task);
                result = this.provider.update(
                    Uri.parse(TaskProviderAdapter.TASK_URI
                        + "/"
                        + task.getId()),
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
                Task task = TaskUtils.generateRandom(this.ctx);

                try {
                    ContentValues values = TaskContract.itemToContentValues(task);
                    values.remove(TaskContract.COL_ID);

                    result = this.provider.update(TaskProviderAdapter.TASK_URI, values, null, null);
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
                        Uri.parse(TaskProviderAdapter.TASK_URI
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
                    result = this.provider.delete(TaskProviderAdapter.TASK_URI, null, null);

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
        Task result = null;

        if (this.entity != null) {
            result = this.providerUtils.query(this.entity);

            TaskUtils.equals(this.entity, result);
        }
    }

    /** Test case ReadAll Entity by provider utils. */
    @SmallTest
    public void testUtilsReadAll() {
        ArrayList<Task> result = null;
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
            Task task = TaskUtils.generateRandom(this.ctx);

            task.setId(this.entity.getId());
            if (this.entity.getTaskWorkingTimes() != null) {
                for (WorkingTime taskWorkingTimes : this.entity.getTaskWorkingTimes()) {
                    boolean found = false;
                    for (WorkingTime taskWorkingTimes2 : task.getTaskWorkingTimes()) {
                        if (taskWorkingTimes.getId() == taskWorkingTimes2.getId() ) {
                            found = true;
                            break;
                        }
                    }
                    if(!found) {
                        task.getTaskWorkingTimes().add(taskWorkingTimes);
                    }
                }
            }
            result = this.providerUtils.update(task);

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
